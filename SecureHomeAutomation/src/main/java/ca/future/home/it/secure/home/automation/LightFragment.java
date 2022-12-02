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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class LightFragment extends Fragment {

    Timer timer = new Timer();
    UserInfo userInfo=new UserInfo();
    public int counter;
    TextView timerTV, testing, ultrasonicTV,statusOfLightTV;
    Double distance;

    View view;

    private Handler handler;
    private Runnable handlerTask;

    //Strings

    String value,LightStatus,on,off,status,
                status_light,statusOn,
                    statusOff,light_status,lightState,chanelDes,
                        notificationLT,notificationDesc,lightstatusStr, StatusOut,noVal;

    //Device vibrate
    VibrationEffect vibrationEffect;
    Vibrator vibrator;

    Boolean cancelTimer;
    TextView ultrasonicET;

    //Scheduler
    ImageButton timerBTN, schedulerBTN;
    int hour, minute;

    public static String statusOfLight,dist,dataOut;

    //Notifications
    NotificationChannel channel;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    NotificationManagerCompat mangerCompat;

    //Light toggle button
    ImageView ivLightOn, ivLightOff;
    ToggleButton lightsOn;

    CardView cardView;

    public LightFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        initString();
        init();
        StartTimer();
        Status();
        return view;
    }

    private void initString() {

        on=getString(R.string.on);
        off=getString(R.string.off);
        status=getString(R.string.lightStatusMSG)+getString(R.string.empty);
        light_status=getString(R.string.LightStatus);
        lightState=getString(R.string.Lights);
        chanelDes=getString(R.string.channelDesc);
        value=getString(R.string.distance_from_ultra);
        noVal=getString(R.string.NoVal);
    }

    private void init() {

        timerTV = view.findViewById(R.id.timerTV);
        ultrasonicTV = view.findViewById(R.id.distanceOut);
        timerBTN = view.findViewById(R.id.timerButton);
        schedulerBTN = view.findViewById(R.id.schedulerButton);
        ultrasonicET = view.findViewById(R.id.Ultrasonic);
        testing = view.findViewById(R.id.testing);
        ivLightOff=view.findViewById(R.id.light_off);
        ivLightOn=view.findViewById(R.id.light_on);
        statusOfLightTV=view.findViewById(R.id.statusOfLight);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        notificationManager = getContext().getSystemService(NotificationManager.class);
        notificationLT=getString(R.string.notificationLightTitle);
        notificationDesc=getString(R.string.notificationLightDesc);
        lightstatusStr=getString(R.string.LightStatus);
        mangerCompat= NotificationManagerCompat.from(getContext());
        lightsOn=view.findViewById(R.id.onLights);
        cardView=view.findViewById(R.id.cv_scheduler);

        //Light off by default
        ivLightOn.setVisibility(View.INVISIBLE);
        ivLightOff.setVisibility(View.VISIBLE);
        lightsOn.setBackgroundResource(R.drawable.status_border_red);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //timer and scheduler
        cancelTimer=true;

        cardView.setBackgroundResource(R.drawable.cardview_border);
        timerBTN.setOnClickListener(view1 -> popTimePicker());
        schedulerBTN.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(getActivity(), SchedulerActivity.class);
            getActivity().startActivity(myIntent);
        });
    }


    void StartTimer(){
        handler = new Handler();
        handlerTask = new Runnable()
        {
            @Override
            public void run() {
                // do something
                Log.d(TAG,"Light status is: "+statusOfLight);
                LightStatus=statusOfLight;
                StatusOut=status+LightStatus;
                statusOfLightTV.setText(StatusOut);

                handler.postDelayed(handlerTask, 1000);
            }
        };
        handlerTask.run();
    }

    private String sendData(String Status) {
        dataOut=Status;
        return dataOut;
    }

    private void Status(){
        Log.d(TAG,"Confirm Status method is working!!!"+LightStatus);

        if(Objects.equals(LightStatus, "On")){
            Log.d(TAG,"Confirm it is on!!!\t"+LightStatus);
            lightsOn.setChecked(true);
            LightStatus = on;
            lightsOn.setBackgroundResource(R.drawable.status_border_green);
            ivLightOff.setVisibility(View.INVISIBLE);
            ivLightOn.setVisibility(View.VISIBLE);
        }
        else if(Objects.equals(LightStatus, off)){
            Log.d(TAG,"Confirm it is off!!!\t"+LightStatus);
            lightsOn.setChecked(false);
            lightsOn.setBackgroundResource(R.drawable.status_border_red);
            ivLightOn.setVisibility(View.INVISIBLE);
            ivLightOff.setVisibility(View.VISIBLE);

        }
        lightsOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (lightsOn.isChecked()) {
                LightStatus = on;
                lightsOn.setBackgroundResource(R.drawable.status_border_green);
                ivLightOff.setVisibility(View.INVISIBLE);
                ivLightOn.setVisibility(View.VISIBLE);
                sendData(on);

            }
            else {
                LightStatus = off;
                lightsOn.setBackgroundResource(R.drawable.status_border_red);
                ivLightOn.setVisibility(View.INVISIBLE);
                ivLightOff.setVisibility(View.VISIBLE);
                sendData(off);
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
                    int count=counter;
                    Log.d(TAG, String.valueOf(count));
                    sendData(on);
                    LightStatus=on;
                    Status();
                    counter++;
                }

                public void onFinish() {
                    timerTV.setText(R.string.lightOff);
                    LightStatus=off;
                    Status();
                    sendData(off);
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

}