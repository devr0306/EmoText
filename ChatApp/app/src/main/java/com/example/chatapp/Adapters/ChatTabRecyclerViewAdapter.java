package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.ChatActivity;
import com.example.chatapp.MainActivity;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatTabRecyclerViewAdapter extends RecyclerView.Adapter<ChatTabRecyclerViewAdapter.ViewHolder> {

    ArrayList<User> usersList = new ArrayList<>();
    Context userContext;

    public ChatTabRecyclerViewAdapter(Context context){

        userContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View indCellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_tab_recycler_view_ind_cells, parent, false);
        ViewHolder vh = new ViewHolder(indCellView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.nameOfPerson.setText(usersList.get(position).getName());

        String profileUrl = usersList.get(position).getProfilePictureURL();

        if(profileUrl == null || profileUrl.length() < 1)
            holder.imageOfPerson.setImageResource(R.drawable.ic_launcher_background);

        else{
            Glide.with(userContext)
                    .asBitmap()
                    .load(profileUrl)
                    .into(holder.imageOfPerson);
        }

        holder.UserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(userContext, ChatActivity.class);
                chatIntent.putExtra("nameOfPerson", holder.nameOfPerson.getText().toString());
                chatIntent.putExtra("imageOfPerson", usersList.get(position).getProfilePictureURL());

                userContext.startActivity(chatIntent);
                ((MainActivity)userContext).overridePendingTransition(R.anim.slide_in_left, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setusersList(ArrayList<User> usersList){
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageOfPerson;
        private TextView nameOfPerson;
        private CardView UserCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPerson = itemView.findViewById(R.id.nameOfPerson);
            imageOfPerson = itemView.findViewById(R.id.image_of_person);
            UserCard = itemView.findViewById(R.id.contactCard);
        }
    }


}

