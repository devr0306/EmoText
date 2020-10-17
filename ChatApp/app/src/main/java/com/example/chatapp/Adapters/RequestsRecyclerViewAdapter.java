package com.example.chatapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.API.FriendRequest;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.R;
import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.ResponseObjects.UserResponse;
import com.example.chatapp.RetrofitClients.AuthRetrofitClient;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;
import com.example.chatapp.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.RequestsViewHolder>{

    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
    private User user;

    Context contactContext;

    public RequestsRecyclerViewAdapter(Context context){

        contactContext = context;
    }

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View indCellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_recycler_view_ind_cell, parent, false);
        RequestsViewHolder vh = new RequestsViewHolder(indCellView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestsViewHolder holder, final int position) {

        getPerson(friendRequests.get(position).getUserFrom(), holder);

        holder.acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(holder, position);
                friendRequests.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                denyRequest(holder, position);
                friendRequests.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.exitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest(holder, position);
                friendRequests.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public void getPerson(String id, RequestsViewHolder holder){

        Call<UserResponse> getUserCall = AuthRetrofitClient
                .getInstance()
                .getAuthApi()
                .getUserById(id);

        getUserCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful()){

                    UserResponse ur = response.body();
                    user = ur.getUser();

                    holder.nameOfPerson.setText(user.getName());

                    if(user.getProfilePictureURL() == null || user.getProfilePictureURL().length() < 1)
                        holder.imageOfPerson.setImageResource(R.drawable.ic_launcher_background);


                    else{
                        Glide.with(contactContext)
                                .asBitmap()
                                .load(user.getProfilePictureURL())
                                .into(holder.imageOfPerson);
                    }
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(contactContext, errorBody, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(contactContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void acceptRequest(RequestsViewHolder holder, int position){

        Call<DefaultResponse> acceptRequestCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .acceptFriendRequest(SharedPrefManager.getInstance(contactContext).getUser().getToken(), friendRequests.get(position).getId());

        acceptRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.isSuccessful()){

                    DefaultResponse dr = response.body();
                    Toast.makeText(contactContext, dr.getMessage(), Toast.LENGTH_SHORT).show();
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(contactContext, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(contactContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void denyRequest(RequestsViewHolder holder, int position){

        Call<DefaultResponse> declineRequestCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .declineFriendRequest(SharedPrefManager.getInstance(contactContext).getUser().getToken(), friendRequests.get(position).getId());

        declineRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.isSuccessful()){

                    DefaultResponse dr = response.body();
                    Toast.makeText(contactContext, dr.getMessage(), Toast.LENGTH_SHORT).show();
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(contactContext, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(contactContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelRequest(RequestsViewHolder holder, int position){

        Call<DefaultResponse> ignoreRequestCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .ignoreFriendRequest(SharedPrefManager.getInstance(contactContext).getUser().getToken(), friendRequests.get(position).getId());

        ignoreRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.isSuccessful()){

                    DefaultResponse dr = response.body();
                    Toast.makeText(contactContext, dr.getMessage(), Toast.LENGTH_SHORT).show();
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(contactContext, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(contactContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public void setLists(ArrayList<FriendRequest> friendRequests){
        this.friendRequests = friendRequests;
        notifyDataSetChanged();

    }

    public class RequestsViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageOfPerson;
        private TextView nameOfPerson, denyButton;
        private CardView contactCard;
        private Button acceptRequestButton;
        private ImageView exitActivity;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPerson = itemView.findViewById(R.id.request_name_of_person);
            imageOfPerson = itemView.findViewById(R.id.image_of_person_for_request);
            contactCard = itemView.findViewById(R.id.request_people_contact_card);
            acceptRequestButton = itemView.findViewById(R.id.accept_request_button);
            denyButton = itemView.findViewById(R.id.reject_button);
            exitActivity = itemView.findViewById(R.id.cross_button);
        }
    }
}
