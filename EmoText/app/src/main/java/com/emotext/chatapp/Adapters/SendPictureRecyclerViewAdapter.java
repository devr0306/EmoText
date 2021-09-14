package com.emotext.chatapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.emotext.chatapp.Models.app.User;
import com.emotext.chatapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendPictureRecyclerViewAdapter extends RecyclerView.Adapter<SendPictureRecyclerViewAdapter.SendPictureViewHolder> {

    private ArrayList<User> friends;
    private ArrayList<User> receiversList;

    private Context context;

    public SendPictureRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public SendPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_picture_ind_cells, parent, false);

        SendPictureViewHolder sendPictureViewHolder = new SendPictureViewHolder(v);
        return sendPictureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SendPictureViewHolder holder, int position) {

        holder.name.setText(friends.get(position).getName());

        String profileUrl = friends.get(position).getProfilePictureURL();

        if(profileUrl == null || profileUrl.length() < 1)
            holder.profilePic.setImageResource(R.drawable.ic_launcher_background);

        else{
            Glide.with(context)
                    .asBitmap()
                    .load(profileUrl)
                    .into(holder.profilePic);
        }

        holder.sentView.setVisibility(View.GONE);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.sentView.getVisibility() == View.GONE){

                    holder.sentView.setVisibility(View.VISIBLE);
                    receiversList.add(friends.get(position));
                }

                else {

                    holder.sentView.setVisibility(View.GONE);
                    receiversList.remove(friends.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setFriendsList(ArrayList<User> friendsList){

        friends = friendsList;
        notifyDataSetChanged();

        receiversList = new ArrayList<>();
    }


    public class SendPictureViewHolder extends RecyclerView.ViewHolder {

        private CardView mainLayout;
        private CircleImageView profilePic;
        private TextView name;
        private View sentView;

        public SendPictureViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.send_picture_layout);
            profilePic = itemView.findViewById(R.id.send_picture_profile_image);
            name = itemView.findViewById(R.id.send_picture_name);
            sentView = itemView.findViewById(R.id.selected_item_view);
        }
    }
}
