/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Locale;

public class LightFragment extends Fragment {

    public int counter;
    TextView timerTV, testing, ultrasonicTV;
    Double distance;
    String dist,value;
    Boolean LightStatus,cancelTimer;
    TextView ultrasonicET;
    ImageButton timerBTN, schedulerBTN;
    int hour, minute;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button lightsOff,lightsOn;

    public LightFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_light, container, false);
        timerTV = view.findViewById(R.id.timerTV);
        ultrasonicTV = view.findViewById(R.id.distanceOut);
        timerBTN = view.findViewById(R.id.timerButton);
        schedulerBTN = view.findViewById(R.id.schedulerButton);
        ultrasonicET = view.findViewById(R.id.Ultrasonic);
        testing = view.findViewById(R.id.testing);
        lightsOff=view.findViewById(R.id.offLights);
        lightsOn=view.findViewById(R.id.onLights);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.db_ultrasonic_dist));
        //timer and scheduler
        cancelTimer=true;

        timerBTN.setOnClickListener(v -> popTimePicker());
        schedulerBTN.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(getActivity(), SchedulerActivity.class);
            getActivity().startActivity(myIntent);
        });
        lightsOff.setOnClickListener(view13 -> {
            LightStatus = false;
            lightHandler();
        });
        lightsOn.setOnClickListener(view12 -> {
            LightStatus = true;
            lightHandler();});

        SensorDB();


    }
    private void lightHandler() {

        if (LightStatus) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));
            databaseReference.setValue(R.string.on);
            cancelTimer=true;
        }
        else{
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));
            databaseReference.setValue(R.string.off);
            cancelTimer=false;

        }
    }

    private void SensorDB(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.db_ultrasonic_dist));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    dist=snapshot.getValue().toString();
                    value=getString(R.string.distance_from_ultra)+dist+getString(R.string.cm);
                    Log.d(TAG,value);
                    ultrasonicTV.setText(dist);
                    try
                    {
                        Double.parseDouble(dist);
                        distance=Double.parseDouble(dist);
                        if (distance<20){
                            String value=getString(R.string.movement_detect);
                            Log.d(TAG,value);
                            ultrasonicTV.setText(value);
                            LightStatus=true;
                        }
                        else{
                            String value=getString(R.string.no_movement_detect)+distance+getString(R.string.cm);
                            Log.d(TAG,value);
                            LightStatus=false;
                        }
                        if (LightStatus) {
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));
                            databaseReference.setValue(R.string.on);
                        }
                        else{
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));
                            databaseReference.setValue(R.string.off);
                        }
                    }

                    catch(NumberFormatException e)
                    {
                        Log.d(TAG,getString(R.string.log_value_not_double));
                        ultrasonicTV.setText(dist);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void popTimePicker() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            counter = 0;
            int milli = 0;
            int second = 1000;
            hour = selectedHour;
            minute = selectedMinute;

            String timeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            Log.d(TAG, timeout);
            String timeOut = getString(R.string.timeSet) + timeout;
            timerTV.setText(timeOut);

            int hourmilli = hour * 600000;
            int minmilli = minute * 60000;
            milli = hourmilli + minmilli;

            new CountDownTimer(milli, second) {
                public void onTick(long millisUntilFinished) {
                    String count=getString(R.string.counter_on)+counter;
                    Log.d(TAG, String.valueOf(count));
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));

                    databaseReference.setValue(getString(R.string.on));
                    counter++;
                }

                public void onFinish() {
                    timerTV.setText(R.string.lightOff);
                    //Log.d(TAG, getString(R.string.lightOff));
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.key));
                    databaseReference.setValue(getString(R.string.off));
                    counter = 0;
                    hour = 0;
                    minute = 0;

                }
            }.start();
        };
        counter = 0;

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle(getString(R.string.Timer));
        timePickerDialog.show();

    }

/*
    public void schedulerLight() {

        Intent myIntent = new Intent(getActivity(), SchedulerActivity.class);
        getActivity().startActivity(myIntent);

    }
*/

}