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
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Locale;

public class LightFragment extends Fragment {
    UserInfo userInfo=new UserInfo();
    public int counter;
    TextView timerTV, testing, ultrasonicTV;
    Double distance;
    String dist,value,key,localKey,personalKey,lightKey,sensorKey,statusOfLight;
    Boolean LightStatus,cancelTimer;
    TextView ultrasonicET;
    ImageButton timerBTN, schedulerBTN;
    int hour, minute;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button lightsOff,lightsOn;
    Vibrator vibrator;

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
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


        return view;
    }
    private void notificationCaller() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.LightStatus), getString(R.string.Lights), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.channelDesc));
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dbID();
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

    private void dbID(){
        userInfo.typeAccount();

        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
            Log.d(TAG,key);
        }
        if(personalKey!=null) {
            key= personalKey;
            Log.d(TAG, key);
        }


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
        lightKey=key+getString(R.string.key);
        Log.d(TAG,getString(R.string.keyIs)+lightKey);
        sensorKey=key+getString(R.string.db_ultrasonic_dist);
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    statusOfLight = snapshot.getValue().toString();
                    Log.d(TAG,statusOfLight);
                    if(statusOfLight==getString(R.string.on)){
                        notificationCaller();
                        alarmProcess();
                    }
                    else if(statusOfLight==getString(R.string.off)){
                    }
                    else{
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
    public void sendNotificationProcess(String notificationTitle, String notificationText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),getString(R.string.LightStatus));
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mangerCompat = NotificationManagerCompat.from(getContext());
        mangerCompat.notify(1,builder.build());
    }
    public void alarmProcess(){
        String notificationTitle = getString(R.string.notificationLightTitle);
        String notificationText = getString(R.string.notificationLightDesc);
        sendNotificationProcess(notificationTitle,notificationText);
        VibrationEffect vibrationEffect;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.EFFECT_HEAVY_CLICK);
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect);
        }
    }
}