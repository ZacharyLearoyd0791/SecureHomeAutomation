/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Variable declaration
    private EditText userEmail;
    private Button sendEmailButton;
    private String userEmailInput;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initializing variable
        userEmail = findViewById(R.id.forgotPasswordEmailAddressEnter);
        sendEmailButton = findViewById(R.id.forgotPasswordSendButton);
        firebaseAuth = FirebaseAuth.getInstance();

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmailInput = userEmail.getText().toString();
                if(userEmailInput.isEmpty()){
                    userEmail.setError(getString(R.string.enter_email_add));
                    userEmail.requestFocus();
                }else{
                    boolean emailValidation = validateEmailInput(userEmail);
                    if(emailValidation){
                        sendEmailToResetPassword(userEmail);
                    }
                }
            }
        });
    }

    private void sendEmailToResetPassword(EditText emailAddress) {
        firebaseAuth.sendPasswordResetEmail(emailAddress.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, R.string.email_send, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateEmailInput(EditText userEmail) {
        String emailInput = userEmail.getText().toString();
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            userEmail.setTextColor(Color.BLACK);
            return true;
        } else{
            Toast.makeText(this, R.string.enter_valid, Toast.LENGTH_SHORT).show();
            userEmail.setTextColor(Color.RED);
            return false;
        }
    }
}