/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    String  idKey, localKey, key, personalKey;
    String feedBackofUser;
    String ratingKey;
    String feedBackKey,userKey,userDetails;

    float ratingVal;


    //Fragments
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    @SuppressLint("StaticFieldLeak")
    public static DoorFragment doorFragment;
    @SuppressLint("StaticFieldLeak")
    public static TempFragment tempFragment;
    @SuppressLint("StaticFieldLeak")
    public static LightFragment lightFragment;
    @SuppressLint("StaticFieldLeak")
    public static WindowFragment windowFragment;
    @SuppressLint("StaticFieldLeak")
    public static ProfileEditFragment profileEditFragment;
    @SuppressLint("StaticFieldLeak")
    public static ForgotPasswordActivity forgotPasswordActivity;
    private final Handler handler = new Handler();
    ProgressBar progressBar;
    String time = "5";
    private AccountFragment accountFragment;
    public static AddDeviceFragment addDeviceFragment;
    Date date;
    DateFormat dateFormat;
    RatingBar ratingBar;
    Button saveBTN;
    String strDate,deviceModelKey;
    DatabaseReference databaseReference;
    EditText userFeedBack;
    //Fingerprint
    String fingerprintState = "";
    private int progressStatus = 0;
    UserInfo userInfo=new UserInfo();
    //Bottom Navigation
    public static BottomNavigationView bottomNav;

    @SuppressLint("NonConstantResourceId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfo.typeAccount();
        userDetails=getString(R.string.UserFeedback);
        userKey=getApplicationContext().getString(R.string.userKey);

        dbID();


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
        profileEditFragment = new ProfileEditFragment();
        forgotPasswordActivity = new ForgotPasswordActivity();

        //Sets initial startup screen to homeFragment
        if(!getIntent().getBooleanExtra(getString(R.string.recreated),false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        }

        //Switch between screens/fragments using bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.window:
                    //bottomNav.setSelectedItemId(R.id.window);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, windowFragment).commit();
                    return true;
                case R.id.door:
                    //bottomNav.setSelectedItemId(R.id.door);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, doorFragment).commit();
                    return true;
                case R.id.temp:
                    //bottomNav.setSelectedItemId(R.id.temp);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, tempFragment).commit();
                    return true;
                case R.id.light:
                    //bottomNav.setSelectedItemId(R.id.light);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, lightFragment).commit();
                    return true;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                    return true;
            }
        });

        //Fingerprint
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.fingerprint));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fingerprintState = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void dbID() {
        userInfo.typeAccount();
        localKey = userInfo.userId;
        personalKey = userInfo.idInfo;

        if (localKey != null) {
            key = localKey;

        }
        if (personalKey != null) {
            key = personalKey;
        }
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
            AsyncTaskRunner runner = new AsyncTaskRunner();
            String sleepTime = time;
            runner.execute(sleepTime);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        }
        if (item.getItemId() == R.id.ab_add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addDeviceFragment).commit();
        }
        if (item.getItemId() == R.id.ab_feedback) {
            showBottomDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress(getString(R.string.sleeping)); // Calls onProgressUpdate()
            String resp;
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = getString(R.string.sleptFor) + params[0] + getString(R.string.sec);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, R.string.refreshing, Toast.LENGTH_SHORT).show();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    getString(R.string.refresher),
                    getString(R.string.discription_dialog));
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feedback_layout);


        ratingBar = dialog.findViewById(R.id.ratingBar);
        saveBTN = dialog.findViewById(R.id.saveFeedback);
        userFeedBack = dialog.findViewById(R.id.feedbackET);
        progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {

            ratingVal = ratingBar.getRating();


            saveBTN.setOnClickListener(view -> {
                feedBackofUser = userFeedBack.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    while (progressStatus < 10) {
                        progressStatus += 1;
                        // Update the progress bar and display the
                        //current value in the text view
                        handler.post(() -> {
                            progressBar.setProgress(progressStatus);
                            if (progressStatus == 10) {
                                dialog.dismiss();
                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                databaseRatingInfo(ratingVal, feedBackofUser,getDeviceName());
            });
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void databaseRatingInfo(float ratingVal,String feedbackOfUser,String deviceModel) {
        time();

        ratingKey=userKey+key+userDetails+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+strDate;
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ratingKey);
        databaseReference.setValue(ratingVal);
        feedBackKey=userKey+key+userDetails+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+getString(R.string.feedback)+getString(R.string.forwardslash)+strDate;
        databaseReference = FirebaseDatabase.getInstance().getReference().child(feedBackKey);
        databaseReference.setValue(feedbackOfUser);
        deviceModelKey=userKey+key+userDetails+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+getString(R.string.deviceKey)+getString(R.string.forwardslash)+strDate;
        databaseReference = FirebaseDatabase.getInstance().getReference().child(deviceModelKey);
        databaseReference.setValue(deviceModel);

    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
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

    @SuppressLint("SimpleDateFormat")
    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat(getString(R.string.format));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
    }
}