package ca.future.home.it.secure.home.automation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailAddress;
    private EditText password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Creating references
        emailAddress = findViewById(R.id.login_page_email_textBox);
        password = findViewById(R.id.login_page_password_textBox);
        //Declarations
        TextView loginButton = findViewById(R.id.login_page_login_button);
        TextView createAccount = findViewById(R.id.loginCreateAccountHereTextClickable);
        TextView forgotPassword = findViewById(R.id.loginForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        //Login button functionality
        loginButton.setOnClickListener(view -> {
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
                   // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
        createAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegistrationActivity.class)));

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
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                            //Starting main activity
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


}