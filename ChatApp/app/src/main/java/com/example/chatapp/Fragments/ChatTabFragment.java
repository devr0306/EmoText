package com.example.chatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chatapp.Models.app.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Adapters.ChatTabRecyclerViewAdapter;
import com.example.chatapp.SettingsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatTabFragment extends Fragment {

    RecyclerView contactsRecyclerView;
    View contactFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactFragmentView = inflater.inflate(R.layout.fragment_chat_tab, container, false);

        init();

        // Inflate the layout for this fragment
       return contactFragmentView;

    }

    public void init(){

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        contacts.add(new Contact("Dev", "https://i.ytimg.com/vi/c7oV1T2j5mc/maxresdefault.jpg"));
        contacts.add(new Contact("Rey", "https://www.tom-archer.com/wp-content/uploads/2017/03/landscape-photography-tom-archer-2.jpg"));
        contacts.add(new Contact("Delphius", "https://iso.500px.com/wp-content/uploads/2017/10/PhotographingTwilight_TannerWendellStewart-218136823.jpg"));

        contactsRecyclerView = contactFragmentView.findViewById(R.id.contact_recycler_view);

        contactFragmentView.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });

        ChatTabRecyclerViewAdapter chatTabRecyclerViewAdapter = new ChatTabRecyclerViewAdapter(contactFragmentView.getContext());
        chatTabRecyclerViewAdapter.setcontactsList(contacts);

        contactsRecyclerView.setAdapter(chatTabRecyclerViewAdapter);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(contactFragmentView.getContext()));

    }
}