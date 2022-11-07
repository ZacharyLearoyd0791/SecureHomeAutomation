package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static ca.future.home.it.secure.home.automation.R.string.hourmin;
import static ca.future.home.it.secure.home.automation.R.string.mustpickday;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SchedulerActivity extends AppCompatActivity {
    ImageButton back;
    TextView startTV, endTv;
    Button start, end, saveTime;
    int hour, minute, checking;
    String endtimeout, Starttimeout, checkBoxChoices, dayspick, daySelected;
    Boolean isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        start = findViewById(R.id.startTime);
        end = findViewById(R.id.endTime);
        saveTime = findViewById(R.id.saveScheduler);
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

        backButton();
        onButtonClick();

    }


    private void backButton() {

        //to go back to the main screen!
        back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void onButtonClick() {
        start.setOnClickListener(view -> startTime());

        end.setOnClickListener(view -> endTime());

        //save time if start > end
        saveTime.setOnClickListener(view -> {
            checking = 0;
            if (monday.isChecked()) {
                checkBoxChoices += monday.getText().toString() + "\tYES";
                isMonday = true;
                checking = 0;

            } else {
                checkBoxChoices += monday.getText().toString() + "\tNO";
                checking = checking + 1;
                isMonday = false;
            }
            if (tuesday.isChecked()) {
                checkBoxChoices += tuesday.getText().toString() + "\tYES";
                checking = 0;
                isTuesday = true;


            } else {
                checkBoxChoices += tuesday.getText().toString() + "\tNO";
                checking = checking + 1;
                isTuesday = false;


            }
            if (wednesday.isChecked()) {
                checkBoxChoices += wednesday.getText().toString() + "\tYES";
                checking = 0;
                isWednesday = true;

            } else {
                checkBoxChoices += wednesday.getText().toString() + "\tNO";
                checking = checking + 1;
                isWednesday = false;

            }

            if (thursday.isChecked()) {
                checkBoxChoices += thursday.getText().toString() + "\tYES";
                checking = 0;
                isThursday = true;
            } else {
                checkBoxChoices += thursday.getText().toString() + "\tNO";
                checking = checking + 1;
                isThursday = false;

            }
            if (friday.isChecked()) {
                checkBoxChoices += friday.getText().toString() + "\tYES";
                checking = 0;
                isFriday = true;

            } else {
                checkBoxChoices += friday.getText().toString() + "\tNO";
                checking = checking + 1;
                isFriday = false;

            }
            if (saturday.isChecked()) {
                checkBoxChoices += saturday.getText().toString() + "\tYES";
                checking = 0;
                isSaturday = true;

            } else {
                checkBoxChoices += saturday.getText().toString() + "\tNO";
                checking = checking + 1;
                isSaturday = false;
            }
            if (sunday.isChecked()) {
                checkBoxChoices += sunday.getText().toString() + "\tYES";
                checking = 0;
                isSunday = true;
            } else {
                checkBoxChoices += sunday.getText().toString() + "\tNO";
                checking = checking + 1;
                isSunday = false;
            }
            checkDays();
            dayspick = getString(R.string.pickedDays) + checkBoxChoices;
            String num = getString(R.string.checkboxNum) + checking;
            Log.d(TAG, num);
            if (checking == 7) {
                Log.d(TAG, getString(R.string.mustpickday));
                Toast.makeText(this, mustpickday, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, dayspick);
                try {
                    saveTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startTime() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;

            Starttimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));

            String timeOut = getString(R.string.timeSet) + Starttimeout;
            Log.d(TAG, timeOut);
            startTV.setText(timeOut);

        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle(getString(R.string.startTimeSelect));
        timePickerDialog.show();

    }

    private void checkDays() {

        if (isMonday) {
            daySelected = "Monday ";
        }
        if (isTuesday) {
            daySelected += "Tuesday ";
        }
        if (isWednesday) {
            daySelected += "Wednesday ";

        }
        if (isThursday) {
            daySelected += "Thursday ";

        }
        if (isFriday) {
            daySelected += "Friday ";

        }
        if (isSaturday) {
            daySelected += "Saturday ";

        }
        if (isSunday) {
            daySelected += "Sunday ";

        }
    }

    private void endTime() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;

            endtimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + endtimeout;
            Log.d(TAG, timeOut);
            endTv.setText(timeOut);


        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();

    }

    private void saveTime() throws ParseException {

        String timeSaved = getString(R.string.startTimemsg) + Starttimeout + getString(R.string.endTimemsg) + endtimeout;
        Log.d(TAG, timeSaved);
        Log.d(TAG, dayspick);

        if ((Starttimeout == null) || endtimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(hourmin));
            Date d1 = sdf.parse(Starttimeout);

            Date d2 = sdf.parse(endtimeout);

            assert d1 != null;
            assert d2 != null;

            if (d2.getTime() > d1.getTime() || checking != 7) {
                Log.d(TAG, getString(R.string.logDataSaved));
                Log.d(TAG, daySelected);
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                String timeday = "Day picked: " + daySelected + "\nTime selected :" + Starttimeout + " to " + endtimeout;
                Log.d(TAG, timeday);


            } else {
                Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }
        }
    }
}

