/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-???
Harpreet Cheema (???) - CENG-322-???
Krushang Parekh (???) - CENG-322-???
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.ContentView;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ActivityTestRule$$ExternalSyntheticLambda0;
import androidx.test.runner.AndroidJUnit4;
import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

import com.google.android.material.checkbox.MaterialCheckBox;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Rule
    public FragmentTestRule<?, SettingsFragment> fragmentTestRule = FragmentTestRule.create(SettingsFragment.class);

    @Test
    public void TestDarkModeAndCheck() {
        //onView(withContentDescription(getString(R.string.settings))).perform(click());
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //onView(withId(R.id.ab_settings)).perform(click());
        onView(withId(R.id.colour_switch)).perform(click());
        onView(withId(R.id.settingsSaveButton)).perform(click());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if(UiModeManager.MODE_NIGHT_YES==1)
        {
            Log.d(TAG, "Dark mode enabled");
        }
        else
            Log.d(TAG, "Dark mode disabled");

    }
}