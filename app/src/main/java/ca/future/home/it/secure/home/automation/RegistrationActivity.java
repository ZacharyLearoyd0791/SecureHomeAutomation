package ca.future.home.it.secure.home.automation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
   //Declaration
    private Button registerButton;
    private EditText fullName;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private String emailInput;
    private String nameInput;
    private String passwordInput;
    private String confirmPasswordInput;
    private FirebaseAuth mAuth;
    private int fillChecker= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Referencing
        registerButton = findViewById(R.id.Registration_page_done_button);
        fullName = findViewById(R.id.Registration_page_full_name_text);
        emailAddress = findViewById(R.id.Registration_page_email_text);
        password = findViewById(R.id.Registration_page_password_text);
        confirmPassword = findViewById(R.id.Registration_page_confirm_password_text);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProcess();
            }
        });
    }

    private void loginProcess(){
        //Assigning Values
        nameInput = fullName.getText().toString();
        emailInput = emailAddress.getText().toString();
        passwordInput = password.getText().toString();
        confirmPasswordInput = confirmPassword.getText().toString();

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
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RegistrationActivity.this, R.string.email_check, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(RegistrationActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            }else{
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}