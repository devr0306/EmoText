package com.example.chatapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.app.Contact;
import com.example.chatapp.Models.app.User;
import com.example.chatapp.R;

import java.util.ArrayList;

public class AddPersonRecyclerViewAdapter extends RecyclerView.Adapter<AddPersonRecyclerViewAdapter.AddPeopleViewHolder>{

    ArrayList<User> contactsList = new ArrayList<>();
    Context contactContext;

    public AddPersonRecyclerViewAdapter(Context context){

        contactContext = context;
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

        Glide.with(contactContext)
                .asBitmap()
                .load(contactsList.get(position).getProfilePictureURL())
                .into(holder.imageOfPerson);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        public AddPeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfPerson = itemView.findViewById(R.id.add_name_of_person);
            imageOfPerson = itemView.findViewById(R.id.image_of_person_for_add);
            contactCard = itemView.findViewById(R.id.add_people_contact_card);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }
}
