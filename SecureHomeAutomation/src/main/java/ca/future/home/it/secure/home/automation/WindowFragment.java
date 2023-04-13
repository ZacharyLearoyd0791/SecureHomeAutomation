/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WindowFragment extends Fragment {

    //Declarations
    View view;
    AlertDialog dialog;
    RecyclerView activityRecyclerView;
    private List<WindowsFragmentData> windowsFragmentDataList;
    private WindowsFragmentRecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Clearing Activity
    ImageView clearActivityButton;

    //Database Declarations
    String currentTime;

    UserInfo userInfo = new UserInfo();
    String key, localKey, personalKey, windowsKey,sensorKey, userKey, userData;
    DatabaseReference reference;
    String alarmType;
    Boolean alarmStatus;
    int numberOfActivities=0;

    //Power on/off
    TextView deviceStatusTV;
    ImageView powerButton;
    boolean clicked = false;
    String alertDialogTitle;
    String alertDialogMessage;

    int alertDialogCode;  // 1 -> power off, 2 -> power on, 3 -> clear activity data, ....

    //
    //Default constructor
    public WindowFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Check if user is connected to internet
        isConnectedToInternet();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_window, container, false);

        sharedPreferences = getActivity().getSharedPreferences("numberActivities", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        numberOfActivities = sharedPreferences.getInt("numberOfActivities",1);
        Toast.makeText(getContext(),"Activity Num: "+ numberOfActivities, Toast.LENGTH_SHORT).show();
        //Database init
        reference = FirebaseDatabase.getInstance().getReference();

        //Test Values
        alarmStatus = false;
        alarmType = "Null";

        //Referencing
        powerButton = view.findViewById(R.id.windows_sensor_power_button);
        deviceStatusTV = view.findViewById(R.id.device_status_windows_break);
        clearActivityButton = view.findViewById(R.id.windows_clear_activity_data);

        currentTime = Calendar.getInstance().getTime().toString();
        activityRecyclerView = view.findViewById(R.id.windows_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityRecyclerView.setLayoutManager(linearLayoutManager);

        //Getting Activities from DB
        initRecyclerViewItems(numberOfActivities);
        //sendToDB(alarmType,alarmStatus,numberOfActivities);

        //PowerButton functionality

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicked) {
                    alertDialogTitle = "Turn OFF the sensor";
                    alertDialogMessage = "You are sure that you want to turn off the windows break detection sensor!";
                    alertDialogCode = 1;
                    createRecyclerViewItems(2);
                    displayPowerOffAlertDialog(alertDialogTitle,alertDialogMessage, alertDialogCode);

                }else{
                    alertDialogCode = 2;
                    powerButton.setImageResource(R.drawable.red_power_button1);
                    Toast.makeText(getContext(), "Sensor Activated!", Toast.LENGTH_SHORT).show();
                    createRecyclerViewItems(3);
                    clicked = true;
                }
            }
        });


        //Clear Activity Button
        clearActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogTitle = "Clear All Activity Data";
                alertDialogMessage = "You are sure that you want to permanently delete your sensor's Activities";
                alertDialogCode = 3;
                numberOfActivities = 1;
                editor.putInt("numberOfActivities",numberOfActivities);
                editor.commit();
                displayPowerOffAlertDialog(alertDialogTitle,alertDialogMessage, alertDialogCode);
            }
        });


        //Setting value to recyclerView
        windowsFragmentDataList = new ArrayList<>();
        //windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_acknowledge_icon,"Alarm Type \n"+alarmType,"Date \n"+currentTime));
        //windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_acknowledge_icon,"Alarm Type \nTest","Date \n"+currentTime));
