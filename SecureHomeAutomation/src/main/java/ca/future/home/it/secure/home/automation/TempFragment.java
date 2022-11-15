/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class TempFragment extends Fragment {
    String userId, localuserId, minkey, maxkey, key;
    UserInfo userInfo = new UserInfo();

    TextView tvMinimumTemperature;
    TextView tvMaximumTemperature;
    TextView tvHeater;
    TextView tvAC;
    TextView tvCurrentTemperature;
    TextView tvHumidity;
    Button btnMaxTemp;
    Button btnMinTemp;
    FirebaseDatabase database;
    DatabaseReference minTempRef;
    DatabaseReference maxTempRef;
    ArcGauge temperatureView;
    int minimumTemperature = 0;
    int maximumTemperature = 0;
    ProgressDialog progressDialog;

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbID();
        init(view);
        setListeners();
    }

    private void init(View view) {
        tvCurrentTemperature = view.findViewById(R.id.CurrentTemp);
        tvHumidity = view.findViewById(R.id.Humidity);
        tvMinimumTemperature = view.findViewById(R.id.MaximumTemperature);
        tvMaximumTemperature = view.findViewById(R.id.MinimumTemperature);
        tvAC = view.findViewById(R.id.AC);
        tvHeater = view.findViewById(R.id.heater);
        btnMaxTemp = view.findViewById(R.id.btnMaxTemperature);
        btnMinTemp = view.findViewById(R.id.btnMinTemperature);
        database = FirebaseDatabase.getInstance();
        temperatureView = view.findViewById(R.id.TemperatureView);
        minTempRef = database.getReference(minkey);
        maxTempRef = database.getReference(maxkey);
        setTemperatureView(temperatureView);
        setCurrentTemperature(23);
        setHumidity(20);
        turnOffHeater();
        turnOnAc();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        loadTemperatureConfigurations();
    }

    private void dbID(){
        userInfo.typeAccount();

        localuserId=userInfo.userId;
        userId=userInfo.idInfo;

        if(localuserId!=null){
            key=localuserId;
            Log.d(TAG,key);
        }
        if(userId!=null) {
            key= userId;
            Log.d(TAG, key);
        }
        minkey=key+getString(R.string.tempmin);
        maxkey=key+getString(R.string.tempmax);
        Log.d(TAG,"test2022 temp data out to db\n min key"+minkey+"\n max key"+maxkey);


    }
    private void setListeners() {
        btnMinTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForInputTemperature(true);
            }
        });

        btnMaxTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForInputTemperature(false);
            }
        });
    }

    private void showDialogForInputTemperature(boolean isMinTemperature) {
        String title = isMinTemperature ? "Minimum Temperature" : "Maximum Temperature";
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.temperature_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        TextView tvTitle = promptsView.findViewById(R.id.Title);
        tvTitle.setText(title);
        EditText userInput = (EditText) promptsView.findViewById(R.id.TemperatureValue);
        if(isMinTemperature) {
            if(minimumTemperature > 0)
                userInput.setText(minimumTemperature+"");
        } else {
            if (maximumTemperature > 0)
                userInput.setText(maximumTemperature+"");
        }
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok_temp, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int temperature = Integer.parseInt(userInput.getText().toString());
                        if (isMinTemperature) {
                            if (temperature < maximumTemperature && temperature > 0) {
                                saveMinTemperature(temperature);
                            } else {
                                showToast(getString(R.string.mini_temp_text));
                                userInput.requestFocus();
                            }
                        } else {
                            if (temperature > minimumTemperature && temperature > 0) {
                                saveMaxTemperature(temperature);
                            } else {
                                showToast(getString(R.string.max_temp_text));
                            }

                        }
                    }
                })
                .setNegativeButton(R.string.cancel_temp,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void saveMinTemperature(int temp) {
        progressDialog.setMessage(getString(R.string.savie));
        progressDialog.show();
        minTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closeProgressDialog();
                if (databaseError != null) {
                    showToast(getString(R.string.min_temp_min_temp) + databaseError.getMessage());
                } else {
                    minimumTemperature = temp;
                    setMinTemperature(minimumTemperature);
                }
            }
        });
    }

    private void saveMaxTemperature(int temp) {
        progressDialog.setMessage(getString(R.string.savings));
        progressDialog.show();
        maxTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closeProgressDialog();
                if (databaseError != null) {
                    showToast(getString(R.string.max_temp_text_temp) + databaseError.getMessage());
                } else {
                    maximumTemperature = temp;
                    setMaxTemperature(maximumTemperature);
                }
            }
        });
    }

    private void loadTemperatureConfigurations() {
        minTempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long temperature = (long) snapshot.getValue();
                minimumTemperature = (int) temperature;
                setMinTemperature(minimumTemperature);
                maxTempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long temperature = (long) snapshot.getValue();
                        maximumTemperature = (int) temperature;
                        setMaxTemperature(maximumTemperature);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d("abc","not loaded");
            }
        });
    }

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setTemperatureView(ArcGauge temperatureView) {

        this.temperatureView = temperatureView;
        Range range1 = new Range();
        range1.setColor(Color.parseColor("#09B2FF"));
        range1.setFrom(0);
        range1.setTo(19);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#FFC107"));
        range2.setFrom(20);
        range2.setTo(28);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#F44336"));
        range3.setFrom(29);
        range3.setTo(75);

        //add color ranges to gauge
        temperatureView.addRange(range1);
        temperatureView.addRange(range2);
        temperatureView.addRange(range3);

        temperatureView.setMinValue(0);
        temperatureView.setMaxValue(75);
        temperatureView.setValueColor(Color.parseColor("#ffffff"));
        temperatureView.setVisibility(View.VISIBLE);
    }

    private void setCurrentTemperature(int value) {
        tvCurrentTemperature.setText(value+"\u00B0 C");
        temperatureView.setValue(value);
    }


    private void setHumidity(int value) {
        tvHumidity.setText(getString(R.string.Humidity)+value+"%");
    }

    private void setMinTemperature(int value) {
        tvMinimumTemperature.setText(value+"\u00B0 C");
    }
    private void setMaxTemperature(int value) {
        tvMaximumTemperature.setText(value+"\u00B0 C");

    }

    private void turnOnAc() {
        tvAC.setTextColor(getResources().getColor(R.color.color_ac_indicator));
        tvAC.setTypeface(tvAC.getTypeface(), Typeface.BOLD);
    }

    private void turnOffAc() {
        tvAC.setTextColor(getResources().getColor(R.color.color_off_indicator));
        tvAC.setTypeface(tvAC.getTypeface(), Typeface.NORMAL);
    }

    private void turnOnHeater() {
        tvHeater.setTextColor(getResources().getColor(R.color.color_heater_indicator));
        tvHeater.setTypeface(tvHeater.getTypeface(), Typeface.BOLD);
    }

    private void turnOffHeater() {
        tvHeater.setTextColor(getResources().getColor(R.color.color_off_indicator));
        tvHeater.setTypeface(tvHeater.getTypeface(), Typeface.NORMAL);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}