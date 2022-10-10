/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (???) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {


    //Switches

    public static Switch lockSwitch;
    public static Switch tempSwitch;
    public static Switch lightSwitch;
    public static Switch windowSwitch;

    //Buttons
    public ImageButton lockBtn;
    public ImageButton tempBtn;
    public ImageButton lightBtn;
    public ImageButton windowBtn;

    public View view;

    //Text View
    public static TextView greetingsText;
    public static TextView quickAcc;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        greetingsText = view.findViewById(R.id.Greetings);

        if(hour>=6 && hour<12){
            Toast.makeText(getActivity(), R.string.greetingMorning, Toast.LENGTH_LONG).show();
            greetingsText.setText(R.string.greetingMorning);
        }
        else if(hour>= 12 && hour < 17){
            Toast.makeText(getActivity(), R.string.greetingAfternoon, Toast.LENGTH_LONG).show();
            greetingsText.setText(R.string.greetingAfternoon);
        }
        else if(hour >= 17 && hour < 21){
            Toast.makeText(getActivity(), R.string.greetingEvening, Toast.LENGTH_LONG).show();
            greetingsText.setText(R.string.greetingEvening);
        }
        else
        {
            Toast.makeText(getActivity(), R.string.greetingNight, Toast.LENGTH_LONG).show();
            greetingsText.setText(R.string.greetingNight);

        }

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

        //Lock button
        lockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.doorFragment).commit();
                //MainActivity.bottomNav.setSelectedItemId(R.id.door);
            }
        });

        //Temperature button
        tempBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.tempFragment).commit();
                //MainActivity.bottomNav.setSelectedItemId(R.id.temp);
            }
        });

        //Light button
        lightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.lightFragment).commit();
                //MainActivity.bottomNav.setSelectedItemId(R.id.light);
            }
        });

        //Window button
        windowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.flFragment, MainActivity.windowFragment).commit();
                //MainActivity.bottomNav.setSelectedItemId(R.id.window);
            }
        });

    }


}