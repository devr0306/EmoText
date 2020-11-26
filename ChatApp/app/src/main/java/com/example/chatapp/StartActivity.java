package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

//TODO remove this gradient
/*android:startColor="#FAE60C"
  android:centerColor="#F1C234"
  android:endColor="#E2FA91B4"*/

public class StartActivity extends AppCompatActivity {

    private ConstraintLayout sign_up_ConstraintLayout, sign_in_ConstraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(SharedPrefManager.getInstance(StartActivity.this).isLoggedIn())
            startActivity(new Intent(StartActivity.this, MainActivity.class));

        initValues();
    }

    public void initValues(){

        sign_in_ConstraintLayout = findViewById(R.id.sign_in_button_constraintLayout);
        sign_up_ConstraintLayout = findViewById(R.id.sign_up_button_constraintLayout);

        sign_in_ConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId() == sign_in_ConstraintLayout.getId()){

                    sign_in_ConstraintLayout.setBackgroundColor(Color.parseColor("#EFAFC4"));
                    
                    Intent toSignInActivity = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(toSignInActivity);

                    sign_in_ConstraintLayout.setBackgroundColor(Color.parseColor("#0000ff00"));

                }
            }
        });

        sign_up_ConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId() == sign_up_ConstraintLayout.getId()){

                    sign_up_ConstraintLayout.setBackgroundColor(Color.parseColor("#EFAFC4"));

                    Intent toSignUpActivity = new Intent(StartActivity.this, SignUpActivity.class);
                    startActivity(toSignUpActivity);

                    sign_up_ConstraintLayout.setBackgroundColor(Color.parseColor("#0000ff00"));

                }
            }
        });
    }
}