/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private String emailInput;
    private String passwordInput;
    private FirebaseAuth mAuth;
    private int fillChecker= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Referencing
        //Declaration
        Button registerButton = findViewById(R.id.Registration_page_done_button);
        fullName = findViewById(R.id.Registration_page_full_name_text);
        emailAddress = findViewById(R.id.Registration_page_email_text);
        password = findViewById(R.id.Registration_page_password_text);
        confirmPassword = findViewById(R.id.Registration_page_confirm_password_text);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(view -> loginProcess());
    }

    private void loginProcess(){
        //Assigning Values
        String nameInput = fullName.getText().toString();
        emailInput = emailAddress.getText().toString();
        passwordInput = password.getText().toString();
        String confirmPasswordInput = confirmPassword.getText().toString();

        //Checking if fields are empty or not
        if(fillChecker==1){
            if(nameInput.isEmpty()){
                fullName.setError(getString(R.string.enter_name));
                fullName.requestFocus();
            }
            if(emailInput.isEmpty()){
                emailAddress.setError(getString(R.string.email_address_enter));
            }
            if(passwordInput.isEmpty()){
                password.setError(getString(R.string.password_enter));
            }
            if(passwordInput.length()<8){
                password.setError(getString(R.string.minimum_eight_character));
                password.requestFocus();
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                emailAddress.setError(getString(R.string.enter_valid_email));
                emailAddress.requestFocus();
            }
            if(!passwordInput.matches(confirmPasswordInput)){
                confirmPassword.setError(getString(R.string.password_no_match));
                confirmPassword.requestFocus();
            }
            fillChecker = 0;
        }else{
            registrationProcess(fillChecker);
        }
    }
    public void registrationProcess(int CheckerId){
        if(CheckerId == 0){
            mAuth.createUserWithEmailAndPassword(emailInput,passwordInput)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, R.string.email_check, Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegistrationActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                        }else{
                            Toast.makeText(RegistrationActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}