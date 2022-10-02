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
    TextView textView;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        textView=view.findViewById(R.id.Greetings);
        String greeting = null;

        if (hour >= 12 && hour < 17) {
            Toast.makeText(getActivity(), "Good Afternoon", Toast.LENGTH_LONG).show();

        } else if (hour >= 17 && hour < 21) {
            Toast.makeText(getActivity(), "Good Evening", Toast.LENGTH_LONG).show();
        } else if (hour >= 21 && hour < 24) {
            Toast.makeText(getActivity(), "Good Night", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Good Morning", Toast.LENGTH_LONG).show();
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}