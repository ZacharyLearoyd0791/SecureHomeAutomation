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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    CheckBox rememberMeCheckBox;

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
        rememberMeCheckBox = findViewById(R.id.rememberMe);

        //Login button functionality
        loginButton.setOnClickListener(view -> {
            if(rememberMeCheckBox.isChecked()) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.has_logged_in), true);
                editor.commit();
            }

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

        //Login via facebook
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, getString(R.string.error)+exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(getString(R.string.public_profile)));
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
                Log.d(getString(R.string.exception), String.valueOf(e));
                Toast.makeText(this, R.string.google_signin_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changingActivity(){
        finish();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }
}