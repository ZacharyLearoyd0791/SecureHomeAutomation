/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


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

public class HomeFragment extends Fragment {

    UserInfo userInfo = new UserInfo();
    StringBuilder stringBuilder;
    public View view;
    AlphaAnimation fadeIn, fadeOut;

    //Switches


    //Buttons
    public ImageButton lockBtn;
    public ImageButton tempBtn;
    public ImageButton lightBtn;
    public ImageButton windowBtn;

    //Text View
    public TextView greetingsText,DoorStatusTV,LightStatusTV,TempStatusTV,WindowsStatusTV;

    //ImageView
    public ImageView doorView;
    public ImageView tempView;
    public ImageView lightView;
    public ImageView windowView;
    public ImageView pressLock;
    public ImageView pressTemp;
    public ImageView pressLight;
    public ImageView pressWindow;
    public ImageView grnLight;


    final Handler handler = new Handler();

    //Database
    WindowFragment windowFragment=new WindowFragment();
    TempFragment tempFragment=new TempFragment();
    DatabaseActivity databaseActivity = new DatabaseActivity();


    //Date
    DateFormat dateFormat;
    String morning, afternoon, evening, night, on, off;
    Date date;
    Calendar cal;
    int hour;

    //Misc Strings
    String doorStatus, lightStatus, maxStatus,windowStatus,
            minStatus, lock,
            unlock, armed, disarmed;

    private Handler handlerRun;
    private Runnable handlerTask;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //ImageView Objects
        doorView = view.findViewById(R.id.doorInfo);
        tempView = view.findViewById(R.id.tempInfo);
        lightView = view.findViewById(R.id.lightInfo);
        windowView = view.findViewById(R.id.windowInfo);
        grnLight = view.findViewById(R.id.greenLight);
        databaseActivity.Activity();
        windowFragment.getFromDataBase();
        tempFragment.dbID();

        DoorStatusTV=view.findViewById(R.id.DoorStatusTVH);
        LightStatusTV=view.findViewById(R.id.LightStatusTVH);
        TempStatusTV=view.findViewById(R.id.TempStatusTVH);
        WindowsStatusTV=view.findViewById(R.id.WindowsStatusTVH);

        tempView.setVisibility(View.INVISIBLE);
        windowView.setVisibility(View.INVISIBLE);
        lightView.setVisibility(View.INVISIBLE);

        init();
        greeting();
        StartTimer();
        Bundle bundle = new Bundle();
        Status();
        WindowsStatusTV.setText("0");

