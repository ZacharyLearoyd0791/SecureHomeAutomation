package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SchedulerActivity extends AppCompatActivity {
    ImageButton back;
    Button start, end, save;
    int hour, minute;
    String endtimeout, Starttimeout, nullVal = "00:00";
    private Date mStartTime, mEndTime;
    ;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private FragmentTransaction currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        start = findViewById(R.id.startTime);
        end = findViewById(R.id.endTime);
        save = findViewById(R.id.saveScheduler);
        back = findViewById(R.id.backbtn);

        //to go back to the main screen!
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime();
            }
        });

        //save time if start > end
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startTime() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            int second = 1000;
            hour = selectedHour;
            minute = selectedMinute;

            Starttimeout = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

            String timeOut = getString(R.string.timeSet) + Starttimeout;
            Log.d(TAG, timeOut);

        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle(getString(R.string.startTimeSelect));
        timePickerDialog.show();

    }

    public void endTime() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            int second = 1000;
            hour = selectedHour;
            minute = selectedMinute;

            endtimeout = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            String timeOut = getString(R.string.timeSet) + endtimeout;
            Log.d(TAG, timeOut);


        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();

    }

    private void saveTime() throws ParseException {

        String timeSaved = "Start time saved: " + Starttimeout + " End time saved :" + endtimeout;
        Log.d(TAG, timeSaved);
        if ((Starttimeout != null) || endtimeout != null) {


            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date d1 = sdf.parse(null);
            try {
                d1 = sdf.parse(Starttimeout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 = sdf.parse(null);
            try {
                d2 = sdf.parse(endtimeout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (d1 != null || d2 != null) {
                long elapsed = d2.getTime() - d1.getTime();

                if (d2.getTime() > d1.getTime()) {
                    Log.d(TAG, "end is more than start");
                } else if (d2.getTime() < d1.getTime()) {
                    Log.d(TAG, "end is less than start");
                } else {
                    Log.d(TAG, "end is equal than start");
                }
            } else {
                Log.d(TAG, "NULL d1 and d2, error might occur");

            }
        } else {
            Toast.makeText(this, "Null Values in Start time or End Time", Toast.LENGTH_SHORT).show();
        }


    }
}
