/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    public View view;

    //Switches
    Switch lockSwitch,tempSwitch,lightSwitch, windowSwitch;

    //Buttons
    ImageButton lockBtn,tempBtn,lightBtn,windowBtn;

    //Text View
    TextView greetingsText;

    //ImageView
    ImageView doorView,tempView,lightView, windowView,pressLock,pressTemp,pressLight;

    GoogleSignInAccount acct;

    Date date;
    Calendar cal;
    int hour;
    final Handler handler = new Handler();



    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        init();
        date();
        greetings();
        return view;
    }

    private void init() {
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
        greetingsText = view.findViewById(R.id.Greetings);

    }
    private void date(){
        date= new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        hour = cal.get(Calendar.HOUR_OF_DAY);
    }

    private void greetings(){
        greetingsText.setText(null);

        if (hour >= 6 && hour < 12) {
            greetingsText.setText(R.string.greetingMorning);
        }
        else if (hour >= 12 && hour < 17) {
            greetingsText.setText(R.string.greetingAfternoon);
        }
        else if (hour >= 17 && hour < 21) {
            greetingsText.setText(R.string.greetingEvening);
        }
        else {
            greetingsText.setText(R.string.greetingNight);
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
            getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.windowFragment).commit();
            //MainActivity.bottomNav.setSelectedItemId(R.id.window);
        });

        //ImageView Objects
        doorView = view.findViewById(R.id.doorInfo);
        tempView = view.findViewById(R.id.tempInfo);
        lightView = view.findViewById(R.id.lightInfo);
        windowView = view.findViewById(R.id.windowInfo);

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