/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class LightFragment extends Fragment {
    public int counter;
    TextView timerTV;
    int hour, minute;

    public LightFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light, container, false);

        timerTV = view.findViewById(R.id.timerTV);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button timerBTN = view.findViewById(R.id.timerButton);

        timerBTN.setOnClickListener(v -> popTimePicker());


    }

    public void popTimePicker() {
        //timerTV.setText("testing");
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {

            hour = selectedHour;
            minute = selectedMinute;
            String timeout = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            Log.d(TAG, timeout);
            timerTV.setText(getString(R.string.timeSet) + timeout);

            int hourmilli = hour * 600000;
            int minmilli = minute * 60000;
            int milli = hourmilli + minmilli;

            new CountDownTimer(milli, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, String.valueOf(counter));

                    counter++;
                }

                public void onFinish() {
                    timerTV.setText(R.string.lightOff);
                    Log.d(TAG, "Finished");

                }
            }.start();
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


}