//        activityRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));
//        adapter = new WindowsFragmentRecyclerViewAdapter(windowsFragmentDataList);
//        activityRecyclerView.setAdapter(adapter);

        return view;
    }
    public void initRecyclerViewItems(int numActivity){

        for(int i = numActivity; i==1;i--) {
            reference.child(dbID()).child("Activities");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    alarmType = snapshot.child(String.valueOf(numActivity)).child("Alarm Type").getValue().toString();
                    currentTime = snapshot.child(String.valueOf(numActivity)).child("Time").getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(alarmType.equalsIgnoreCase("Device Turned ON")){
                createRecyclerViewItems(3);
            }else if(alarmType.equalsIgnoreCase("Device Turned OFF")){
                createRecyclerViewItems(2);
            }else if(alarmType.equalsIgnoreCase("Device Tested")){
                createRecyclerViewItems(1);
            }else if(alarmType.equalsIgnoreCase("Windows Sensor Activated!")){
                createRecyclerViewItems(4);
            }else{
                Toast.makeText(getContext(), "Cannot get History \nSomething went wrong :(", Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }
    public void createRecyclerViewItems(int dataCode){

            if(dataCode == 1) {
                numberOfActivities ++;
                alarmType = "Device Tested";
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Type").setValue(alarmType);
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Time").setValue(currentTime);
//                reference.child(dbID()).child("Number of Activities").setValue(String.valueOf(numberOfActivities));
                editor.putInt("numberOfActivities",numberOfActivities);
                editor.commit();
                windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_acknowledge_icon, "Alarm Type \n" + alarmType, "Date \n" + currentTime));
            } else if (dataCode == 2) {
                numberOfActivities++;
                alarmType = "Device Turned OFF";
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Type").setValue(alarmType);
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Time").setValue(currentTime);
                //reference.child(dbID()).child("Number of Activities").setValue(String.valueOf(numberOfActivities));
                editor.putInt("numberOfActivities",numberOfActivities);
                editor.commit();
                windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_warning_icon, "Alarm Type \n" + alarmType, "Date \n" + currentTime));
            } else if (dataCode == 3) {
                numberOfActivities++;
                alarmType = "Device Turned ON";
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Type").setValue(alarmType);
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Time").setValue(currentTime);
                //reference.child(dbID()).child("Number of Activities").setValue(String.valueOf(numberOfActivities));
                editor.putInt("numberOfActivities",numberOfActivities);
                editor.commit();
                windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_acknowledge_icon, "Alarm Type \n" + alarmType, "Date \n" + currentTime));
            } else if (dataCode == 4) {
                numberOfActivities++;
                alarmType = "Windows Sensor Activated!";
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Type").setValue(alarmType);
                reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Time").setValue(currentTime);
                //reference.child(dbID()).child("Number of Activities").setValue(String.valueOf(numberOfActivities));
                editor.putInt("numberOfActivities",numberOfActivities);
                editor.commit();
                windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_high_alert_icon, "Alarm Type \n" + alarmType, "Date \n" + currentTime));
            }
        activityRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new WindowsFragmentRecyclerViewAdapter(windowsFragmentDataList);
        activityRecyclerView.setAdapter(adapter);


    }


    //Getting User ID
    private String dbID(){
        userInfo.typeAccount();
        userKey = getApplicationContext().getString(R.string.userKey);
        userData = getApplicationContext().getString(R.string.userData);

        localKey = userInfo.userId;
        personalKey = userInfo.idInfo;

        if(localKey!=null){
            key = localKey;
        }
        if(personalKey !=null){
            key = personalKey;
        }
        windowsKey = key+userData+getApplicationContext().getString(R.string.windowKey);
        sensorKey = windowsKey;

        return userKey+windowsKey;
    }

    private void sendToDB(String alarmType, Boolean alarmStatus, int numberOfAlarmActivities){String time = currentTime.toString();
        String alarmDescription = alarmType;
        Boolean alarmStatusDescription = alarmStatus;
        int numberOfActivities = numberOfAlarmActivities;
        reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Time").setValue(time);
        reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Status").setValue(alarmStatusDescription);
        reference.child(dbID()).child("Activities").child(String.valueOf(numberOfActivities)).child("Alarm Type").setValue(alarmDescription);

    }

    private void clearActivityData(int numberOfActivity){
        for(int i = 0; i< numberOfActivity;i++) {
            Query query = reference.child(dbID()).child("Activities").child(String.valueOf(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void displayPowerOffAlertDialog(String title, String message, int code){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (code == 1){
                            powerButton.setImageResource(R.drawable.green_power_button1);
                            clicked = false;
                            numberOfActivities++;
                        }else if(code == 2){
                            powerButton.setImageResource(R.drawable.red_power_button1);
                            clicked = true;

                        }else if (code == 3){
                            clearActivityData(0);
                            numberOfActivities++;
                        }else {
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    //Check if the user is connected to internet or not
    private boolean isConnectedToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        if(!connected){
            alertDialogMessage = "Your device is not connected to internet. Please connect to internet to have control over the sensor";
            alertDialogTitle = "No Internet Connection";
            alertDialogCode = 4;
            displayPowerOffAlertDialog(alertDialogTitle,alertDialogMessage,alertDialogCode);


        }
        return connected;
    }
}