/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (???) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    //Bottom navigation and fragment views
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private DoorFragment doorFragment;
    private TempFragment tempFragment;
    private LightFragment lightFragment;
    private WindowFragment windowFragment;

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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();
        doorFragment = new DoorFragment();
        tempFragment = new TempFragment();
        lightFragment = new LightFragment();
        windowFragment = new WindowFragment();

        //Sets initial startup screen to homeFragment
        if(!getIntent().getBooleanExtra(getString(R.string.recreated),false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        }

        //Switch between screens/fragments using bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, settingsFragment).commit();
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
        switch (item.getItemId()){
            case R.id.ab_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, settingsFragment).commit();
                break;

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