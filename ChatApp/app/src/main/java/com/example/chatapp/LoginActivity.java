package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.ResponseObjects.LoginResponse;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.ResponseObjects.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLoginET, passwordLoginET;
    private CheckBox rememberMeBox;
    private Button loginButton;
    private ImageView unlockButton;
    private boolean isLocked;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    public void init(){

        emailLoginET = findViewById(R.id.email_edit_text);
        passwordLoginET = findViewById(R.id.password_edit_text);

        isLocked = true;
        unlockButton = findViewById(R.id.unlock_button_for_password);
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLocked){
                    passwordLoginET.setTransformationMethod(null);
                    unlockButton.setImageResource(R.drawable.ic_lock_open);
                    passwordLoginET.setSelection(passwordLoginET.getText().length());
                }

                else{
                    passwordLoginET.setTransformationMethod(new PasswordTransformationMethod());
                    unlockButton.setImageResource(R.drawable.ic_lock);
                    passwordLoginET.setSelection(passwordLoginET.getText().length());
                }

                isLocked = !isLocked;
            }
        });

        rememberMeBox = findViewById(R.id.remember_me_checkbox);

        loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(emailLoginET.getText().toString().isEmpty())
                    emailLoginET.setError("Fill in the email");

                else
                    email = emailLoginET.getText().toString().trim();

                if (passwordLoginET.getText().toString().isEmpty())
                    passwordLoginET.setError("Fill in the password");

                else
                    password = passwordLoginET.getText().toString().trim();

                //TODO uncomment
                if(!emailLoginET.getText().toString().isEmpty() && !passwordLoginET.getText().toString().isEmpty()){
                    signIn();
                }
            }
        });
    }

    //TODO- Doesn't work
    public void signIn(){

        Call<LoginResponse> loginCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .signin(email, password);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                //TODO- Doesn't show "User Doesn't exist"
                if(response.isSuccessful()){

                    LoginResponse lr = response.body();

                    if(rememberMeBox.isChecked())
                        SharedPrefManager.getInstance(LoginActivity.this).rememberUser();

                    getAndLoadUser(lr.getToken());

                    Toast.makeText(LoginActivity.this, lr.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent toMainFromLogin = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toMainFromLogin);

                }

                else {
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAndLoadUser(String token){

        User user = null;
        Call<UserResponse> getUserCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .getUserFromToken(token);

        getUserCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful()){

                    UserResponse ur = response.body();

                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(ur.getUser());
                    SharedPrefManager.getInstance(LoginActivity.this).login(token);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}