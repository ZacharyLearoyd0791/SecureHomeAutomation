package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.UiModeManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class espressoTest {

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

    @Test
    public void testDoorUnlocked() {
        onView(withId(R.id.bottomNavigationView)).perform(open());
        onView(withText("Door"));
        onView(withId(R.id.doorLockBtn)).perform(click());
        onView(withId(R.id.statusofDoor)).check(matches(withText("Unlocked")));
    }

    @Test
    public void testDoorLocked() {
        //onView(withId(R.id.bottomNavigationView)).perform(open());
        //onView(withText("Door"));
        onView(withId(R.id.bottomNavigationView)).perform(NavigationViewActions.navigateTo(R.id.door));
        onView(withId(R.id.doorLockBtn)).perform(click());
        onView(withId(R.id.statusofDoor)).check(matches(withText("Locked")));
    }

    @Test
    public void testLightOn() {
        //onView(withId(R.id.bottomNavigationView)).perform(open());
        onView(withId(R.id.bottomNavigationView)).perform(NavigationViewActions.navigateTo(R.id.door));
        onView(withId(R.id.onLights)).perform(click());
        onView(withId(R.id.statusOfLight)).check(matches(withText("Status of Light:On")));
    }

    @Test
    public void testLightOff() {
        //onView(withId(R.id.bottomNavigationView)).perform(open());
        onView(withId(R.id.bottomNavigationView)).perform(NavigationViewActions.navigateTo(R.id.door));
        onView(withId(R.id.onLights)).perform(click());
        onView(withId(R.id.statusOfLight)).check(matches(withText("Status of Light:Off")));
    }
}