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

public class HomeFragment extends Fragment {

    UserInfo userInfo=new UserInfo();
    StringBuilder stringBuilder;
    public View view;
    AlphaAnimation fadeIn,fadeOut;

    //Switches
    public Switch lockSwitch;
    public Switch tempSwitch;
    public Switch lightSwitch;
    public Switch windowSwitch;

    //Buttons
    public ImageButton lockBtn;
    public ImageButton tempBtn;
    public ImageButton lightBtn;
    public ImageButton windowBtn;

    //Text View
    public TextView greetingsText;

    //ImageView
    public ImageView doorView;
    public ImageView tempView;
    public ImageView lightView;
    public ImageView windowView;
    public ImageView pressLock;
    public ImageView pressTemp;
    public ImageView pressLight;
    public ImageView pressWindow;

    final Handler handler = new Handler();

    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseActivity databaseActivity = new DatabaseActivity();

    //Date
    DateFormat dateFormat;
    String morning,afternoon,evening,night;
    Date date;
    Calendar cal;
    int hour;

    //String
    String idKey,localKey,key,personalKey,strDate,doorKey;
    boolean retrieveKey;
    String lock;
    String unlock;
    String doorStatus;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        greeting();
        lock= getString(R.string.lock_status);
        unlock=getString(R.string.unlocked_status);

        Bundle bundle = new Bundle();

        return view;
    }

    private void init(){
        //Switches
        lockSwitch = view.findViewById(R.id.sw_lock);
        tempSwitch = view.findViewById(R.id.sw_temp);
        lightSwitch = view.findViewById(R.id.sw_light);
        windowSwitch = view.findViewById(R.id.sw_window);

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
    }

    private void greeting(){
        greetingsText = view.findViewById(R.id.Greetings);
        greetingsText.setText(null);

        morning=getString(R.string.greetingMorning);
        afternoon=getString(R.string.greetingAfternoon);
        evening=getString(R.string.greetingEvening);
        night=getString(R.string.greetingNight);

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

        //Door lock status in database
        doorKey=dbID(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(doorKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doorStatus=null;
                if(snapshot.exists()) {
                    doorStatus = snapshot.getValue().toString();
                    if (doorStatus.equals(lock)) {
                        lockSwitch.setChecked(true);
                        doorView.setVisibility(View.INVISIBLE);
                    } else if (doorStatus.equals(unlock)) {
                        lockSwitch.setChecked(false);
                        doorView.setVisibility(View.VISIBLE);
                    }
                    lockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            doorView.setVisibility(View.INVISIBLE);
                            //databaseActivity.toDatabase(getString(R.string.lock_status));
                            toDatabase(getString(R.string.lock_status));

                        } else {
                            doorView.setVisibility(View.VISIBLE);
                            toDatabase(getString(R.string.unlocked_status));
                            //databaseActivity.toDatabase(getString(R.string.unlocked_status));
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

        //ImageView Objects
        doorView = view.findViewById(R.id.doorInfo);
        tempView = view.findViewById(R.id.tempInfo);
        lightView = view.findViewById(R.id.lightInfo);
        windowView = view.findViewById(R.id.windowInfo);

        //Switches selected
        //lock switch

        //temperature switch
        tempSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tempView.setVisibility(View.INVISIBLE);
            } else {
                tempView.setVisibility(View.VISIBLE);
            }
        });
        //light switch
        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lightView.setVisibility(View.INVISIBLE);
            } else {
                lightView.setVisibility(View.VISIBLE);
            }
        });
        //window switch
        windowSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                windowView.setVisibility(View.INVISIBLE);
            } else {
                windowView.setVisibility(View.VISIBLE);
            }
        });
    }

    ////////////////////////
    //Temporary fix for storing to database while DatabaseActivity is debugged
    /////////////////////////
    public void toDatabase(String status){
        dbID(false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child((idKey));

        databaseReference.setValue(status);
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("Status ",status);

        databaseReference.updateChildren(updateStatus);
    }
    private String dbID(boolean retrieveKey){
        userInfo.typeAccount();
        time();

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

        if(retrieveKey){
            return idKey=key+"/Door status/Status ";
        }

        idKey=key+getString(R.string.forwardslash)+getString(R.string.door_status)+getString(R.string.forwardslash);
        return "";
    }
    @SuppressLint("SimpleDateFormat")
    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getString(R.string.formatted));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
        System.out.println("Converted String: " + strDate);
    }
}