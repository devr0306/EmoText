package com.example.chatapp.Fragments.Settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.R;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;
import com.example.chatapp.SharedPrefManager;
import com.example.chatapp.StartActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment{

    private View view;
    private CircleImageView profileImage;
    private TextView signOutText;
    private String name = "My Account";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        init();

        return view;
    }

    public void init(){

        name = "My Account";

        profileImage = view.findViewById(R.id.account_profile_image);
        signOutText = view.findViewById(R.id.sign_out_text);

        signOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutFromAccount();
                SharedPrefManager.getInstance(getContext()).clear();
                startActivity(new Intent(getContext(), StartActivity.class));
            }
        });
    }

    public void logoutFromAccount(){

        String token = SharedPrefManager.getInstance(getContext()).getUser().getToken();

        if(token != null){

            Call<DefaultResponse> logoutCall = AuthRetrofitClient
                    .getInstance()
                    .getAuthApi()
                    .logout(token);

            logoutCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                    if(response.isSuccessful()){

                        DefaultResponse dr = response.body();
                        assert dr != null;
                        Toast.makeText(getContext(), dr.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String getSettingName() {
        return name;
    }

    public void setSettingName(String name) {
        this.name = name;
    }
}