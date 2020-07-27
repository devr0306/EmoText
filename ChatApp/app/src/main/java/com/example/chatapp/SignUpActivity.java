package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.Models.API.DefaultResponse;
import com.example.chatapp.Models.API.LoginResponse;
import com.example.chatapp.Models.API.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static Intent toMainActivity;

    //Username stuff
    private ConstraintLayout layout_for_username_fragment;
    private EditText nameET, usernameET, passwordET, confirmPasswordET;
    private Button signUpButton;

    private CheckBox rememberBox;
    
    //Email stuff
    private ConstraintLayout emailLayout;
    private RelativeLayout emailPromptLayout, emailCodePromptLayout;
    private Button emailEnterButton, emailCodeEnterButton;
    private EditText emailEnterEditText, emailCodeEnterEditText;
    
    private String email, username, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

   

    public void init(){

        //Username initializations
        initUserName();

        //Email initializations
        initEmail();
    }
    
    public void initEmail(){

        emailLayout = findViewById(R.id.email_and_code_signup_layout);
        emailPromptLayout = findViewById(R.id.layout_for_email_enter);
        emailCodePromptLayout = findViewById(R.id.layout_for_email_code);
        emailEnterButton = findViewById(R.id.email_enter_button);
        emailCodeEnterButton = findViewById(R.id.email_code_enter_button);
        emailEnterEditText = findViewById(R.id.email_prompt_edit_text);
        emailCodeEnterEditText = findViewById(R.id.email_code_edit_text);

        startEmailLayout();

        findViewById(R.id.go_back_to_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEmailLayout();
            }
        });
    }

    public void startEmailLayout(){

        emailLayout.setVisibility(View.VISIBLE);
        emailPromptLayout.setVisibility(View.VISIBLE);
        emailCodePromptLayout.setVisibility(View.GONE);
        layout_for_username_fragment.setVisibility(View.GONE);

        emailEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO uncomment the lines
                //if(!emailEnterEditText.getText().toString().equals("")){

                if(emailEnterEditText.getText().toString().isEmpty()){
                    emailEnterEditText.setError("Fill in the email");
                }

                else{
                    email = emailEnterEditText.getText().toString();
                    emailPromptLayout.setVisibility(View.GONE);
                    emailCodePromptLayout.setVisibility(View.VISIBLE);
                }

                //}
            }
        });

        emailCodeEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailCodeEnterEditText.getText().toString().isEmpty())
                    emailCodeEnterEditText.setError("Fill out the email code");

                else {
                    emailLayout.setVisibility(View.GONE);
                    layout_for_username_fragment.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    public void initUserName(){

        layout_for_username_fragment = findViewById(R.id.other_fields_signup_layout);
        nameET = findViewById(R.id.ask_name_edit_text);
        usernameET = findViewById(R.id.ask_username_edit_text);
        passwordET = findViewById(R.id.ask_password_edit_text);
        confirmPasswordET = findViewById(R.id.confirm_password_edit_text);
        signUpButton = findViewById(R.id.sign_up_button);
        rememberBox = findViewById(R.id.remember_me_checkbox_signup);

        findViewById(R.id.go_back_to_email_from_username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startEmailLayout();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO uncomment
                //if(!nameET.getText().toString().equals("") && !usernameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")
                //      && !confirmPasswordET.getText().toString().equals("")){

                if (usernameET.getText().toString().isEmpty())
                    usernameET.setError("Fill in the username");
                else
                    username = usernameET.getText().toString();

                if (nameET.getText().toString().isEmpty())
                    nameET.setError("Fill in the name");

                else
                    name = nameET.getText().toString();

                if (passwordET.getText().toString().isEmpty())
                    passwordET.setError("Fill in the password");

                else
                    password = passwordET.getText().toString();

                signUp();
            }
        });
    }

    public void signUp(){

        Call<DefaultResponse> signUpCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .signup(name, username, email, password);

        signUpCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                DefaultResponse dr;

                if(response.isSuccessful()) {
                    dr = response.body();

                    Toast.makeText(SignUpActivity.this, dr.getMessage(), Toast.LENGTH_LONG).show();
                    signIn();

                    goToNextActivity();

                }

                else {
                    try {

                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                //TODO- Change
                Toast.makeText(SignUpActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signIn(){

        Call<LoginResponse> loginCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .signin(email, password);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){

                    LoginResponse lr = response.body();

                    Log.d("Success", "Login Success: " + lr.getToken());

                    if(rememberBox.isChecked())
                        SharedPrefManager.getInstance(SignUpActivity.this).rememberUser();

                    getAndLoadUser(lr.getToken());
                }

                else {
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Error for login", t.toString());
            }
        });
    }

    public void getAndLoadUser(String token){

        Call<UserResponse> getUserCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .getUserFromToken(token);

        getUserCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful()){

                    UserResponse ur = response.body();

                    SharedPrefManager.getInstance(SignUpActivity.this).saveUser(ur.getUser());
                    SharedPrefManager.getInstance(SignUpActivity.this).login(token);
                }

                else {
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("Error for getUser", t.toString());
            }
        });
    }

    public void goToNextActivity(){

        toMainActivity = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(toMainActivity);
    }

    //TODO-Remove this and call
    /*public void changeFragments(Fragment fragment){


            signUpFragmentTransaction = signUpFragmentManager.beginTransaction();
            signUpFragmentTransaction.replace(R.id.layout_for_fragments, fragment);
            signUpFragmentTransaction.commit();

    }*/

}