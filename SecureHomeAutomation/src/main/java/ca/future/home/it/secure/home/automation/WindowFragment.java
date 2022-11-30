/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WindowFragment extends Fragment {

    //Declarations
    RecyclerView activitiesRecyclerView;
    Button notificationButton;
    TextView deviceStatusTextView;
    Button alarmButton;
    Vibrator vibrator;
    View view;
    int deviceState;

 
    public WindowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_window, container, false);

        //Creating Instances
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        alarmButton = view.findViewById(R.id.TestDeviceButton);
        activitiesRecyclerView = view.findViewById(R.id.activity_recycler_view);
        deviceStatusTextView = view.findViewById(R.id.device_status_windows_break);

        //If SDK is lower than oreo then creating channel for notification process
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(getString(R.string.window_break),getString(R.string.windows), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.notify_window_break));
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmProcess();
            }
        });


        //Activities of the devices using recyclerView

        //Getting Device status from firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Windows Break");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceState = snapshot.child(getString(R.string.status_device)).getValue(Integer.class);
                if(deviceState==0){
                    deviceStatusTextView.setText(R.string.devicenotfound);
                }else if(deviceState ==1){
                    deviceStatusTextView.setText(R.string.activedevice);
                    deviceStatusTextView.setTextColor(Color.GREEN);
                }else if(deviceState == -1){
                    deviceStatusTextView.setText(R.string.errordevice);
                    deviceStatusTextView.setTextColor(Color.RED);
                    Toast.makeText(getContext(), R.string.reconnect, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void sendNotificationProcess(String notificationTitle, String notificationText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),getString(R.string.window_break));
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mangerCompat = NotificationManagerCompat.from(getContext());
        mangerCompat.notify(1,builder.build());
    }

    public void alarmProcess(){
        String notificationTitle = getString(R.string.window_on_test);
        String notificationText = getString(R.string.window_alarm_active);
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


}