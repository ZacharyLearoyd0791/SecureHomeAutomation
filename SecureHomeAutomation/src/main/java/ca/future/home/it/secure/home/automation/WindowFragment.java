/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;




import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class WindowFragment extends Fragment {

    //Declarations
    Button notificationButton;
    Button alarmButton;
    Vibrator vibrator;
    View view;
    public WindowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_window, container, false);
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
       vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        alarmButton = view.findViewById(R.id.AlarmButton);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("WindowBreak","windows", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification to be sent when window is broken");
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
//        notificationButton = view.findViewById(R.id.Notification_button);
//        notificationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendNotificationProcess();
//            }
//        });

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmProcess();
            }
        });
        return view;
    }
    public void sendNotificationProcess(String notificationTitle, String notificationText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"WindowBreak");
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mangerCompat = NotificationManagerCompat.from(getContext());
        mangerCompat.notify(1,builder.build());
    }
    public void alarmProcess(){
        String notificationTitle = "Window Sensor on Test!";
        String notificationText = "Window Sensor is Activated for Testing, don't panic :)";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Do you want to test this device?")
                .setMessage("Clicking Yes will activate the alarm system which can detect windows break. Alarm will be activated for 15 s ")
                .setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which) ->{
                    sendNotificationProcess(notificationTitle,notificationText);
                    createSnackBar();
                })
                .setNegativeButton("No",(DialogInterface.OnClickListener)(dialog,which)->{
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
        Snackbar snackbar = Snackbar.make(view.findViewById(R.id.frameLayout5),"Do you want to Turn Off the alarm?",Snackbar.LENGTH_LONG)
                .setAction("Off", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Alarm test terminated!", Toast.LENGTH_LONG).show();
                    }
                });
        snackbar.show();
    }
}