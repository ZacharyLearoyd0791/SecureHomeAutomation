package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseActivity extends Fragment {
    UserInfo userInfo=new UserInfo();
    LightFragment lightFragment=new LightFragment();
    DoorFragment doorFragment=new DoorFragment();


    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    static Context context;

    //Date
    Date date;
    DateFormat dateFormat;

    //Key string
    String finalDoorKey,localKey,key,personalKey,strDate,
            finalSensorKey, finalStatusKey,statusKey,SensorKey,doorKey,maxKey,minKey,finalMaxKey,finalMinKey,
            finalWindowBreak,windowBKey,finaldateKey,finalTimeKey,scheduleKey;

    //Database String
    String DBDoor,DBLight,DBDist,DBWindow, DBMax,DBMin,DBScheduleDay,DBScheduleTime;
    String outDoor,outLight,outDist,outWindow,outMax,outMin,outScheduleDate,outScheduleTime;

    int min,max;
    private Handler handler;
    private Runnable handlerTask;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Activity();
    }

    void StartTimer(){
        handler = new Handler();
        handlerTask = new Runnable()
        {
            @Override
            public void run() {
                // do something
                sendDataStrings();
                handler.postDelayed(handlerTask, 1000);
            }
        };
        handlerTask.run();
    }

    public void Activity(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.d(TAG, "DatabaseActivity Active!");
        time();
        initString();
        dbID();
        getDB();
        StartTimer();
    }

    private void initString() {

        doorKey=getApplicationContext().getString(R.string.forwardslash)+getApplicationContext().getString(R.string.door_status);
        statusKey=getApplicationContext().getString(R.string.statusKey);
        SensorKey=getApplicationContext().getString(R.string.db_ultrasonic_dist);
        maxKey=getApplicationContext().getString(R.string.tempmax);
        minKey=getApplicationContext().getString(R.string.tempmin);
        windowBKey=getApplicationContext().getString(R.string.windowBreakKey);
        scheduleKey= getApplicationContext().getString(R.string.schedKey);
    }

    public void dbID(){
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

        finalDoorKey =key+doorKey;

        //Light related user key:
        finalStatusKey =key+statusKey;
        finalSensorKey =key+SensorKey;
        finaldateKey=key+scheduleKey+getApplicationContext().getString(R.string.dayKey);
        finalTimeKey=key+scheduleKey+getApplicationContext().getString(R.string.timeKey);

        //Temp related user key:
        finalMaxKey=key+maxKey;
        finalMinKey=key+minKey;
        //Window related user key:
        finalWindowBreak=key+windowBKey;
    }

    private void time(){
        Log.d(TAG,"STR TIME METHOD");
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getApplicationContext().getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
        System.out.println("Converted String: " + strDate);
        Log.d(TAG,"STR Time: "+strDate);
    }

    public void getDB(){
        //door
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBDoor= Objects.requireNonNull(snapshot.getValue()).toString();
                    Log.d(TAG,"Test Door DB"+DBDoor);
                    DoorDBAction();
                }
                else {
                    Log.d(TAG, "Door Status No Current Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Temp
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalMaxKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBMax= Objects.requireNonNull(snapshot.getValue().toString());

                    databaseReference = FirebaseDatabase.getInstance().getReference().child((finalMinKey));
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                DBMin= Objects.requireNonNull(snapshot.getValue().toString());
                                TemperatureDBAction();
                            }
                            else {
                                Log.d(TAG, "Temperature  No Current Value");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else {
                    Log.d(TAG, "Door Status No Current Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Light
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalStatusKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBLight= Objects.requireNonNull(snapshot.getValue()).toString();
                    Log.d(TAG,"DATABASE ACTIVITY SAYS THAT THE LIGHT IS:"+DBLight);
                    LightStatusDBAction();
                }
                else {
                    Log.d(TAG, "Light Status No Current Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalSensorKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBDist= Objects.requireNonNull(snapshot.getValue()).toString();//on or off
                    DistDBAction();
                }
                else {
                    Log.d(TAG, "Distance Status No Current Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Schedule
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finaldateKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBScheduleDay= Objects.requireNonNull(snapshot.getValue()).toString();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child((finalTimeKey));
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                              DBScheduleTime=Objects.requireNonNull(snapshot.getValue()).toString();
                              ScheduleDBAction();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Window
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalWindowBreak));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBWindow= Objects.requireNonNull(snapshot.getValue()).toString();
                    WindowDBAction();
                }
                else {
                    Log.d(TAG, "Window Status No Current Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ScheduleDBAction() {

    }

    //Window Action
    private void WindowDBAction() {
        Log.d(TAG,"Status of Window is: "+DBWindow);
    }

    //Light Action
    private void LightStatusDBAction() {
        lightFragment.statusOfLight=(DBLight);
        Log.d(TAG,"2002 light fragment status led is :\t"+lightFragment.statusOfLight);

    }
    private void DistDBAction() {
        lightFragment.dist=(DBDist);
        Log.d(TAG,"Current Distance on sensor is: "+lightFragment.dist);
    }

    //Temperature action
    private void TemperatureDBAction() {
        try{
            max = Integer.parseInt(DBMax);
            Log.d(TAG,"Max value:\t"+max);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        try{
            min = Integer.parseInt(DBMin);
            Log.d(TAG,"Min value:\t"+min);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    //Door action
    private void DoorDBAction() {
        doorFragment.statusofDoor=(DBDoor);
        Log.d(TAG,"2002 door fragment status led is :\t"+doorFragment.statusofDoor);

    }

    private void sendDataStrings(){

        outLight=lightFragment.statusOfLight;
        outScheduleDate=lightFragment.scheduleDate;
        outDoor=doorFragment.statusofDoor;


        if (outLight!=null) {
            Log.d(TAG, "Testing data for sending data to db " + outLight);
            toDatabase();
        }
        if (outScheduleDate!=null){
            Log.d(TAG, "Testing data for sending data to db " + outScheduleDate);
            toDatabase();
        }
        if (outDoor!=null){
            Log.d(TAG, "Testing data for sending data to db " + outDoor);
            toDatabase();
        }

    }
    //FOR DOOR LOCK
    public void toDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseActivity.context = getApplicationContext();

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.setValue(outDoor);

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalStatusKey));
        databaseReference.setValue(outLight);
    }
}