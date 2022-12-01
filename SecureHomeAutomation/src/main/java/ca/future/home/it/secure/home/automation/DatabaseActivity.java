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
import android.util.Log;

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

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    UserInfo userInfo=new UserInfo();
    static Context context;

    //Date
    Date date;
    DateFormat dateFormat;

    //Key string
    String finalDoorKey,localKey,key,personalKey,strDate,
            finalSensorKey, finalStatusKey,statusKey,SensorKey,doorKey,maxKey,minKey,finalMaxKey,finalMinKey,
                finalWindowBreak,windowBKey;

    //Database String
    String DBDoor,DBLight,DBDist,DBWindow, DBMax,DBMin;
    int min,max;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public void Activity(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.d(TAG, "DatabaseActivity Active!");
        time();

    }

    private void initString() {

        doorKey=getApplicationContext().getString(R.string.forwardslash)+getApplicationContext().getString(R.string.door_status);
        statusKey=getApplicationContext().getString(R.string.statusKey);
        SensorKey=getApplicationContext().getString(R.string.db_ultrasonic_dist);
        maxKey=getApplicationContext().getString(R.string.tempmax);
        minKey=getApplicationContext().getString(R.string.tempmin);
        windowBKey=getApplicationContext().getString(R.string.windowBreakKey);
        dbID();

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

        //Temp related user key:
        finalMaxKey=key+maxKey;
        finalMinKey=key+minKey;
        //Window related user key:
        finalWindowBreak=key+windowBKey;

        //Log key to confirm for testing
        Log.d(TAG,"Key grabbed from DatabaseClass: \n"+finalDoorKey+"\n"+finalStatusKey+"\n"+finalSensorKey+"\n"+finalMaxKey+"\n"+finalMinKey+"\n"+finalWindowBreak);
        getDB();
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
        initString();
    }
    public void getDB(){
        //door
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DBDoor= Objects.requireNonNull(snapshot.getValue()).toString();
                    DoorDBAction(DBDoor);
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
                                TemperatureDBAction(DBMax,DBMin);
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
                    DBLight= Objects.requireNonNull(snapshot.getValue()).toString();//on or off
                    LightStatusDBAction(DBLight);
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
                    DistDBAction(DBDist);
                }
                else {
                    Log.d(TAG, "Distance Status No Current Value");
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
                    WindowDBAction(DBWindow);
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
//Window Action
    private void WindowDBAction(String dbWindow) {
        Log.d(TAG,"Status of Window is: "+dbWindow);
    }
    
    //Light Action
    private void LightStatusDBAction(String dbLight) {
        Log.d(TAG,"Status of Light is: "+dbLight);
    }
    private void DistDBAction(String dbDist) {
        Log.d(TAG,"Current Distance on sensor is: "+dbDist);
    }

    //Temperature action
    private void TemperatureDBAction(String dbMax, String dbMin) {
        try{
            max = Integer.parseInt(dbMax);
            Log.d(TAG,"Max value:\t"+max);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        try{
            min = Integer.parseInt(dbMin);
            Log.d(TAG,"Min value:\t"+min);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
//Door action
    private void DoorDBAction(String dbDoor) {
        Log.d(TAG,"Door Status"+dbDoor);
    }

    //FOR DOOR LOCK
    public void toDatabase(String status){
        DatabaseActivity.context = getApplicationContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalSensorKey));

        databaseReference.setValue(status);
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put(getString(R.string.status),status);

        databaseReference.updateChildren(updateStatus);
    }
}