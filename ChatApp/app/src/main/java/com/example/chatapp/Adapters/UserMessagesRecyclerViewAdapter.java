package com.example.chatapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.API.Message;
import com.example.chatapp.R;
import com.example.chatapp.SharedPrefManager;

import java.util.ArrayList;

public class UserMessagesRecyclerViewAdapter extends RecyclerView.Adapter<UserMessagesRecyclerViewAdapter.MessageViewHolder> {

    ArrayList<Message> messagesList = new ArrayList<>();
    Context messageContext;

    public UserMessagesRecyclerViewAdapter(Context context){
        messageContext = context;
    }

    @NonNull
    @Override
    public UserMessagesRecyclerViewAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View messageBlockView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_block, parent, false);
        UserMessagesRecyclerViewAdapter.MessageViewHolder vh = new UserMessagesRecyclerViewAdapter.MessageViewHolder(messageBlockView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserMessagesRecyclerViewAdapter.MessageViewHolder holder, final int position) {

        Message message = messagesList.get(position);

        if(position != 0){

            Log.i("TestingChat", message.getText() + ": " + message.getDate().toString() + messagesList.get(position - 1).getDate().toString());
            Log.i("TestingChat", message.getText() + ": " + (message.getDate().compareTo(messagesList.get(position - 1).getDate()) == 0));
        }


        if(position == 0 || !(message.getDate().compareTo(messagesList.get(position - 1).getDate()) == 0)){

            holder.dateText.setText(message.getDateString());
            holder.dateText.setVisibility(View.VISIBLE);
        }

        if(message.getFromId().equals(SharedPrefManager.getInstance(messageContext).getUser().getId())){

            holder.messageText.setText(message.getText());
            holder.userMessageLayout.setVisibility(View.VISIBLE);
            holder.otherUserMessageLayout.setVisibility(View.GONE);

            holder.userMessageLayout.setBackground(messageContext.getDrawable(R.drawable.right_message_block));
            holder.outsideLayout.setGravity(Gravity.END);
        }

        else {

            holder.otherMessageText.setText(message.getText());
            holder.otherUserMessageLayout.setVisibility(View.VISIBLE);
            holder.userMessageLayout.setVisibility(View.GONE);

            holder.otherUserMessageLayout.setBackground(messageContext.getDrawable(R.drawable.left_message_block));
            holder.outsideLayout.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setMessagesList(ArrayList<Message> messagesList){
        this.messagesList = messagesList;
        notifyDataSetChanged();
    }

    public void addToMessagesList(Message m){
        this.messagesList.add(m);
        notifyDataSetChanged();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout outsideLayout;

        private TextView dateText;

        private RelativeLayout userMessageLayout, otherUserMessageLayout;
        private TextView messageText, otherMessageText;
        
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            outsideLayout = itemView.findViewById(R.id.outside_layout_for_message);

            dateText = itemView.findViewById(R.id.date_textview);

            userMessageLayout = itemView.findViewById(R.id.user_message_layout);
            messageText = itemView.findViewById(R.id.message_text);
            otherMessageText = itemView.findViewById(R.id.other_message_text);
            
            otherUserMessageLayout = itemView.findViewById(R.id.other_user_message_layout);
            otherMessageText = itemView.findViewById(R.id.other_message_text);
        }
    }
}
