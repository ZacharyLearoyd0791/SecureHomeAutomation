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

import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView greetingsText;
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated (View view,
                               Bundle savedInstanceState){

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        greetingsText =view.findViewById(R.id.Greetings);

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
    }


}