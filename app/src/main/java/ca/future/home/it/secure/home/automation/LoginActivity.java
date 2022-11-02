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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    //Custom login
    private EditText emailAddress;
    private EditText password;
    private FirebaseAuth mAuth;
    public static String PREFS_NAME = "LoggedInFile";
    public static String LOGGED_IN_METHOD_EMAIL = "LoggedInMethodEmail";
    public static String LOGGED_IN_METHOD_GOOGLE = "LoggedInMethodGoogle";
    public static String LOGGED_IN_METHOD_FACEBOOK= "LoggedInMethodFacebook";
    Switch fingerPrintSwitch;
    protected boolean loggedInWithCutomEmail = false;
    protected boolean loggedInWithGoogle = false;
    protected boolean loggedInWithFacebook = false;
    //Google login
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    //Facebook login
    private ImageView facebookButton;
    CallbackManager callbackManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Creating references
        emailAddress = findViewById(R.id.login_page_email_textBox);
        password = findViewById(R.id.login_page_password_textBox);
        fingerPrintSwitch = findViewById(R.id.fingerprint_switch);
//        fingerPrintSwitch.setChecked(false);
        //Declarations
        ImageView googleLogoButton = findViewById(R.id.google_logo);
        TextView loginButton = findViewById(R.id.login_page_login_button);
        TextView createAccount = findViewById(R.id.loginCreateAccountHereTextClickable);
        TextView forgotPassword = findViewById(R.id.loginForgotPassword);
        mAuth = FirebaseAuth.getInstance();         //Getting firebase instance
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        facebookButton = findViewById(R.id.facebook_logo);
        callbackManager = CallbackManager.Factory.create();


        //Login button functionality
        loginButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasLoggedIn",true);
            editor.commit();




            String emailInput = emailAddress.getText().toString();
            String passwordInput = password.getText().toString();
            if(emailInput.isEmpty() && passwordInput.isEmpty()){
                Toast.makeText(LoginActivity.this, R.string.enter_something, Toast.LENGTH_SHORT).show();
            } else if (emailInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            } else if (passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.enterpassword, Toast.LENGTH_SHORT).show();
            } else {
                boolean emailValidation = validateEmailInput(emailAddress);
                if (emailValidation) {
                    loginUser(emailInput, passwordInput);
                }
            }
        });

        //For opening Registration page
        createAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegistrationActivity.class)));

        //For opening Forgot password page
        forgotPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));

        //For google sign in process
        googleLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInProcess();
            }
        });

        //Login via fingerprint
//        if(fingerPrintSwitch.isChecked()) {
//            BiometricManager biometricManager= BiometricManager.from(this);
//            switch(biometricManager.canAuthenticate()){
//                case BiometricManager.BIOMETRIC_SUCCESS:
//                    Toast.makeText(this, "You can use the fingerprint sensor to login", Toast.LENGTH_SHORT).show();
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                    Toast.makeText(this, "This device don't have fingerprint sensor", Toast.LENGTH_SHORT).show();
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                    Toast.makeText(this, "Biometric sensor currently unavailable", Toast.LENGTH_SHORT).show();
//                    break;
//                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                    Toast.makeText(this, "No fingerprint found!", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//
//            Executor executor = ContextCompat.getMainExecutor(this);
//            final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
//                @Override
//                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                    super.onAuthenticationError(errorCode, errString);
//                }
//
//                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
//                @Override
//                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                    super.onAuthenticationSucceeded(result);
//                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//                }
//
//                @Override
//                public void onAuthenticationFailed() {
//                    super.onAuthenticationFailed();
//                }
//            });
//            // creating a variable for our promptInfo
//            // BIOMETRIC DIALOG
//            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
//                    .setDescription("Use your fingerprint to login ")
//
//                    .setNegativeButtonText("Cancel").build();
//
//            biometricPrompt.authenticate(promptInfo);
//        }

        //Login via facebook
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        loggedInWithFacebook = true;
                        SharedPreferences sharedPreferencesFacebook = getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_FACEBOOK,0);
                        SharedPreferences.Editor editorF = sharedPreferencesFacebook.edit();
                        editorF.putBoolean("LoggedInMethodFacebook",loggedInWithFacebook);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Error: "+exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });


    }
    //Validating Email address
    private boolean validateEmailInput(EditText email) {
        String emailInput = emailAddress.getText().toString();
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailAddress.setTextColor(Color.BLACK);
            return true;
        } else{
            Toast.makeText(this, R.string.email_valid, Toast.LENGTH_SHORT).show();
            emailAddress.setTextColor(Color.RED);
            return false;
        }
    }
    //Login User
    private void loginUser(String emailInput, String passwordInput) {
        mAuth.signInWithEmailAndPassword(emailInput,passwordInput)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_LONG).show();
                            //Starting main activity
                            loggedInWithCutomEmail = true;
                            SharedPreferences sharedPreferencesEmail = getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_EMAIL,0);
                            SharedPreferences.Editor editorE = sharedPreferencesEmail.edit();
                            editorE.putBoolean("LoggedInMethodEmail",loggedInWithCutomEmail);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(LoginActivity.this, R.string.emailverify, Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });




    }

   // Google Sign in process
    public void googleSignInProcess(){
        Intent googleSignInIntent = gsc.getSignInIntent();
        startActivityForResult(googleSignInIntent,1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);    //used for facebook
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                changingActivity();
            } catch (ApiException e) {
                Log.d("Exeception: ", String.valueOf(e));
                Toast.makeText(this, R.string.google_signin_error, Toast.LENGTH_SHORT).show();
            }
        }


    }
    public void changingActivity(){
        finish();
        loggedInWithGoogle =true;
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        loggedInWithGoogle =true;
        SharedPreferences sharedPreferencesGoogle = getSharedPreferences(LoginActivity.LOGGED_IN_METHOD_GOOGLE,0);
        SharedPreferences.Editor editorG = sharedPreferencesGoogle.edit();
        editorG.putBoolean("LoggedInMethodGoogle",loggedInWithGoogle);
    }


}