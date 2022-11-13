/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.ekn.gruzer.gaugelibrary.contract.ValueFormatter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class TempFragment extends Fragment {

    TextView tvMinimumTemperature;
    TextView tvMaximumTemperature;
    TextView tvCurrentTemperature;
    FirebaseDatabase database;
    DatabaseReference minTempRef;
    DatabaseReference maxTempRef;
    ArcGauge temperatureView;

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
        init(view);
        setListeners();
    }

    private void init(View view) {
        tvCurrentTemperature = view.findViewById(R.id.CurrentTemp);
        tvMinimumTemperature = view.findViewById(R.id.MaximumTemperature);
        tvMaximumTemperature = view.findViewById(R.id.MinimumTemperature);
        database = FirebaseDatabase.getInstance();
        temperatureView = view.findViewById(R.id.TemperatureView);
        minTempRef = database.getReference("Temperature-Configurations/minTemperature");
        maxTempRef = database.getReference("Temperature-Configurations/maxTemperature");
        setTemperatureView(temperatureView);
        setCurrentTemperature(18);
    }

    private void setListeners() {
        tvMaximumTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForInputTemperature(true);
            }
        });

        tvMaximumTemperature.setOnClickListener(new View.OnClickListener() {
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
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int temperature = Integer.parseInt(userInput.getText().toString());
                        if (isMinTemperature) {
                            saveMinTemperature(temperature);
                        } else {
                            saveMaxTemperature(temperature);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void saveMinTemperature(int temp) {
        minTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Minimum Temperature could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Minimum Temperature saved successfully.");
                }
            }
        });
    }

    private void saveMaxTemperature(int temp) {
        maxTempRef.setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Maximum Temperature could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Maximum Temperature saved successfully.");
                }
            }
        });
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
    }

    private void setCurrentTemperature(int value) {
        tvCurrentTemperature.setText(value+"\u00B0 C");
    }
}