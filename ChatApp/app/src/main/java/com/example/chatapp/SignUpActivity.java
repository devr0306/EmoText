package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.ResponseObjects.LoginResponse;
import com.example.chatapp.ResponseObjects.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static Intent toMainActivity;
    private static final int GALLERY_REQUEST_CODE = 22;
    private static final int TAKE_PHOTO_REQUEST_CODE = 23;

    //Username stuff
    private ConstraintLayout layout_for_username_fragment;
    private EditText nameET, usernameET, passwordET, confirmPasswordET;
    private Button signUpButton;

    private ImageView enterPasswordLockButton, confirmPasswordLockButton;
    boolean isEnterLocked, isConfirmLocked;

    private CheckBox rememberBox;

    private CircleImageView profilePic;
    private CardView layoutForPicButtons;
    
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
                    email = emailEnterEditText.getText().toString().trim();
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

        profilePic = findViewById(R.id.set_profile_pic_button);
        layoutForPicButtons = findViewById(R.id.layout_for_pic_buttons);
        layoutForPicButtons.setVisibility(View.GONE);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutForPicButtons.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.gallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
                photoPickIntent.setType("image/*");
                startActivityForResult(photoPickIntent, GALLERY_REQUEST_CODE);
            }
        });

        findViewById(R.id.take_picture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
            }
        });

        isEnterLocked = true;
        enterPasswordLockButton = findViewById(R.id.unlock_button_for_password_enter);
        enterPasswordLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEnterLocked){
                    passwordET.setTransformationMethod(null);
                    enterPasswordLockButton.setImageResource(R.drawable.ic_lock_open);
                    passwordET.setSelection(passwordET.getText().length());
                }

                else{
                    passwordET.setTransformationMethod(new PasswordTransformationMethod());
                    enterPasswordLockButton.setImageResource(R.drawable.ic_lock);
                    passwordET.setSelection(passwordET.getText().length());
                }

                isEnterLocked = !isEnterLocked;
            }
        });

        isConfirmLocked = true;
        confirmPasswordLockButton = findViewById(R.id.unlock_button_for_password_confirm);
        confirmPasswordLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConfirmLocked){
                    confirmPasswordET.setTransformationMethod(null);
                    confirmPasswordLockButton.setImageResource(R.drawable.ic_lock_open);
                    confirmPasswordET.setSelection(confirmPasswordET.getText().length());
                }

                else{
                    confirmPasswordET.setTransformationMethod(new PasswordTransformationMethod());
                    confirmPasswordLockButton.setImageResource(R.drawable.ic_lock);
                    confirmPasswordET.setSelection(confirmPasswordET.getText().length());
                }

                isConfirmLocked = !isConfirmLocked;
            }
        });

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
                    username = usernameET.getText().toString().trim();

                if (nameET.getText().toString().isEmpty())
                    nameET.setError("Fill in the name");

                else
                    name = nameET.getText().toString().trim();

                if (passwordET.getText().toString().isEmpty())
                    passwordET.setError("Fill in the password");

                else
                    password = passwordET.getText().toString().trim();

                signUp();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            switch (requestCode){

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        profilePic.setImageBitmap(bitmap);
                        layoutForPicButtons.setVisibility(View.GONE);

                        SharedPrefManager.getInstance(SignUpActivity.this).setProfilePic(convertBitToString(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case TAKE_PHOTO_REQUEST_CODE:

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(photo);

                    try {
                        SharedPrefManager.getInstance(this).setProfilePic(convertBitToString(photo));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public String convertBitToString(Bitmap bitmap) throws UnsupportedEncodingException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return  MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Title",null);
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

                        if (index != -1) {

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                //TODO- Change
                Toast.makeText(SignUpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String errorBody = response.errorBody().string();
                                int index = errorBody.indexOf("\"message\":");

                                if (index != -1) {

                                    String errorSub = errorBody.substring(index + 10);
                                    errorBody = errorSub.substring(1, errorSub.length() - 2);
                                }

                                Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
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

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String errorBody = response.errorBody().string();
                                int index = errorBody.indexOf("\"message\":");

                                if (index != -1) {

                                    String errorSub = errorBody.substring(index + 10);
                                    errorBody = errorSub.substring(1, errorSub.length() - 2);
                                }

                                Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
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
}