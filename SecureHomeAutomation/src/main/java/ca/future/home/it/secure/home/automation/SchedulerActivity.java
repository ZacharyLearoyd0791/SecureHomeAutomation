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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SchedulerActivity extends Activity {

    UserInfo userInfo= new UserInfo();
    ProgressBar progressBar;
    ImageButton back;
    TextView startTV, endTv;
    Button start, end, saveTime;
    int hour, minute, checking,counter,countCheck;
    String endtimeout, Starttimeout, daySelected,
            timeday,check,count,dbDate,dbTime,
                localKey,personalKey,schedKey,key,
                                readTime,readDate,mDate,strTime,strDate;
    Boolean isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    LinearLayout linearLayout;
    ScrollView scroll;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private int progressStatus = 0;
    private final Handler handler = new Handler();


    String []getday,gettime;

    int time,dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        counter=0;
        firebaseDatabase = FirebaseDatabase.getInstance();

        dbID();
        init();

        backButton();
        onButtonClick();
        readDB();
    }

    private void init() {
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
        progressBar=findViewById(R.id.delayProgress);
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void dbID() {
        //Get user ID from Database
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
        else{
            key=getString(R.string.noID);
        }
        schedKey=key+getString(R.string.schedKey);
        Log.d(TAG,getString(R.string.keyIs)+schedKey);
    }

    private void readDB() {
        //reading data from database
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schedKey+(getString(R.string.dayKey)));
        databaseReference=firebaseDatabase.getReference().child(schedKey+(getString(R.string.dayKey)));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null){

                    readDate=snapshot.getValue().toString();
                    readDate=readDate.replace("{","");
                    readDate=readDate.replace("}","");
                    Log.d(TAG,readDate);
                    getday=readDate.split(",");

                    databaseReference=firebaseDatabase.getReference().child(schedKey+(getString(R.string.timeKey)));
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot != null) {
                                readTime = snapshot.getValue().toString();
                                Log.d(TAG,"Data from readDB is:\nDate:"+readDate+"\nTime:"+readTime);
                                readTime=readTime.replace("{","");
                                readTime=readTime.replace("}","");
                                Log.d(TAG,readTime);
                                gettime=readTime.split(",");

                                for(int i=0;i<gettime.length;i++){

                                    timeday=getday[i]+"\n"+gettime[i];
                                    logging(timeday);
                                    counter=counter+1;
                                }
                                Log.d(TAG,"Counter: "+counter);
                                countCheck=counter;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else{
                    Toast.makeText(SchedulerActivity.this, "Has no Recent Data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void logging(String timeday) {

        scroll=findViewById(R.id.scheduleLog);

        linearLayout=scroll.findViewById(R.id.liner_schedule);

        addHistory(timeday);
    }

    private void addHistory(String timeday) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(timeday);
        textView.setBackgroundResource(R.drawable.scroll_view_item_border);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setTextColor(0xFF000000);
        textView.setTextSize(14);
        textView.setFontFeatureSettings(getString(R.string.font_sans_serif));
        textView.setPadding(10,19,10,19);
        linearLayout.addView(textView);

    }

    private void onButtonClick() {
        //On click actions when using buttons
        start.setOnClickListener(view -> startTime());

        end.setOnClickListener(view -> endTime());

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
            if (checking == 7) {
                Log.d(TAG, getString(R.string.mustpickday));
                Toast.makeText(this, mustpickday, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    saveSchedule();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkDays() {
        daySelected = getString(R.string.empty);


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

    private void saveSchedule() throws ParseException {
        //Checking day picked
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

        if ((Starttimeout == null) || endtimeout == null) {
            Toast.makeText(this, R.string.startorendnull, Toast.LENGTH_SHORT).show();

        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(hourmin));
            Date d1 = sdf.parse(Starttimeout);
            Date d2 = sdf.parse(endtimeout);
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
                timeday=daySelected+"  "+Starttimeout+" "+endtimeout;
                logging(timeday);
                check=daySelected;
                counter=counter+1;
                Log.d(TAG,"Info "+timeday);
                toDatabase();
            }
            else {
                Log.d(TAG, getString(R.string.logDataEndSmall));
                Toast.makeText(this, R.string.endSmall, Toast.LENGTH_SHORT).show();

            }

        }
        unCheck();
    }

    private void unCheck() {
        monday.setChecked(false);
        tuesday.setChecked(false);
        wednesday.setChecked(false);
        thursday.setChecked(false);
        friday.setChecked(false);
        saturday.setChecked(false);
        sunday.setChecked(false);
    }

    private void toDatabase() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            while (progressStatus < 10) {
                progressStatus += 1;
                // Update the progress bar and display the
                //current value in the text view
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    if (progressStatus == 10) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                try {
                    // Sleep for 200 milliseconds.
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Toast.makeText(this, R.string.savedScheduleToast, Toast.LENGTH_SHORT).show();
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
}

