/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.share.Share;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.internal.ConfigGetParameterHandler;


public class SettingsFragment extends Fragment {

    //Switches and Others

    private Switch boldSwitch;
    private Switch colourSwitch;
    private Switch fingerSwitch;
    private Switch portraitSwitch;
    private Button saveButton;
    public static String SETTINGS_PREFS_NAME = "SavedSettings";
    private Boolean boldTextState ;
    private Boolean darkModeState;
    private Boolean fingerPrintState;
    private Boolean screenOrientationState;
    private Boolean faceLockState;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //View
    private View view;

    //Text Views
    private TextView tvBold;
    private TextView tvColour;
    private TextView tvFinger;
    private TextView tvPortrait;

    //Firebase database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MainActivity mainAct = new MainActivity();


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

        //Switches and Others
        boldSwitch = view.findViewById(R.id.bold_switch);
        colourSwitch = view.findViewById(R.id.colour_switch);
        fingerSwitch = view.findViewById(R.id.fingerprint_switch);
        portraitSwitch = view.findViewById(R.id.portrait_switch);
        saveButton = view.findViewById(R.id.settingsSaveButton);
        sharedPreferences = getActivity().getSharedPreferences(SettingsFragment.SETTINGS_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boldTextState = sharedPreferences.getBoolean("BoldTextState",false);
        darkModeState = sharedPreferences.getBoolean("DarkModeState",false);
        fingerPrintState = sharedPreferences.getBoolean("FingerprintState",false);
        screenOrientationState = sharedPreferences.getBoolean("ScreenLockState",false);
        faceLockState = sharedPreferences.getBoolean("FaceLockState",false);
        //TextViews
        tvBold = view.findViewById(R.id.tv_bold);
        tvColour = view.findViewById(R.id.tv_colour);
        tvFinger = view.findViewById(R.id.tv_finger);
        tvPortrait = view.findViewById(R.id.tv_Portrait);

        //Getting Switch states from sharedPreferences
        //Getting bold text switch state
        boldSwitch.setChecked(boldTextState);

        //Getting Dark Mode switch state
        colourSwitch.setChecked(darkModeState);
        //Getting fingerprint switch state
        fingerSwitch.setChecked(fingerPrintState);
        //Getting orientation switch state
        portraitSwitch.setChecked(screenOrientationState);

        //Bold Text
        boldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boldTextState = true;
                    tvBold.setTypeface(null, Typeface.BOLD);
                    tvColour.setTypeface(null, Typeface.BOLD);
                    boldSwitch.setTypeface(null, Typeface.BOLD);
                    colourSwitch.setTypeface(null, Typeface.BOLD);
                    fingerSwitch.setTypeface(null, Typeface.BOLD);
                    portraitSwitch.setTypeface(null, Typeface.BOLD);
                    tvFinger.setTypeface(null, Typeface.BOLD);
                    tvPortrait.setTypeface(null, Typeface.BOLD);
                    //HomeFragment.windowSwitch.setTypeface(null, Typeface.BOLD);
                } else {
                    boldTextState = false;
                    tvBold.setTypeface(null, Typeface.NORMAL);
                    tvColour.setTypeface(null, Typeface.NORMAL);
                    boldSwitch.setTypeface(null, Typeface.NORMAL);
                    colourSwitch.setTypeface(null, Typeface.NORMAL);
                    fingerSwitch.setTypeface(null, Typeface.NORMAL);
                    portraitSwitch.setTypeface(null, Typeface.NORMAL);
                    tvFinger.setTypeface(null, Typeface.NORMAL);
                    tvPortrait.setTypeface(null, Typeface.NORMAL);
                    //HomeFragment.windowSwitch.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        //Dark Mode
        colourSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                darkModeState = colourSwitch.isChecked();
            }
        });

        //Fingerprint enable/disable
        fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                fingerPrintState = fingerSwitch.isChecked();
            }
        });

        //Screen Orientation
        portraitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                screenOrientationState = portraitSwitch.isChecked();
            }
        });

    //Save Button functionality
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean("BoldTextState",boldTextState).apply();
                editor.putBoolean("DarkModeState",darkModeState).apply();
                editor.putBoolean("FingerprintState",fingerPrintState).apply();
                editor.putBoolean("ScreenLockState",screenOrientationState).apply();
                editor.putBoolean("FaceLockState",faceLockState).apply();
                editor.apply();
                editor.commit();
                if(darkModeState==true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}