package com.example.chatapp.Adapters;

import android.content.Context;
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
import com.example.chatapp.ResponseObjects.DefaultResponse;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.R;
import com.example.chatapp.RetrofitClients.ContactsAPIClient;
import com.example.chatapp.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPersonRecyclerViewAdapter extends RecyclerView.Adapter<AddPersonRecyclerViewAdapter.AddPeopleViewHolder>{

    ArrayList<User> contactsList = new ArrayList<>();
    Context addUserContext;

    public AddPersonRecyclerViewAdapter(Context context){

        addUserContext = context;
    }

    @NonNull
    @Override
    public AddPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View indCellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_people_recycler_view_ind_cells, parent, false);
        AddPeopleViewHolder vh = new AddPersonRecyclerViewAdapter.AddPeopleViewHolder(indCellView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddPeopleViewHolder holder, int position) {

        holder.nameOfPerson.setText(contactsList.get(position).getName());

        if(holder.isAdded)
            holder.addButton.setText("Added");

        Glide.with(addUserContext)
                .asBitmap()
                .load(contactsList.get(position).getProfilePictureURL())
                .into(holder.imageOfPerson);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(holder, position);
            }
        });
    }

    public void sendRequest(AddPeopleViewHolder holder, int position){

        Call<DefaultResponse> sendRequestCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .sendFriendRequest(SharedPrefManager.getInstance(addUserContext).getUser().getToken(), contactsList.get(position).getId());

        sendRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.isSuccessful()){

                    DefaultResponse dr = response.body();
                    Toast.makeText(addUserContext, dr.getMessage(), Toast.LENGTH_SHORT).show();

                    holder.isAdded = true;
                    holder.addButton.setText("Added");
                }

                else{
                    try {
                        String errorBody = response.errorBody().string();
                        int index = errorBody.indexOf("\"message\":");

                        if(index != -1){

                            String errorSub = errorBody.substring(index + 10);
                            errorBody = errorSub.substring(1, errorSub.length() - 2);
                        }

                        Toast.makeText(addUserContext, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(addUserContext, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void setcontactsList(ArrayList<User> contactsList){
        this.contactsList = contactsList;
        notifyDataSetChanged();
    }

    public class AddPeopleViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageOfPerson;
        private TextView nameOfPerson;
        private CardView contactCard;
        private Button addButton;

        private boolean isAdded;

        public AddPeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPerson = itemView.findViewById(R.id.add_name_of_person);
            imageOfPerson = itemView.findViewById(R.id.image_of_person_for_add);
            contactCard = itemView.findViewById(R.id.add_people_contact_card);
            addButton = itemView.findViewById(R.id.add_button);

            isAdded = false;
        }
    }
}
