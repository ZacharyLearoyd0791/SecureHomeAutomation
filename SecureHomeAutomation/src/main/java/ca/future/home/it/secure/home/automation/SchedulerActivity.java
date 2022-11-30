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

