/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class LightFragment extends Fragment {
    public int counter;
    TextView timerTV, statusOfLightTV;
    ScrollView scroll;
    LinearLayout linearLayout;
    View view;
    Date date;
    DateFormat dateFormat;
    private Handler handler;
    private Runnable handlerTask;

    //Strings

    String value;
    String LightStatus;
    String on;
    String off;
    String status;
    String light_status;
    String lightState;
    String chanelDes;
    String notificationLT;
    String notificationDesc;
    String lightstatusStr;
    String StatusOut;
    String noVal;
    String strTime;

    //Device vibrate
    VibrationEffect vibrationEffect;
    Vibrator vibrator;

    Boolean cancelTimer;

    ImageButton timerBTN, schedulerBTN;
    int hour, minute;

    public static String statusOfLight,dist,scheduleDate,scheduleTime;

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
        time();
        initString();
        init();
        StartTimer();
        Status();

        //SAMPLE DATA
        addHistory(on);
        addHistory(off);
        addHistory(on);
        addHistory(off);

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
        timerBTN = view.findViewById(R.id.timerButton);
        schedulerBTN = view.findViewById(R.id.schedulerButton);
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
        scroll=view.findViewById(R.id.scheduleLog);
        linearLayout=scroll.findViewById(R.id.linearScroll);

    }

    private void addHistory(String string) {
        TextView textView = new TextView(getActivity());
        textView.setText(getString(R.string.logLightUpdate)+strTime+getString(R.string.logLightStatus)+string);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings("sans-serif");
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //timer and scheduler
        cancelTimer=true;

        cardView.setBackgroundResource(R.drawable.cardview_border);
        timerBTN.setOnClickListener(view1 -> popTimePicker());
        /*schedulerBTN.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(getActivity(), SchedulerActivity.class);
            getActivity().startActivity(myIntent);
        });*/

    }


    void StartTimer(){
        handler = new Handler();
        handlerTask = new Runnable()
        {
            @Override
            public void run() {
                // do something
                LightStatus=statusOfLight;
                if(LightStatus!=null) {
                    if (LightStatus.equals(on)){
                        lightsOn.setChecked(true);
                    }
                    else{
                        statusOfLightTV.setText(status+off);
                        lightsOn.setChecked(false);

                    }

                }
                StatusOut=status+LightStatus;
                statusOfLightTV.setText(StatusOut);
                handler.postDelayed(handlerTask, 1000);
            }
        };
        handlerTask.run();
    }

    private void Status(){
        if (LightStatus!=null) {
            if (LightStatus.equals(on)) {
                lightsOn.setChecked(true);
                lightsOn.setBackgroundResource(R.drawable.status_border_green);
                ivLightOff.setVisibility(View.INVISIBLE);
                ivLightOn.setVisibility(View.VISIBLE);
                lightsOn.setChecked(true);
            }
            else if (LightStatus.equals(off)) {

                lightsOn.setChecked(false);
                lightsOn.setBackgroundResource(R.drawable.status_border_red);
                ivLightOn.setVisibility(View.INVISIBLE);
                ivLightOff.setVisibility(View.VISIBLE);

            }
        }
        else{
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
                statusOfLight=on;
            }
            else {
                LightStatus = off;
                lightsOn.setBackgroundResource(R.drawable.status_border_red);
                ivLightOn.setVisibility(View.INVISIBLE);
                ivLightOff.setVisibility(View.VISIBLE);
                statusOfLight=off;
            }
            addHistory(LightStatus);

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
            String timeOut = getString(R.string.timeSet) + timeout;
            timerTV.setText(timeOut);

            int hourmilli = hour * 600000;
            int minmilli = minute * 60000;
            milli = hourmilli + minmilli;

            new CountDownTimer(milli, second) {
                public void onTick(long millisUntilFinished) {
                    int count=counter;
                    statusOfLight=on;
                    LightStatus=on;
                    Status();
                    counter++;
                }

                public void onFinish() {
                    timerTV.setText(R.string.lightOff);
                    LightStatus=off;
                    Status();
                    statusOfLight=off;
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
    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getString(R.string.format));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strTime = dateFormat.format(date);
        }
    }

}