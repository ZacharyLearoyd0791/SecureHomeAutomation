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
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class LightFragment extends Fragment {
    private final long timeCountInMilliSeconds = 1 * 60000;
    private final TimerStatus timerStatus = TimerStatus.STOPPED;
    public int counter;
    View view;
    TextView timerTV;
    int hour, minute;
    private Button timerBTN;
    private View mView;

    public LightFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light, container, false);

        this.mView = view;
        timerTV = view.findViewById(R.id.timerTV);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        timerBTN = view.findViewById(R.id.timerButton);

        timerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });


    }

    public void popTimePicker(View view) {
        //timerTV.setText("testing");
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hour = selectedHour;
                minute = selectedMinute;
                String timeout = (String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                Log.d(TAG, timeout);
                timerTV.setText("Time you set in\n" + timeout);

                int hourmilli = hour * 600000;
                int minmilli = minute * 60000;
                int milli = hourmilli + minmilli;

                new CountDownTimer(milli, 1000) {
                    public void onTick(long millisUntilFinished) {
                        Log.d(TAG, String.valueOf(counter));

                        counter++;
                    }

                    public void onFinish() {
                        timerTV.setText("Lights OFF!!");
                        Log.d(TAG, "Finished");

                    }
                }.start();
            }


        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private enum TimerStatus {
        STARTED,
        STOPPED
    }
}