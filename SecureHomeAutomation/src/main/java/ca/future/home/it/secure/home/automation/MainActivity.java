/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    UserInfo userInfo=new UserInfo();
    String localKey,key,personalKey,light_lightstatus,idKey,feedBackofUser,ratingKey,feedBackKey;

    float ratingVal;


    //Fragments
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    public static DoorFragment doorFragment;
    public static TempFragment tempFragment;
    public static LightFragment lightFragment;
    public static WindowFragment windowFragment;
    public static ProfileEditFragment profileEditFragment;
    String time ="5";
    private AccountFragment accountFragment;
    public static AddDeviceFragment addDeviceFragment;
    Date date;
    DateFormat dateFormat;
    RatingBar ratingBar;
    Button saveBTN;
    String strDate;
    //Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText userFeedBack;
    String ratingx;
    //Fingerprint
    String fingerprintState="";

    //Bottom Navigation
    public static BottomNavigationView bottomNav;

    @SuppressLint("NonConstantResourceId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbID();
        getFCM();
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
        profileEditFragment = new ProfileEditFragment();

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

    private void idToDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.idLightStatus));
        databaseReference.setValue(idKey);

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
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
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
            Toast.makeText(MainActivity.this, "Items Refreshed!", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Refreshing items",
                    "It won't take much time :)");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout rating=dialog.findViewById(R.id.ratings);
        LinearLayout feedback=dialog.findViewById(R.id.feedback);
        LinearLayout save =dialog.findViewById(R.id.saveFeedBackLL);
        ratingBar=dialog.findViewById(R.id.ratingBar);
        saveBTN=dialog.findViewById(R.id.saveFeedback);
        userFeedBack=(EditText) dialog.findViewById(R.id.feedbackET);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                ratingVal=ratingBar.getRating();


                saveBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedBackofUser=userFeedBack.getText().toString();

                        dialog.dismiss();

                        databaseRatingInfo(ratingVal,feedBackofUser);

                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void databaseRatingInfo(float ratingVal,String feedbackOfUser) {

        time();
        ratingKey=getString(R.string.forwardslash)+key+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+strDate;
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ratingKey);
        databaseReference.setValue(ratingVal);
        feedBackKey=key+getString(R.string.forwardslash)+getString(R.string.rating)+getString(R.string.forwardslash)+getString(R.string.feedback)+getString(R.string.forwardslash)+strDate;
        databaseReference = FirebaseDatabase.getInstance().getReference().child(feedBackKey);
        databaseReference.setValue(feedbackOfUser);



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

    private void getFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, getString(R.string.log_fcm), task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = (getString(R.string.fcm_token)) + token;
                    Log.d(TAG, msg);
                });
    }
    private void dbID(){
        userInfo.typeAccount();
        time();

        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
            Log.d(TAG,key);

        }
        if(personalKey!=null) {
            key= personalKey;
            Log.d(TAG, key);
        }
        light_lightstatus=key+getString(R.string.statusKey);
        idKey=key+getString(R.string.statusKey);
        idToDatabase();
    }
    private void time(){
        date = Calendar.getInstance().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date);
        }
    }
}