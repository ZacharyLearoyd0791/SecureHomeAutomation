/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static ca.future.home.it.secure.home.automation.R.string.hourmin;
import static ca.future.home.it.secure.home.automation.R.string.mustpickday;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SchedulerActivity extends Activity {

    ImageButton back;
    TextView startTV, endTv;
    Button start, end, saveTime;
    int hour, minute, checking,counter;
    String endTimeout, startTimeout, daySelected, timeDay,check,count,dbDate,dbTime;
    Boolean isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    LinearLayout linearLayout;
    ScrollView scroll;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
            checkDays();
            String num = getString(R.string.checkboxNum) + checking;
            Log.d(TAG, num);
            if (checking == 7) {
                Log.d(TAG, getString(R.string.mustpickday));
                Toast.makeText(this, mustpickday, Toast.LENGTH_SHORT).show();
            } else {
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

            startTimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));

            String timeOut = getString(R.string.timeSet) + startTimeout;
            Log.d(TAG, timeOut);
            startTV.setText(timeOut);

        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle(getString(R.string.startTimeSelect));
        timePickerDialog.show();

    }

    private void checkDays() {
        daySelected = getString(R.string.empty);
        if (isMonday) {
            daySelected = getString(R.string.monday);
        }
        if (isTuesday) {
            daySelected = getString(R.string.tuesday);
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

    private void endTime() {
        hour = 0;
        minute = 0;


        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;

            endTimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + endTimeout;
            endTv.setText(timeOut);


        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();

    }

    private void saveTime() throws ParseException {

        String timeSaved = getString(R.string.startTimemsg) + startTimeout + getString(R.string.endTimemsg) + endTimeout;
        Log.d(TAG, timeSaved);

        if ((startTimeout == null) || endTimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(hourmin));
            Date d1 = sdf.parse(startTimeout);

            Date d2 = sdf.parse(endTimeout);

            assert d1 != null;
            assert d2 != null;

            if (counter ==0){
                Log.d(TAG,getString(R.string.FirstCheck));
            }
            else{
                count= getString(R.string.Check)+ counter;
                Log.d(TAG,count);
            }

            if (d2.getTime() > d1.getTime() && checking != 7) {
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                timeDay = getString(R.string.dayspickedMsg) + daySelected + getString(R.string.timeSelectedmsg) + startTimeout + getString(R.string.to) + endTimeout;
                Log.d(TAG, timeDay);
                logging();
                check=daySelected;
                counter=counter+1;


            } else {
                Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }
            toDatabase();

        }
    }
    private void logging(){

        scroll=findViewById(R.id.scheduleLog);

        linearLayout=scroll.findViewById(R.id.liner_schedule);

        addHistory(timeDay);

    }

    private void addHistory(String string) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(string);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings(getString(R.string.font_sans_serif));
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);
    }

    private void toDatabase(){
        String timeMsg= startTimeout +getString(R.string.to)+ endTimeout;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((getString(R.string.dayKey)));

        dbDate = daySelected;
        databaseReference.setValue(dbDate);
        databaseReference.setValue(dbTime);
        Map<String, Object> day = new HashMap<>();
        for (int i =0; i<counter;i++){
            String dateC=getString(R.string.Day)+i;
            day.put(dateC, daySelected);
        }
        databaseReference.updateChildren(day);

        dbTime= startTimeout +getString(R.string.to)+ endTimeout;
        databaseReference = FirebaseDatabase.getInstance().getReference().child((getString(R.string.timeKey)));
        databaseReference.setValue(dbTime);
        Map<String, Object> time = new HashMap<>();
        for (int i =0; i<counter;i++){
            String dateC=getString(R.string.Time)+i;
            time.put(dateC, timeMsg);
        }
        databaseReference.updateChildren(time);
    }
}

