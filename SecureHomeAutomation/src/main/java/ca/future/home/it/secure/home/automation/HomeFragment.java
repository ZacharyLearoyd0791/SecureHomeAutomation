/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    public Date date;


    //Switches
    public Calendar cal;
    public int hour;
    public FireBaseInfo fireBaseInfo = new FireBaseInfo();

    private View view;
    private String name;
    private Switch lockSwitch;
    private Switch tempSwitch;
    private Switch lightSwitch;
    private Switch windowSwitch;

    //Buttons
    private ImageButton lockBtn;
    private ImageButton tempBtn;
    private ImageButton lightBtn;
    private ImageButton windowBtn;

    //Text View
    private TextView greetingsText;
    private TextView quickAcc;

    //ImageView
    private ImageView doorView;
    private ImageView tempView;
    private ImageView lightView;
    private ImageView windowView;
    private ImageView pressLock;
    private ImageView pressTemp;
    private ImageView pressLight;
    private ImageView pressWindow;

    final Handler handler = new Handler();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        greetings();
        return view;
    }

    public void init() {
        //ImageView Objects
        doorView = view.findViewById(R.id.doorInfo);
        tempView = view.findViewById(R.id.tempInfo);
        lightView = view.findViewById(R.id.lightInfo);
        windowView = view.findViewById(R.id.windowInfo);

        //Switch
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

        //Textview
        greetingsText = view.findViewById(R.id.Greetings);
    }

    public void greetings() {
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        hour = cal.get(Calendar.HOUR_OF_DAY);

        greetingsText.setText(null);


        if (hour >= 6 && hour < 12) {

            greetingsText.setText(R.string.greetingMorning);
            view.setBackgroundResource(R.drawable.morning);


        } else if (hour >= 12 && hour < 17) {

            greetingsText.setText(R.string.greetingAfternoon);
            view.setBackgroundResource(R.drawable.afternoon);

        } else if (hour >= 17 && hour < 21) {

            greetingsText.setText(R.string.greetingEvening);
            view.setBackgroundResource(R.drawable.evening);

        } else {

            greetingsText.setText(R.string.greetingNight);
            view.setBackgroundResource(R.drawable.night);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        name = fireBaseInfo.getName();


        String testing="Logging the name to test the code"+name;

        Log.d(TAG, testing);
        if (name != null) {
            greetingsText.append(" " + name);
            Log.d(TAG, name);
        } else {
            greetingsText.append(" " + "No name");

            Log.d(TAG, "No name");
        }

        //Switches

        //Simulate button press - visibility starts invisible
        pressLock.setVisibility(View.INVISIBLE);
        pressTemp.setVisibility(View.INVISIBLE);
        pressLight.setVisibility(View.INVISIBLE);
        pressWindow.setVisibility(View.INVISIBLE);

        //Lock button
        lockBtn.setOnClickListener(view14 -> {
            lockBtn.setVisibility(View.INVISIBLE);
            pressLock.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.doorFragment).commit(), 300);
        });

        //Temperature button
        tempBtn.setOnClickListener(view13 -> {
            tempBtn.setVisibility(View.INVISIBLE);
            pressTemp.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.tempFragment).commit(), 300);
        });

        //Light button
        lightBtn.setOnClickListener(view12 -> {
            lightBtn.setVisibility(View.INVISIBLE);
            pressLight.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.lightFragment).commit(), 300);
        });

        //Window button
        windowBtn.setOnClickListener(view1 -> {
            windowBtn.setVisibility(View.INVISIBLE);
            pressWindow.setVisibility(View.VISIBLE);

            //simulate button pressed
            handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.windowFragment).commit(), 300);
        });


        //Switches selected
        //lock switch
        lockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                doorView.setVisibility(View.INVISIBLE);
            } else {
                doorView.setVisibility(View.VISIBLE);
            }
        });
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
}