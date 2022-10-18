/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;




import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class WindowFragment extends Fragment {

    //Declarations
    Button notificationButton;
    public WindowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_window, container, false);
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("WindowBreak","windows", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification to be sent when window is broken");
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        notificationButton = view.findViewById(R.id.Notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotificationProcess();
            }
        });
        return view;
    }
    public void sendNotificationProcess(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"WindowBreak");
        builder.setContentTitle("Window Sensor Activated!")
                .setContentText("There is a window break in the house")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mangerCompat = NotificationManagerCompat.from(getContext());
        mangerCompat.notify(1,builder.build());
    }
}