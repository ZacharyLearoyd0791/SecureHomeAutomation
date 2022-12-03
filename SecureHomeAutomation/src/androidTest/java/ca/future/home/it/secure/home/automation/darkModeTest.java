package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.UiModeManager;
import android.util.Log;
import com.android21buttons.fragmenttestrule.FragmentTestRule;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import ca.future.home.it.secure.home.automation.SettingsFragment;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class darkModeTest {

    @Rule
    public FragmentTestRule<?, SettingsFragment> fragmentTestRule = FragmentTestRule.create(SettingsFragment.class);

    @Test
    public void testDarkModeAndCheck() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withId(R.id.ab_settings)).perform(click());
        onView(withId(R.id.colour_switch)).perform(click());
        onView(withId(R.id.settingsSaveButton)).perform(click());

        //Same functionality, two different ways of testing
        //Test 1
        if((AppCompatDelegate.MODE_NIGHT_YES)==1)
        {
            Log.d(TAG, "Dark mode enabled");
        }
        else
            Log.d(TAG, "Dark mode disabled");

        //Test 2
        if(UiModeManager.MODE_NIGHT_YES==1)
        {
            Log.d(TAG, "Dark mode enabled");
        }
        else
            Log.d(TAG, "Dark mode disabled");
    }
}