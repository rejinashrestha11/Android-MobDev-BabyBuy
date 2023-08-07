package com.example.babybuy.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babybuy.Database.BabyBuyDb;
import com.example.babybuy.R;

public class LoginActivity extends AppCompatActivity {
    EditText Eemail, Epassword;
    Button Blogin, Bsignup;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BabyBuyDb db = new BabyBuyDb(this);
        Eemail = findViewById(R.id.emailInput);
        Epassword = findViewById(R.id.passwordInput);
        Blogin = findViewById(R.id.loginBtn);
        Bsignup = findViewById(R.id.btnSignup);

        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Converted edittext to string
                email = Eemail.getText().toString();
                password = Epassword.getText().toString();


                //if condition for null value in edittext
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter username and Password", Toast.LENGTH_SHORT).show();
                }

                //Email pattern and password length checked
                else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    //created boolean variable for pop up message when login success or not
                    boolean i = db.checkemailandpassword(email, password);
                    if (i == true) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent ilogin = new Intent(LoginActivity.this, DashboardActivity.class);
                       // ilogin.putExtra("Email", email);
                        getemail();
                        startActivity(ilogin);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                    }
                }


                //if email pattern is wrong show this message
                else {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email ", Toast.LENGTH_LONG).show();
                    Eemail.setError(" Valid email is required");
                }
            }
        });

        Bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    public void getemail() {
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("email",email );
        Ed.apply();
    }
}
