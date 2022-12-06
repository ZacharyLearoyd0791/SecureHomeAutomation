/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WindowFragment extends Fragment {

    //Declarations
    RecyclerView activitiesRecyclerView;
    Button notificationButton;
    TextView deviceStatusTextView;
    Button alarmButton;
    Vibrator vibrator;
    View view;
    String deviceState;
    ToggleButton sensorPowerButton;
    UserInfo userInfo=new UserInfo();
    String key,localKey,personalKey,windowsKey,sensorKey,userKey,userData;
    String[] sensorStatusArray = {"Sensor is ON","Sensor is OFF","Sensor is Activated","Sensor is Deactivated","Sensor is in TEST mode"};
    String sensorStatus = "Sensor is off";
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<RecyclerViewData> data;
    public static int numberOfAlerts;
    private int alertCode;
    static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;





    public WindowFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_window, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Number of activities",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        numberOfAlerts = sharedPreferences.getInt("Number of Alerts",0);
        alertCode = sharedPreferences.getInt("Code of Alerts",0);
        getFromDataBase();



        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        alarmButton = view.findViewById(R.id.TestDeviceButton);
        activitiesRecyclerView = view.findViewById(R.id.windows_recycler_view);
        deviceStatusTextView = view.findViewById(R.id.device_status_windows_break);
        sensorPowerButton = view.findViewById(R.id.windows_sensor_on_off_button);
        sensorPowerButton.setChecked( sharedPreferences.getBoolean("Power state", false));
        //Recycler View
        data = RecyclerViewData.createWindowsAlertList(numberOfAlerts);
        RecyclerAdapter adapter = new RecyclerAdapter(data);
        activitiesRecyclerView.setAdapter(adapter);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //If SDK is lower than oreo then creating channel for notification process
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(getApplicationContext().getString(R.string.window_break),getApplicationContext().getString(R.string.windows), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getApplicationContext().getString(R.string.notify_window_break));
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfAlerts++;
                alertCode = 2;
                editor.putInt("Number of Alerts", numberOfAlerts);
                editor.putInt("Code of Alerts",alertCode);
                editor.commit();
                alarmProcess();
            }
        });

        //Toggle button for power on/off
        sensorPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensorPowerButton.isChecked()){
                    numberOfAlerts++;
                    alertCode = 1;
                    editor.putBoolean("Power state",sensorPowerButton.isChecked());
                    editor.putInt("Number of Alerts", numberOfAlerts);
                    editor.putInt("Code of Alerts",alertCode);
                    editor.commit();

                    Toast.makeText(getContext(), "Sensor turned ON!"+alertCode, Toast.LENGTH_SHORT).show();
                }else{
                    alertCode = 0;
                    numberOfAlerts++;
                    editor.putBoolean("Power state",sensorPowerButton.isChecked());
                    editor.putInt("Number of Alerts", numberOfAlerts);
                    editor.putInt("Code of Alerts",alertCode);
                    editor.commit();
                    Toast.makeText(getContext(), "Sensor turned OFF"+alertCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
        putToDatabase();
        return view;
    }


    public void sendNotificationProcess(String notificationTitle, String notificationText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),getApplicationContext().getString(R.string.window_break));
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mangerCompat = NotificationManagerCompat.from(getContext());
        mangerCompat.notify(1,builder.build());
    }

    public void alarmProcess(){
        String notificationTitle = getApplicationContext().getString(R.string.window_on_test);
        String notificationText = getApplicationContext().getString(R.string.window_alarm_active);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.test_device_req)
                .setMessage(R.string.activate_window_alarm)
                .setPositiveButton(R.string.yes,(DialogInterface.OnClickListener)(dialog, which) ->{
                    sendNotificationProcess(notificationTitle,notificationText);
                    createSnackBar();
                })
                .setNegativeButton(R.string.no,(DialogInterface.OnClickListener)(dialog, which)->{
                    dialog.cancel();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        VibrationEffect vibrationEffect;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(10000,VibrationEffect.EFFECT_HEAVY_CLICK);
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect);
        }
    }
    public void createSnackBar(){
        Snackbar snackbar = Snackbar.make(view.findViewById(R.id.frameLayout5), R.string.turn_off_req,Snackbar.LENGTH_LONG)
                .setAction(R.string.off, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), R.string.alarm_terminated, Toast.LENGTH_LONG).show();
                    }
                });
        snackbar.show();
    }
    private String dbID(){
        userInfo.typeAccount();
        userKey=getApplicationContext().getString(R.string.userKey);
        userData=getApplicationContext().getString(R.string.userData);


        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
        }
        if(personalKey!=null) {
            key= personalKey;
        }
        windowsKey=key+userData+getApplicationContext().getString(R.string.windowKey);
        sensorKey=windowsKey;

        return userKey+windowsKey  ;
    }
    private void putToDatabase(){
        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.toString();
        //Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
        int alertCodeData =  sharedPreferences.getInt("Code of Alerts",0);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(dbID()).child("Activities").child(String.valueOf(numberOfAlerts)).child("Time").setValue(time);     //This will store time of Alert
        reference.child(dbID()).child("Activities").child(String.valueOf(numberOfAlerts)).child("Alert Code").setValue(alertCodeData);    //This stores the type of Alert
       if(alertCode==0) {
           reference.child(dbID()).child("Device Status Code").setValue("0");  //This code will get the status of the device, if device is off
       }else if(alertCode ==1){
           reference.child(dbID()).child("Device Status Code").setValue("1");  //This code will get the status of the device, if device is on
       }else if(alertCode == 4){
           reference.child(dbID()).child("Device Status Code").setValue("-1");  //This code will get the status of the device, if device is error
       }
        reference.child(dbID()).child("Sensor Activities number").setValue(numberOfAlerts);  //This is number of activity/alerts

    }

    public void getFromDataBase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceState = snapshot.child(dbID()).child("Device Status Code").toString();
                if(deviceState.contains("-2")){
                    deviceStatusTextView.setText(R.string.devicenotfound);
                    sensorStatus ="Device not Available";
                    deviceStatusTextView.setTextColor(Color.BLACK);
                }else if (deviceState.contains("0")){
                    deviceStatusTextView.setText("Turned OFF");
                    sensorStatus ="Device turned off";
                    deviceStatusTextView.setTextColor(Color.BLACK);
                }else if(deviceState.contains("1")){
                    deviceStatusTextView.setText(R.string.activedevice);
                    deviceStatusTextView.setTextColor(Color.GREEN);
                }else if(deviceState.contains("2")){
                    deviceStatusTextView.setText(R.string.device_deactivated);
                    deviceStatusTextView.setTextColor(Color.RED);
                    sensorStatus = "Device turned OFF";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getSensorStatus(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensorStatus = snapshot.child(dbID()).child("Device Status").getValue().toString();
                numberOfAlerts = Integer.parseInt(snapshot.child(dbID()).child("Sensor Activities number").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}