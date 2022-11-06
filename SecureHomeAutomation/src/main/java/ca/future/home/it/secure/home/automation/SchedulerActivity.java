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
    String endtimeout, Starttimeout;
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

            Starttimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));

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

            endtimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + endtimeout;
            Log.d(TAG, timeOut);


        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();

    }

    private void saveTime() throws ParseException {

        String timeSaved = getString(R.string.startTimemsg) + Starttimeout + getString(R.string.endTimemsg) + endtimeout;
        Log.d(TAG, timeSaved);
        if ((Starttimeout == null) || endtimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.hourmin));
            Date d1 = sdf.parse(Starttimeout);

            Date d2 = sdf.parse(endtimeout);

            long elapsed = d2.getTime() - d1.getTime();

            if (d2.getTime() > d1.getTime()) {
                Log.d(TAG, getString(R.string.logDataSaved));
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            } else {
                Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }
        }
    }
}

