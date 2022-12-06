package ca.future.home.it.secure.home.automation;

import android.widget.EditText;

import com.facebook.login.Login;

import org.junit.Assert;
import org.junit.Test;

public class ValidateLoginCredentials {
    @Test
    public void test_password_input_fail() {
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.isValidPassword("Krushang002"));
    }

    @Test
    public void test_password_input_true(){
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.isValidPassword("Krushang!@#002"));
    }
    LoginActivity validator = new LoginActivity();
    EditText email= validator.findViewById(R.id.login_page_email_textBox);

    @Test
    public void test_email_true(){
        email.setText("krushang002@gmail.com");
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.validateEmailInput(email));
    }
    @Test
    public void test_email_false(){
        email.setText("krushang");
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.validateEmailInput(email));
    }
    @Test
    public void test_email_false_2(){
        email.setText("@krushang");
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.validateEmailInput(email));
    }
    @Test
    public void test_email_true_2(){
        email.setText("krushang002@gmail.com");
        LoginActivity validator = new LoginActivity();
        Assert.assertTrue(validator.validateEmailInput(email));
    }





}
