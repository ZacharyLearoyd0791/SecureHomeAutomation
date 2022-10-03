package ca.future.home.it.secure.home.automation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    //Declarations
    private Button loginButton;
    private EditText emailAddress;
    private EditText password;
    private TextView createAccount;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Creating references
        emailAddress = findViewById(R.id.login_page_email_textBox);
        password = findViewById(R.id.login_page_password_textBox);
        loginButton = findViewById(R.id.login_page_login_button);
        createAccount = findViewById(R.id.loginCreateAccountHereTextClickable);
        forgotPassword = findViewById(R.id.loginForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        //Login button functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = emailAddress.getText().toString();
                String passwordInput = password.getText().toString();
                if(emailInput.isEmpty() && passwordInput.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter something!", Toast.LENGTH_SHORT).show();
                } else if (emailInput.isEmpty() && !passwordInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!emailInput.isEmpty() && passwordInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    boolean emailValidation = validateEmailInput(emailAddress);
                    if (emailValidation == true) {
                        loginUser(emailInput, passwordInput);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }
            }


        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
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
            Toast.makeText(this, "Enter Valid Email!", Toast.LENGTH_SHORT).show();
            emailAddress.setTextColor(Color.RED);
            return false;
        }
    }
    //Login User
    private void loginUser(String emailInput, String passwordInput) {
        mAuth.signInWithEmailAndPassword(emailInput,passwordInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                //Starting main activity
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            }else{
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}