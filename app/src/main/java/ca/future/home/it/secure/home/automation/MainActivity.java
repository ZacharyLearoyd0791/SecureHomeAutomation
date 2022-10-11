/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    //Fragments
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    public static DoorFragment doorFragment;
    public static TempFragment tempFragment;
    public static LightFragment lightFragment;
    public static WindowFragment windowFragment;
    private AccountFragment accountFragment;
    public static AddDeviceFragment addDeviceFragment;

    //Bottom Navigation
    public static BottomNavigationView bottomNav;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*

        //This is for testing purposes only. To test crashlytics
        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });


        addContentView(crashButton, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
        */

        //Bottom navigation and fragment views
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();
        doorFragment = new DoorFragment();
        tempFragment = new TempFragment();
        lightFragment = new LightFragment();
        windowFragment = new WindowFragment();
        accountFragment = new AccountFragment();
        addDeviceFragment = new AddDeviceFragment();

        //Sets initial startup screen to homeFragment
        if(!getIntent().getBooleanExtra(getString(R.string.recreated),false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        }

        //Switch between screens/fragments using bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.window:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, windowFragment).commit();
                    return true;
                case R.id.door:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, doorFragment).commit();
                    return true;
                case R.id.temp:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, tempFragment).commit();
                    return true;
                case R.id.light:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, lightFragment).commit();
                    return true;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                    return true;
            }
        });
    }

    //inflate action bar on first time opening app
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //action bar menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        bottomNav = findViewById(R.id.bottomNavigationView);
        if (item.getItemId() == R.id.ab_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, settingsFragment).commit();
        }
        if (item.getItemId() == R.id.ab_account) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, accountFragment).commit();
        }
        if (item.getItemId() == R.id.ab_refresh) {
            bottomNav.setSelectedItemId(R.id.home);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        }
        if (item.getItemId() == R.id.ab_add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addDeviceFragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

        builder
                .setMessage(R.string.leavingMsg)

                .setTitle(R.string.leavingTitle)
                .setIcon(R.drawable.exit_icon);
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        R.string.Yes,
                        (dialog, which) -> finish());

        builder
                .setNegativeButton(
                        R.string.No,
                        (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}