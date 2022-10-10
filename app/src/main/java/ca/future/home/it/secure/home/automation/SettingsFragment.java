/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (???) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private Switch boldSwitch;
    private Switch colourSwitch;
    private View view;
    private TextView tvBold;
    private TextView tvColour;
    private ToggleButton doorToggle;
    private TextView tvGreetings;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boldSwitch = view.findViewById(R.id.bold_switch);
        colourSwitch = view.findViewById(R.id.colour_switch);
        tvBold = view.findViewById(R.id.tv_bold);
        tvColour = view.findViewById(R.id.tv_colour);

        boldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvBold.setTypeface(null, Typeface.BOLD);
                    tvColour.setTypeface(null, Typeface.BOLD);
                    boldSwitch.setTypeface(null, Typeface.BOLD);
                    colourSwitch.setTypeface(null, Typeface.BOLD);

                } else {
                    tvBold.setTypeface(null, Typeface.NORMAL);
                    tvColour.setTypeface(null, Typeface.NORMAL);
                    boldSwitch.setTypeface(null, Typeface.NORMAL);
                    colourSwitch.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
    }


}