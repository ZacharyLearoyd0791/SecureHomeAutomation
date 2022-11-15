/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static ca.future.home.it.secure.home.automation.R.string.Auth_Failed;
import static ca.future.home.it.secure.home.automation.R.string.Auth_error;
import static ca.future.home.it.secure.home.automation.R.string.Auth_succeed;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class SplashScreenActivity extends AppCompatActivity {

    UserInfo userInfo= new UserInfo();
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    Intent intent;
    Uri data;
    String onLights, param,key,localKey,personalKey,lightKey,sensorKey,on,off,offLights;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbID();
        loginActivity();
        shortCuts();

    }

    private void loginActivity() {
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.R)
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences settingsSharedPreferences = getSharedPreferences(SettingsFragment.SETTINGS_PREFS_NAME, MODE_PRIVATE);

                    boolean hasLoggedIn = sharedPreferences.getBoolean(getString(R.string.hasLoggedin), true);
                    boolean settingsEnableFingerPrint = settingsSharedPreferences.getBoolean(getString(R.string.fingerprintstate), false);
                    if (settingsEnableFingerPrint) {

                        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());

                        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

                            case BiometricManager.BIOMETRIC_SUCCESS:
//                            Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                                break;

                            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                            Log.e("MY_APP_TAG", "No biometric features available on this device.");
                                break;

                            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                            Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                                break;

                            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                                // Prompts the user to create credentials that your app accepts.
                                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                                startActivityForResult(enrollIntent, 101);
                                break;
                        }

                        executor = ContextCompat.getMainExecutor(getApplicationContext());
                        biometricPrompt = new BiometricPrompt(SplashScreenActivity.this,
                                executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode,
                                                              @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                Toast.makeText(getApplicationContext(),
                                                getString(Auth_error) + errString, Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onAuthenticationSucceeded(
                                    @NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);

                                Toast.makeText(getApplicationContext(),
                                        R.string.Auth_succeed, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                Toast.makeText(getApplicationContext(), R.string.Auth_Failed,
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                .setTitle(getString(R.string.Biometric_title))
                                .setSubtitle(getString(R.string.Biometric_subtitle))
                                .setNegativeButtonText(getString(R.string.Biometric_negitiveBtn))
                                .build();

                        biometricPrompt.authenticate(promptInfo);
                        if (hasLoggedIn) {

                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            finish();
                        }

                    } else {
                        if (hasLoggedIn) {

                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            finish();
                        }
                    }


                }
            }, 3000);

        }

    private void shortCuts() {

        intent = getIntent();
        data = intent.getData();

        on=getString(R.string.on);
        off=getString(R.string.off);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(lightKey);
        if(data!=null){
            param=intent.getData().getQueryParameter("statusType");
            //Log.d(TAG,"Test_Google_Assistance: V2, open close test\t"+param);

            if (param.equalsIgnoreCase(on)){
                databaseReference.setValue(on);
                //Log.d(TAG,"Test_Google_Assistance: V2, open close test\t"+param);

            }
            else if (param.equalsIgnoreCase(off)){
                databaseReference.setValue(off);
                //Log.d(TAG,"Test_Google_Assistance: V2, open close test\t"+param);

            }
            else{
               // Log.d(TAG,"Test_Google_Assistance: V2, open close test param grabbed but error\t"+param);
            }
        }
        else{
            //Log.d(TAG,"Test_Google_Assistance: V2, open close test is null");
        }
    }

    private void dbID() {
        userInfo.typeAccount();

        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
            Log.d(ContentValues.TAG,key);
        }
        if(personalKey!=null) {
            key= personalKey;
            Log.d(ContentValues.TAG, key);
        }
        lightKey=key+getString(R.string.statusKey);
        Log.d(ContentValues.TAG,getString(R.string.keyIs)+lightKey);
        sensorKey=key+getString(R.string.db_ultrasonic_dist);
    }

}
