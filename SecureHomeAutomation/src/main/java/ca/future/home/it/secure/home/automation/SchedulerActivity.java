/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static ca.future.home.it.secure.home.automation.R.string.dayKey;
import static ca.future.home.it.secure.home.automation.R.string.hourmin;
import static ca.future.home.it.secure.home.automation.R.string.mustpickday;
import static ca.future.home.it.secure.home.automation.R.string.off;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SchedulerActivity extends Activity {

    ImageButton back;
    TextView startTV, endTv;
    Button start, end, savebutton;
    int hour, minute, checking,counter,countCheck;
    String endtimeout, Starttimeout, daySelected,
            timeday,check,count,dbDate,dbTime,
                localKey,personalKey,schedKey,key,
                                readTime,readDate,mDate,strTime,schedTime,schedDay;
    Boolean isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    LinearLayout linearLayout;
    ScrollView scroll;
    Date d1,d2;
    LightFragment lightFragment=new LightFragment();

    private int progressStatus = 0;
    private final Handler handler = new Handler();


    String []getday,gettime;

    int time,dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        counter=0;

        init();
        backButton();
        ButtonAction();
    }

    private void backButton() {
        //back button code
        back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            AlertDialog.Builder builder
                    = new androidx.appcompat.app.AlertDialog
                    .Builder(SchedulerActivity.this);

            builder
                    .setMessage(R.string.exitScheduleMsg)

                    .setTitle(R.string.leavingTitle)
                    .setIcon(R.drawable.exit_icon);
            builder.setCancelable(false);

            builder
                    .setPositiveButton(
                            R.string.Yes,
                            (dialog, which) -> startActivity(intent));

            builder
                    .setNegativeButton(
                            R.string.No,
                            (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }

    private void init() {
        start = findViewById(R.id.startTime);
        end = findViewById(R.id.endTime);
        savebutton = findViewById(R.id.saveScheduler);
        back = findViewById(R.id.backbtn);
        startTV = findViewById(R.id.startTimeTV);
        endTv = findViewById(R.id.endtimeTv);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
    }

    private void ButtonAction(){
        start.setOnClickListener(view -> startTime());
        end.setOnClickListener(view -> endTime());
        savebutton.setOnClickListener(view -> {
            checking = 0;
            if (monday.isChecked()) {
                isMonday = true;
                checking = 0;

            } else {
                checking = checking + 1;
                isMonday = false;
            }
            if (tuesday.isChecked()) {
                checking = 0;
                isTuesday = true;
            } else {
                checking = checking + 1;
                isTuesday = false;
            }
            if (wednesday.isChecked()) {
                checking = 0;
                isWednesday = true;
            } else {
                checking = checking + 1;
                isWednesday = false;
            }

            if (thursday.isChecked()) {
                checking = 0;
                isThursday = true;
            } else {
                checking = checking + 1;
                isThursday = false;
            }
            if (friday.isChecked()) {
                checking = 0;
                isFriday = true;
            } else {
                checking = checking + 1;
                isFriday = false;
            }
            if (saturday.isChecked()) {
                checking = 0;
                isSaturday = true;
            } else {
                checking = checking + 1;
                isSaturday = false;
            }
            if (sunday.isChecked()) {
                checking = 0;
                isSunday = true;
            } else {
                checking = checking + 1;
                isSunday = false;
            }
            //checkDays();
            if (checking == 7) {
                Log.d(TAG, getString(R.string.mustpickday));
                Toast.makeText(this, mustpickday, Toast.LENGTH_SHORT).show();
            } else {
                saveSchedule();
            }
        });
    }

    private void saveSchedule() {
        checkDayPicked();
        if ((Starttimeout == null) || endtimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(getString(hourmin));
            try {
                d1 = sdf.parse(Starttimeout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                d2 = sdf.parse(endtimeout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (d2.getTime() > d1.getTime() && checking != 7) {
                schedTime=Starttimeout+getString(R.string.to)+endtimeout;
                schedDay=daySelected;

                check=daySelected;
                counter=counter+1;
                Toast.makeText(this, R.string.savedScheduleToast, Toast.LENGTH_SHORT).show();
                lightFragment.scheduleTime=schedTime;
                lightFragment.scheduleDate=schedDay;

            }
            else {
                //Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void checkDayPicked() {
        daySelected=getString(R.string.empty);
        if (isMonday) {
            daySelected += getString(R.string.monday);
        }
        if (isTuesday) {
            daySelected += getString(R.string.tuesday);
        }
        if (isWednesday) {
            daySelected += getString(R.string.wednesday);
        }
        if (isThursday) {
            daySelected += getString(R.string.thursday);
        }
        if (isFriday) {
            daySelected += getString(R.string.friday);
        }
        if (isSaturday) {
            daySelected += getString(R.string.saturday);
        }
        if (isSunday) {
            daySelected += getString(R.string.sunday);
        }
    }


    private void startTime() {
        hour = 0;
        minute = 0;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            Starttimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + Starttimeout;
            startTV.setText(timeOut);
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle(getString(R.string.startTimeSelect));
        timePickerDialog.show();
    }
    private void endTime() {
        hour = 0;
        minute = 0;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            endtimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + endtimeout;
            endTv.setText(timeOut);
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();
    }

}

