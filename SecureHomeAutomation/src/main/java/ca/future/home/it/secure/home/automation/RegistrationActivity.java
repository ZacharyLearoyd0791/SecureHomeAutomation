/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText password;
    private EditText confirmPassword;
    private String emailInput;
    private String phoneNumberInput;
    private String passwordInput;
    private String nameInput;
    private FirebaseAuth mAuth;
    private int fillChecker= 1;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        phoneNumber = findViewById(R.id.Registration_page_phone_number_text);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("User Data", MODE_PRIVATE);
        editor=sharedPreferences.edit();

        registerButton.setOnClickListener(view -> loginProcess());
    }

    private void loginProcess(){
        //Assigning Values
        nameInput = fullName.getText().toString();
        emailInput = emailAddress.getText().toString();
        passwordInput = password.getText().toString();
        String confirmPasswordInput = confirmPassword.getText().toString();
        phoneNumberInput = phoneNumber.getText().toString();

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
            if(phoneNumberInput.length()<10){
                phoneNumber.setError(getString(R.string.phone_number_error_message));
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                emailAddress.setError(getString(R.string.enter_valid_email));
                emailAddress.requestFocus();
            }
            if(!passwordInput.matches(confirmPasswordInput)){
                confirmPassword.setError(getString(R.string.password_no_match));
                confirmPassword.requestFocus();
            }if(!isValidPassword(passwordInput)) {
                password.setError(getString(R.string.password_error_message));
            }
            fillChecker = 0;
        }else{
            registrationProcess(fillChecker);
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-8])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void registrationProcess(int CheckerId){
        editor.putString("NewUserName",nameInput);
        editor.putString("NewUserEmail",emailInput);
        editor.putString("NewUserPhone",phoneNumberInput);
        editor.apply();
        editor.commit();
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.user_details));
        UserHelperClass helperClass = new UserHelperClass(nameInput,emailInput,phoneNumberInput,passwordInput);
        databaseReference.child(phoneNumberInput).setValue(helperClass);
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