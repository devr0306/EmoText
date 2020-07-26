package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.ChatActivity;
import com.example.chatapp.Models.app.Contact;
import com.example.chatapp.R;

import java.util.ArrayList;

public class ChatTabRecyclerViewAdapter extends RecyclerView.Adapter<ChatTabRecyclerViewAdapter.ViewHolder> {

    ArrayList<Contact> contactsList = new ArrayList<>();
    Context contactContext;

    public ChatTabRecyclerViewAdapter(Context context){

        contactContext = context;
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

        holder.nameOfPerson.setText(contactsList.get(position).getName());

        Glide.with(contactContext)
                .asBitmap()
                .load(contactsList.get(position).getImageURL())
                .into(holder.imageOfPerson);

        holder.contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(contactContext, ChatActivity.class);
                chatIntent.putExtra("nameOfPerson", holder.nameOfPerson.getText().toString());
                chatIntent.putExtra("imageOfPerson", contactsList.get(position).getImageURL());

                contactContext.startActivity(chatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void setcontactsList(ArrayList<Contact> contactsList){
        this.contactsList = contactsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageOfPerson;
        private TextView nameOfPerson;
        private CardView contactCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPerson = itemView.findViewById(R.id.nameOfPerson);
            imageOfPerson = itemView.findViewById(R.id.image_of_person);
            contactCard = itemView.findViewById(R.id.contactCard);
        }
    }


}

