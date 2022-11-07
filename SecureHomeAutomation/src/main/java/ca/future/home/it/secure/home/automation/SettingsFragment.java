/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment {

    //Switches

    private Switch boldSwitch;
    private Switch colourSwitch;
    private Switch fingerSwitch;
    private Switch portraitSwitch;

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

        //Switches
        boldSwitch = view.findViewById(R.id.bold_switch);
        colourSwitch = view.findViewById(R.id.colour_switch);
        fingerSwitch = view.findViewById(R.id.fingerprint_switch);
        portraitSwitch = view.findViewById(R.id.portrait_switch);

        //TextViews
        tvBold = view.findViewById(R.id.tv_bold);
        tvColour = view.findViewById(R.id.tv_colour);
        tvFinger = view.findViewById(R.id.tv_finger);
        tvPortrait = view.findViewById(R.id.tv_Portrait);

        //Bold Text
        boldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                    //mainAct.changeColour(1);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        //Fingerprint enable/disable
        databaseReference = FirebaseDatabase.getInstance().getReference("Settings");
        fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(fingerSwitch.isChecked()){
                    databaseReference.child("Fingerprint").setValue("On").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Fingerprint Enabled successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    databaseReference.child("Fingerprint").setValue("Off").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Fingerprint Disabled successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}