        return view;
    }

    void StartTimer() {
        handlerRun = new Handler();
        handlerTask = new Runnable() {

            @Override
            public void run() {

                //door status
                doorStatus = databaseActivity.DBDoor;
                if(doorStatus!=null) {
                    DoorStatusTV.setText(doorStatus);
                    DoorStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    DoorStatusTV.setTextSize(15);

                    if(doorStatus.equals("Locked"))
                    {doorView.setVisibility(View.INVISIBLE);}

                    else if(doorStatus.equals("Unlocked"))
                    {doorView.setVisibility(View.VISIBLE);}
                }
                else{
                    DoorStatusTV.setText(R.string.NoVal);
                    DoorStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    DoorStatusTV.setTextSize(15);
                    doorView.setVisibility(View.VISIBLE);
                }

                //light status
                lightStatus = databaseActivity.DBLight;
                if (lightStatus!=null){
                    LightStatusTV.setText(lightStatus);
                    LightStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    LightStatusTV.setTextSize(15);

                    if(lightStatus.equals("On"))
                    {lightView.setVisibility(View.INVISIBLE);}

                    else if(lightStatus.equals("Off"))
                    {lightView.setVisibility(View.VISIBLE);}
                }

                else{
                    LightStatusTV.setText(R.string.NoVal);
                    LightStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    LightStatusTV.setTextSize(15);
                }

                //window status
                windowStatus = String.valueOf(windowFragment.numberOfAlerts);
                if(windowStatus!=null){
                    WindowsStatusTV.setText(windowStatus);
                    WindowsStatusTV.setText("Armed");
                    WindowsStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    WindowsStatusTV.setTextSize(15);
                }
                else{
                    WindowsStatusTV.setText(R.string.status_device+R.string.NoVal);
                    WindowsStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    WindowsStatusTV.setTextSize(15);
                }
                minStatus= String.valueOf(tempFragment.minimumTemperature);
                maxStatus= String.valueOf(tempFragment.maximumTemperature);

                if(minStatus!=null){
                    TempStatusTV.setText("Min Temperature Set: "+minStatus);
                    TempStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    TempStatusTV.setTextSize(15);
                }
                else{
                    TempStatusTV.setText(R.string.noVal);
                    TempStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    TempStatusTV.setTextSize(15);
                }
                if(maxStatus!=null){
                    TempStatusTV.append("\n"+"Max Temperature Set: "+maxStatus);
                    TempStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    TempStatusTV.setTextSize(15);
                }
                else{
                    TempStatusTV.append(R.string.max_temp+maxStatus);
                    TempStatusTV.setTypeface(Typeface.DEFAULT_BOLD);
                    TempStatusTV.setTextSize(15);
                }


                //Log.d(TAG,"Testing remove 10m \n 2002 \n"+doorStatus+"\n"+lightStatus);

                handler.postDelayed(handlerTask, 1);
            }
        };
        handlerTask.run();
    }

    private void Status(){


    }

    private void init(){

        //Buttons
        lockBtn = view.findViewById(R.id.lock_Btn);
        tempBtn = view.findViewById(R.id.temp_Btn);
        lightBtn = view.findViewById(R.id.light_Btn);
        windowBtn = view.findViewById(R.id.window_Btn);

        //ImageView
        pressLock = view.findViewById(R.id.iv_press_lock);
        pressTemp = view.findViewById(R.id.iv_press_temp);
        pressLight = view.findViewById(R.id.iv_press_light);
        pressWindow = view.findViewById(R.id.iv_press_window);

        on=getString(R.string.on);
        off=getString(R.string.off);
        lock = getString(R.string.lock_status);
        unlock = getString(R.string.unlocked_status);
        armed=getString(R.string.sensor_on);
        disarmed=getString(R.string.sensor_off);
    }

    private void greeting(){
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        greetingsText = view.findViewById(R.id.Greetings);
        greetingsText.setText(null);

        morning=getString(R.string.greetingMorning);
        afternoon=getString(R.string.greetingAfternoon);
        evening=getString(R.string.greetingEvening);
        night=getString(R.string.greetingNight);
        greetingsText.setTypeface(Typeface.DEFAULT_BOLD);
        greetingsText.setTextSize(20);
        if (hour >= 6 && hour < 12) {
            greetingsText.setText(morning);
        }
        else if (hour >= 12 && hour < 17) {
            greetingsText.setText(afternoon);
        }
        else if (hour >= 17 && hour < 21) {
            greetingsText.setText(evening);
        }
        else {
            greetingsText.setText(night);
        }
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){

        pressLock.setVisibility(View.INVISIBLE);
        pressTemp.setVisibility(View.INVISIBLE);
        pressLight.setVisibility(View.INVISIBLE);

        //Lock button
        lockBtn.setOnClickListener(view14 -> {
            lockBtn.setVisibility(View.INVISIBLE);
            pressLock.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.doorFragment).commit(), 300);
            //MainActivity.bottomNav.setSelectedItemId(R.id.door);
        });

        //Temperature button
        tempBtn.setOnClickListener(view13 -> {
            tempBtn.setVisibility(View.INVISIBLE);
            pressTemp.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.tempFragment).commit(), 300);
            //MainActivity.bottomNav.setSelectedItemId(R.id.temp);
        });

        //Light button
        lightBtn.setOnClickListener(view12 -> {
            lightBtn.setVisibility(View.INVISIBLE);
            pressLight.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.lightFragment).commit(), 300);

            //MainActivity.bottomNav.setSelectedItemId(R.id.light);
        });

        //Window button
        windowBtn.setOnClickListener(view1 -> {
            windowBtn.setVisibility(View.INVISIBLE);
            pressWindow.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.windowFragment).commit(), 300);
            //MainActivity.bottomNav.setSelectedItemId(R.id.window);
        });

    }
}