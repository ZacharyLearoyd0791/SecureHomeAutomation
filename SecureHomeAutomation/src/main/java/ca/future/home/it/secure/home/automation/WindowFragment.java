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

import static ca.future.home.it.secure.home.automation.R.string.sensor_turned_on;
import static ca.future.home.it.secure.home.automation.R.string.user_info;

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
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
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
    RecyclerView activityRecyclerView;
    private List<WindowsFragmentData> windowsFragmentDataList;
    private WindowsFragmentRecyclerViewAdapter adapter;

    //Database Declarations
    Date currentTime;

    UserInfo userInfo = new UserInfo();
    String key, localKey, personalKey, windowsKey,sensorKey, userKey, userData;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    String alarmType;
    Boolean alarmStatus;
    int numberOfActivities;
    //Default constructor
    public WindowFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Test Values
        alarmStatus = false;
        alarmType = "Null";

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_window, container, false);
        currentTime = Calendar.getInstance().getTime();
        activityRecyclerView = (RecyclerView) view.findViewById(R.id.windows_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityRecyclerView.setLayoutManager(linearLayoutManager);

        windowsFragmentDataList = new ArrayList<>();
        windowsFragmentDataList.add(new WindowsFragmentData(R.drawable.ic_windows_break_acknowledge_icon,"Alarm Type \n"+alarmType,"Date \n"+currentTime));
        activityRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new WindowsFragmentRecyclerViewAdapter(windowsFragmentDataList);
        activityRecyclerView.setAdapter(adapter);


        sendToDB(alarmType,alarmStatus,numberOfActivities);
        return view;
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

}