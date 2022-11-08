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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SchedulerActivity extends Activity {

    ImageButton back;
    TextView startTV, endTv;
    Button start, end, saveTime;
    int hour, minute, checking,i,counter;
    String endtimeout, Starttimeout, daySelected, timeday,check,count;
    String []arr;
    Boolean isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    LinearLayout linearLayout;
    ScrollView scroll;

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
        daySelected = "";
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

            endtimeout = (String.format(Locale.getDefault(), getString(R.string.timeFormat), hour, minute));
            String timeOut = getString(R.string.timeSet) + endtimeout;
            endTv.setText(timeOut);


        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle(getString(R.string.endTimeSelect));
        timePickerDialog.show();

    }

    private void saveTime() throws ParseException {

        String timeSaved = getString(R.string.startTimemsg) + Starttimeout + getString(R.string.endTimemsg) + endtimeout;
        Log.d(TAG, timeSaved);

        if ((Starttimeout == null) || endtimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(hourmin));
            Date d1 = sdf.parse(Starttimeout);

            Date d2 = sdf.parse(endtimeout);

            assert d1 != null;
            assert d2 != null;

            if (counter ==0){
                Log.d(TAG,"first check");
            }
            else{
                count= "Check: "+ counter;
                Log.d(TAG,count);

                for (i=0;i<counter;i++){
                    arr= new String[100];
                    arr[counter-1]=check;
                    String temp="Checking array"+ arr[counter-1];
                    Log.d(TAG, temp);
                    Log.d(TAG,daySelected);

                    Log.d(TAG,"------------------------------------------------------------------");
                    Log.d(TAG,daySelected);
                    Log.d(TAG,arr[counter-1]);
                }
                Log.d(TAG,daySelected);
                Log.d(TAG,arr[counter-1]);
                if (arr[counter-1]==daySelected){
                    Log.d(TAG,"Same string");
                       /* Log.d(TAG,daySelected);
                        Log.d(TAG,arr[counter-1]);*/
                }
                else{
                    Log.d(TAG,"Not Same string");

                    Log.d(TAG,"------------------------------------------------------------------");
                    Log.d(TAG,daySelected);
                    Log.d(TAG,arr[counter-1]);
                }
            }

            if (d2.getTime() > d1.getTime() && checking != 7) {
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                timeday = getString(R.string.dayspickedMsg) + daySelected + getString(R.string.timeSelectedmsg) + Starttimeout + getString(R.string.to) + endtimeout;
                Log.d(TAG, timeday);
                logging();
                check=daySelected;
                counter=counter+1;


            } else {
                Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void logging(){

        scroll=findViewById(R.id.scheduleLog);

        linearLayout=scroll.findViewById(R.id.liner_schedule);

        addHistory(timeday);

    }

    private void addHistory(String string) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(string);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings("sans-serif");
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);
    }
}

