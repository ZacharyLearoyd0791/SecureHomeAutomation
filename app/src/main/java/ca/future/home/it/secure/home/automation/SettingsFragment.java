/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (???) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

import ca.future.home.it.secure.home.automation.Application.SharedPref;

public class SettingsFragment extends Fragment {

    View view;
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
        RadioButton rbEnglish = view.findViewById(R.id.rb_english);
        RadioButton rbFrench = view.findViewById(R.id.rb_french);
        RadioGroup radioGroup = view.findViewById(R.id.rg_language);

        String langCode = SharedPref.read(SharedPref.KEY_LANGUAGE,getString(R.string.en_code));
        if (langCode.equals(getString(R.string.en_code))) {
            rbEnglish.setChecked(true);
        } else {
            rbFrench.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(rbEnglish.isChecked()) {
                    changeLanguage(getString(R.string.en_code));
                } else if (rbFrench.isChecked()) {
                    changeLanguage(getString(R.string.fr_code));
                }
            }
        });
    }


    private void changeLanguage(String langCode)
    {
        SharedPref.write(SharedPref.KEY_LANGUAGE,langCode);
        getActivity().getIntent().putExtra(getString(R.string.recreated),true);
        getActivity().recreate();
    }
}