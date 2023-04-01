/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TempFragment extends Fragment {
    DatabaseActivity databaseActivity=new DatabaseActivity();
    String userId, localuserId, minkey, maxkey, key;
    UserInfo userInfo = new UserInfo();
    DatabaseReference databaseReference;
    String tempVal;

    TextView tvMinimumTemperature;
    TextView tvMaximumTemperature;
    private Handler handler;
    private Runnable handlerTask;
    TextView tvHeater;
    long temperature;
    TextView tvAC;
    TextView tvCurrentTemperature;
    // TextView tvHumidity;
    Button btnMaxTemp;
    Button btnMinTemp;
    FirebaseDatabase database;
    DatabaseReference minTempRef;
    DatabaseReference maxTempRef;
    ArcGauge temperatureView;
    public static int minimumTemperature;
    public static int maximumTemperature;
    ProgressDialog progressDialog;
    String tempstr,userKey,userDetails,userData;

    String serialKeyNumber;
    View view;

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_temp, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userKey=getString(R.string.userKey);
        userData=getApplicationContext().getString(R.string.userData);
        dbID();
        init();
    }

    private void init() {
        tvCurrentTemperature = view.findViewById(R.id.CurrentTemp);
        //  tvHumidity = view.findViewById(R.id.Humidity);
        tvMinimumTemperature = view.findViewById(R.id.MaximumTemperature);
        tvMaximumTemperature = view.findViewById(R.id.MinimumTemperature);
        //tvAC = view.findViewById(R.id.AC);
        //tvHeater = view.findViewById(R.id.heater);
        btnMaxTemp = view.findViewById(R.id.btnMaxTemperature);
        btnMinTemp = view.findViewById(R.id.btnMinTemperature);
        database = FirebaseDatabase.getInstance();
        temperatureView = view.findViewById(R.id.TemperatureView);
        minTempRef = database.getReference(minkey);
        maxTempRef = database.getReference(maxkey);
        StartTimer();





        //setHumidity(20);
        //turnOffHeater();
        //turnOnAc();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        loadTemperatureConfigurations();
        setListeners();

    }

    public void dbID(){
        userInfo.typeAccount();

        localuserId=userInfo.userId;
        userId=userInfo.idInfo;

        if(localuserId!=null){
            key=localuserId;
        }
        if(userId!=null) {
            key= userId;
        }
        minkey=userKey+key+userData+getApplicationContext().getString(R.string.tempmin);
        maxkey=userKey+key+userData+getApplicationContext().getString(R.string.tempmax);

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
                if (minimumTemperature > 0) {
                    showDialogForInputTemperature(false);
                } else {
                    showToast(getApplicationContext().getString(R.string.minimum_temp_first));
                }
            }
        });
    }

    private void showDialogForInputTemperature(boolean isMinTemperature) {
        String title = isMinTemperature ? getApplicationContext().getString(R.string.minTemp) : getApplicationContext().getString(R.string.MaxTemp);
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.temperature_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        TextView tvTitle = promptsView.findViewById(R.id.Title);
        tvTitle.setText(title);
        EditText userInput = promptsView.findViewById(R.id.TemperatureValue);
        if(isMinTemperature) {
            if(minimumTemperature > 0 && minimumTemperature < 33)
                userInput.setText(minimumTemperature+"");
        } else {
            if (maximumTemperature > 0 && maximumTemperature < 33)
                userInput.setText(maximumTemperature+"");
        }
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok_temp, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int temperature = Integer.parseInt(userInput.getText().toString());
                        if (isMinTemperature) {
                            if (maximumTemperature == 0) {
                                if (temperature > 0) {
                                    saveMinTemperature(temperature);
                                } else {
                                    showToast(getApplicationContext().getString(R.string.mini_temp_text));
                                    userInput.requestFocus();
                                }
                            } else {
                                if (temperature > 0 && temperature < maximumTemperature) {
                                    saveMinTemperature(temperature);
                                } else {
                                    showToast(getApplicationContext().getString(R.string.mini_temp_text));
                                    userInput.requestFocus();
                                }
                            }

                        } else {
                            if (temperature > minimumTemperature && temperature > 0) {
                                saveMaxTemperature(temperature);
                            } else {
                                showToast(getApplicationContext().getString(R.string.max_temp_text));
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
        progressDialog.setMessage(getApplicationContext().getString(R.string.savie));
        progressDialog.show();
        minTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closeProgressDialog();
                if (databaseError != null) {
                    showToast(getApplicationContext().getString(R.string.min_temp_min_temp) + databaseError.getMessage());
                } else {
                    minimumTemperature = temp;
                    setMinTemperature(minimumTemperature);
                }
            }
        });
    }

    private void saveMaxTemperature(int temp) {
        progressDialog.setMessage(getApplicationContext().getString(R.string.savings));
        progressDialog.show();
        maxTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closeProgressDialog();
                if (databaseError != null) {
                    showToast(getApplicationContext().getString(R.string.max_temp_text_temp) + databaseError.getMessage());
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
                if (snapshot.getValue()!=null){

                    tempstr = snapshot.getValue().toString();
                    temperature= Long.parseLong(tempstr);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        range1.setTo(18);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#FFC107"));
        range2.setFrom(19);
        range2.setTo(29);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#F44336"));
        range3.setFrom(30);
        range3.setTo(50);

        //add color ranges to gauge
        temperatureView.addRange(range1);
        temperatureView.addRange(range2);
        temperatureView.addRange(range3);

        temperatureView.setMinValue(0);
        temperatureView.setMaxValue(50);
        temperatureView.setValueColor(Color.parseColor("#ffffff"));
        temperatureView.setVisibility(View.VISIBLE);
    }

    private void setCurrentTemperature(int value) {
        tvCurrentTemperature.setText(value+"\u00B0 C");
        temperatureView.setValue(value);
    }


    //private void setHumidity(int value) {
    //  tvHumidity.setText(getApplicationContext().getString(R.string.Humidity)+value+"%");
    //}

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



    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
    void StartTimer(){
        handler = new Handler();
        handlerTask = new Runnable()
        {
            @Override
            public void run() {
                // do something
                databaseActivity.Activity();
                serialKeyNumber=databaseActivity.serialNumber;
                handler.postDelayed(handlerTask, 1000);
                if (serialKeyNumber=="null"){
                    setTemperatureView(temperatureView);

                    setCurrentTemperature(23);
                }
                else{
                    String userKey=getApplicationContext().getString(R.string.userKey);
                    databaseReference=FirebaseDatabase.getInstance().getReference().child(userKey+key+"/Raspberry/"+serialKeyNumber+"/Temperature");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                tempVal=Objects.requireNonNull(snapshot.getValue().toString());
                                setTemperatureView(temperatureView);
                                int tempValue=Integer.parseInt(tempVal);
                                setCurrentTemperature((tempValue));


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        };
        handlerTask.run();
    }

}