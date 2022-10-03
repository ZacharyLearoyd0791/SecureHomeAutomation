package ca.future.home.it.secure.home.automation;

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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    //Declarations
    private Button loginButton;
    private EditText emailAddress;
    private EditText password;
    private TextView createAccount;
    private TextView forgotPassword;

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
                      //  loginUser(emailInput, passwordInput);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }



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
}