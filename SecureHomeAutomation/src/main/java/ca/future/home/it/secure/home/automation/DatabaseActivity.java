package ca.future.home.it.secure.home.automation;


import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatabaseActivity extends Fragment {
    UserInfo userInfo = new UserInfo();
    LightFragment lightFragment = new LightFragment();
    DoorFragment doorFragment = new DoorFragment();

    private static final int NOTIFICATION_ID = 123;
    private NotificationManagerCompat notificationManager;
    private Notification notification;
    String finalCityKey, city;

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    static Context context;

    //Date
    Date date;
    String valueLimit;
    DateFormat dateFormat;

    //Database String
    public String DBDoor, DBLight, DBDist, DBWindow, DBMax, DBAddKey, DBRemoveKey, DBMin, DBScheduleDay, DBScheduleTime, name, email, hardwareKey,
            finalhardwareKey, serialNumber, finalAddKey, finalRemoveKey;
    //Key string
    String finalDoorKey, localKey, key, personalKey, strDate,
            finalSensorKey, finalStatusKey, statusKey, SensorKey, doorKey, maxKey, minKey, finalMaxKey, finalMinKey,
            finalWindowBreak, windowBKey, finaldateKey, finalTimeKey, scheduleKey, userKey, addKey, removeKey;
    String outDoor, outLight, userData, outAddKey, outRemoveKey, outWindow, outMax, outMin, outScheduleDate, userDetails, outLimit;
    String alertMode;
    FragmentActivity viewActivity;
    View rootView;
    int min, max;
    private Handler handler;
    private Runnable handlerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_main, container, false);

        // Get the package name of the app
        viewActivity = getActivity();
        //Activity();
        return rootView;
    }

    void StartTimer() {
        handler = new Handler();
        handlerTask = new Runnable() {
            @Override
            public void run() {
                // do something
                sendDataStrings();
                System.gc();

                handler.postDelayed(this, 1000);
            }
        };
        handlerTask.run();
    }

    public void Activity() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        time();
        initString();
        dbID();
        getDB();
        StartTimer();


        //AlertMode();
    }

    private void initString() {

        doorKey = getApplicationContext().getString(R.string.forwardslash) + getApplicationContext().getString(R.string.door_status);
        statusKey = getApplicationContext().getString(R.string.statusKey);
        SensorKey = getApplicationContext().getString(R.string.db_ultrasonic_dist);
        maxKey = getApplicationContext().getString(R.string.tempmax);
        minKey = getApplicationContext().getString(R.string.tempmin);
        windowBKey = getApplicationContext().getString(R.string.windowBreakKey);
        scheduleKey = getApplicationContext().getString(R.string.schedKey);
        userKey = getApplicationContext().getString(R.string.userKey);
        userDetails = getApplicationContext().getString(R.string.userDetails);
        userData = getApplicationContext().getString(R.string.userData);
        name = getApplicationContext().getString(R.string.name);
        email = getApplicationContext().getString(R.string.email);
        hardwareKey = getApplicationContext().getString(R.string.serialHardware);
        addKey = getApplicationContext().getString(R.string.addKeyStatus);
        removeKey = getApplicationContext().getString(R.string.removeKeyDB);
    }

    public void dbID() {
        userInfo.typeAccount();
        localKey = userInfo.userId;
        personalKey = userInfo.idInfo;

        if (localKey != null) {
            key = localKey;
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey + key + userDetails + name));
            databaseReference.setValue(userInfo.name);
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey + key + userDetails + email));
            databaseReference.setValue(userInfo.email);
        }
        if (personalKey != null) {
            key = personalKey;
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey + key + userDetails + name));
            databaseReference.setValue(userInfo.personName);
            databaseReference = FirebaseDatabase.getInstance().getReference().child((userKey + key + userDetails + email));
            databaseReference.setValue(userInfo.personEmail);
        }

        //Door Fragment
        finalDoorKey = userKey + key + userData + doorKey;
        finalAddKey = userKey + key + userData + addKey;
        finalRemoveKey = userKey + key + userData + removeKey;

        finalhardwareKey = userKey + key + hardwareKey;

        //Light related user key:
        finalStatusKey = userKey + key + userData + statusKey;
        finalSensorKey = userKey + key + userData + SensorKey;
        finaldateKey = userKey + key + userData + scheduleKey + getApplicationContext().getString(R.string.dayKey);


        //Temp related user key:
        finalMaxKey = userKey + key + userData + maxKey;
        finalMinKey = userKey + key + userData + minKey;

        //Window related user key:
        finalWindowBreak = userKey + key + userData + windowBKey;
    }

    private void time() {
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getApplicationContext().getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
    }

    public void getDB() {
        setCity();

        //serial
        databaseReference = FirebaseDatabase.getInstance().getReference().child(finalhardwareKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    serialNumber = Objects.requireNonNull(snapshot.getValue().toString());
                    getserialNumber();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //door
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DBDoor = Objects.requireNonNull(snapshot.getValue()).toString();
                    DoorDBAction();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Door Add Key
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalAddKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DBAddKey = Objects.requireNonNull(snapshot.getValue()).toString();
                    AddKeyDBAction();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Door Remove Key
        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalRemoveKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DBRemoveKey = Objects.requireNonNull(snapshot.getValue()).toString();
                    RemoveKeyDBAction();
                } else {
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
                if (snapshot.exists()) {
                    DBMax = Objects.requireNonNull(snapshot.getValue().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference().child((finalMinKey));
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DBMin = Objects.requireNonNull(snapshot.getValue().toString());
                                TemperatureDBAction();
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
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
                if (snapshot.exists()) {
                    DBLight = Objects.requireNonNull(snapshot.getValue()).toString();
                    LightStatusDBAction();
                } else {
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
                if (snapshot.exists()) {
                    DBDist = Objects.requireNonNull(snapshot.getValue()).toString();//on or off
                    DistDBAction();
                } else {
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
                if (snapshot.exists()) {
                    DBScheduleDay = Objects.requireNonNull(snapshot.getValue()).toString();

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

                } else {

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
                if (snapshot.exists()) {
                    DBWindow = Objects.requireNonNull(snapshot.getValue()).toString();
                    WindowDBAction();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String getserialNumber() {
        //String serialNum=serialNumber;

        return serialNumber;
    }

    private void ScheduleDBAction() {

    }

    //Window Action
    private void WindowDBAction() {
    }

    //Light Action
    private void LightStatusDBAction() {
        LightFragment.statusOfLight = (DBLight);

    }

    private void DistDBAction() {
        LightFragment.dist = (DBDist);
    }

    //Temperature action
    private void TemperatureDBAction() {

    }

    //Door action
    private void DoorDBAction() {
        DoorFragment.statusofDoor = (DBDoor);

    }
    private void AddKeyDBAction() {
        DoorFragment.statusAdd = (DBAddKey);
    }

    private void RemoveKeyDBAction() {
        DoorFragment.statusRemove = (DBRemoveKey);
    }

    private void sendDataStrings() {

        outLight = LightFragment.statusOfLight;
        outDoor = DoorFragment.statusofDoor;
        outAddKey = DoorFragment.statusAdd;
        outRemoveKey = DoorFragment.statusRemove;

        if (outLight != null) {
            toDatabase();
        }

        if (outDoor != null) {
            toDatabase();
        }
        if (outAddKey != null){
            toDatabase();
        }
        if (outRemoveKey != null){
            toDatabase();
        }
        System.gc();

    }

    //FOR DOOR LOCK
    public void toDatabase() {


        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalDoorKey));
        databaseReference.setValue(outDoor);

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalAddKey));
        databaseReference.setValue(outAddKey);

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalRemoveKey));
        databaseReference.setValue(outRemoveKey);

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalStatusKey));
        databaseReference.setValue(outLight);
        System.gc();


    }

    public void AlertMode() {
        // Set up a listener for the node you want to monitor
        String AlarmKey = userKey + key + ("/userData/Windows Sensor/Alarm Triggered");
        Log.d(TAG,AlarmKey);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(AlarmKey);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d(TAG,(snapshot.getValue().toString()));

                    if (Objects.requireNonNull(snapshot.getValue().toString()).equals("On")){
                        Log.d(TAG,"adad");
                    WindowBreakNotification();

                    }
                    else{

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(listener);


    }

    public void WindowBreakNotification() {

        Context newContext = getApplicationContext(); // Use getContext() method to get the context of the fragment
        String channelId = "my_channel";

        notification = new NotificationCompat.Builder(newContext, channelId)
                .setContentTitle(getApplicationContext().getString(R.string.alert))
                .setContentText(getApplicationContext().getString(R.string.messageContentBreak))
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        
    }

    public void Notification() {
        Context newContext = getApplicationContext(); // Use getContext() method to get the context of the fragment


        String channelId = "my_channel_id";

// Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(newContext, channelId)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(getApplicationContext().getString(R.string.alert))
                .setContentText(getApplicationContext().getString(R.string.messageContent))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

// Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(newContext);
        notificationManager.notify(1, builder.build());

    }

    public void setCity() {

        finalCityKey = userKey + key + "/settings/City";
        databaseReference = FirebaseDatabase.getInstance().getReference().child(finalCityKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    city = snapshot.getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
