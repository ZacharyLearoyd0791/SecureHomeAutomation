package ca.future.home.it.secure.home.automation;


import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
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
            finalWindowBreak,windowBKey,finaldateKey,finalTimeKey,scheduleKey,userKey;

    //Database String
    String DBDoor,DBLight,DBDist,DBWindow, DBMax,DBMin,DBScheduleDay,DBScheduleTime,name,email;
    String outDoor,outLight,userData,outWindow,outMax,outMin,outScheduleDate,userDetails;

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
        userKey=getApplicationContext().getString(R.string.userKey);
        userDetails=getApplicationContext().getString(R.string.userDetails);
        userData=getApplicationContext().getString(R.string.userData);
        name=getApplicationContext().getString(R.string.name);
        email=getApplicationContext().getString(R.string.email);
    }

    public void dbID(){
        userInfo.typeAccount();
        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey+key+userDetails+name));
            databaseReference.setValue(userInfo.name);
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey+key+userDetails+email));
            databaseReference.setValue(userInfo.email);
        }
        if(personalKey!=null) {
            key= personalKey;
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey+key+userDetails+name));
            databaseReference.setValue(userInfo.personName);
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey+key+userDetails+email));
            databaseReference.setValue(userInfo.personEmail);
        }

        finalDoorKey =userKey+key+userData+doorKey;

        //Light related user key:
        finalStatusKey =userKey+key+userData+statusKey;
        finalSensorKey =userKey+key+userData+SensorKey;
        finaldateKey=userKey+key+userData+scheduleKey+getApplicationContext().getString(R.string.dayKey);
        finalTimeKey=userKey+key+userData+scheduleKey+getApplicationContext().getString(R.string.timeKey);

        //Temp related user key:
        finalMaxKey=userKey+key+userData+maxKey;
        finalMinKey=userKey+key+userData+minKey;
        //Window related user key:
        finalWindowBreak=userKey+key+userData+windowBKey;
    }

    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getApplicationContext().getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
    }

    public void getDB(){
        //door
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBDoor= Objects.requireNonNull(snapshot.getValue()).toString();
                    DoorDBAction();
                }
                else {
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

        //Light
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalStatusKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBLight= Objects.requireNonNull(snapshot.getValue()).toString();
                    LightStatusDBAction();
                }
                else {
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
                                DBScheduleTime = Objects.requireNonNull(snapshot.getValue()).toString();
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
    }

    //Light Action
    private void LightStatusDBAction() {
        lightFragment.statusOfLight=(DBLight);

    }
    private void DistDBAction() {
        lightFragment.dist=(DBDist);
    }

    //Temperature action
    private void TemperatureDBAction() {
        try{
            max = Integer.parseInt(DBMax);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        try{
            min = Integer.parseInt(DBMin);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    //Door action
    private void DoorDBAction() {
        doorFragment.statusofDoor=(DBDoor);

    }

    private void sendDataStrings(){

        outLight=lightFragment.statusOfLight;
        outScheduleDate=lightFragment.scheduleDate;
        outDoor=doorFragment.statusofDoor;


        if (outLight!=null) {
            toDatabase();
        }
        if (outScheduleDate!=null){
            toDatabase();
        }
        if (outDoor!=null){
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