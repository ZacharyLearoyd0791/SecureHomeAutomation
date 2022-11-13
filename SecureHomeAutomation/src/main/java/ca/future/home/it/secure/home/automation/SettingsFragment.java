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
    private Boolean boldTextState = false;
    private Boolean darkModeState = false;
    private Boolean fingerPrintState = false;
    private Boolean screenOrientationState = false;
    private Boolean sBoldTextState;
    private Boolean sDarkModeState;
    private Boolean sFingerPrintState;
    private Boolean sScreenOrientationState;
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
        sBoldTextState = sharedPreferences.getBoolean(getString(R.string.BoldTextString),false);
        sDarkModeState = sharedPreferences.getBoolean(getString(R.string.DarkModeString),false);
        sFingerPrintState = sharedPreferences.getBoolean(getString(R.string.fingerPrintString),false);
        sScreenOrientationState = sharedPreferences.getBoolean(getString(R.string.screenOrientationString),false);

        //TextViews
        tvBold = view.findViewById(R.id.tv_bold);
        tvColour = view.findViewById(R.id.tv_colour);
        tvFinger = view.findViewById(R.id.tv_finger);
        tvPortrait = view.findViewById(R.id.tv_Portrait);

        //Getting Switch states from sharedPreferences
        //Getting bold text switch state
        if(sBoldTextState){
            boldSwitch.setChecked(true);
        }else{
            boldSwitch.setChecked(false);
        }

        //Getting Dark Mode switch state
        if(sDarkModeState){
            colourSwitch.setChecked(true);
        }else{
            colourSwitch.setChecked(false);
        }

        //Getting fingerprint switch state
        if(sFingerPrintState){
            fingerSwitch.setChecked(true);
        }else{
            fingerSwitch.setChecked(false);
        }

        //Getting orientation switch state
        if(sScreenOrientationState){
            portraitSwitch.setChecked(true);
        }else{
            portraitSwitch.setChecked(false);
        }

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
                if (isChecked) {
                    darkModeState = true;

                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    darkModeState = false;
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        //Fingerprint enable/disable
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.settings));
        fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(fingerSwitch.isChecked()){
                    fingerPrintState = true;
                }else{
                    fingerPrintState = false;
                }
            }
        });

        //Screen Orientation
        if(portraitSwitch.isChecked()){
            screenOrientationState = true;
        }else{
            screenOrientationState = false;
        }


    //Save Button functionality
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean(getString(R.string.BoldTextString),boldTextState).apply();
                editor.putBoolean(getString(R.string.DarkModeString),darkModeState).apply();
                editor.putBoolean(getString(R.string.fingerPrintString),fingerPrintState).apply();
                editor.putBoolean(getString(R.string.screenOrientationString),screenOrientationState).apply();

                if(darkModeState==true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}