package com.emotext.chatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emotext.chatapp.Adapters.PeopleTabRecyclerViewAdapter;
import com.emotext.chatapp.Models.app.User;
import com.emotext.chatapp.R;
import com.emotext.chatapp.ResponseObjects.ContactListResponse;
import com.emotext.chatapp.RetrofitClients.ContactsAPIClient;
import com.emotext.chatapp.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PeopleTabFragment extends Fragment {

    RecyclerView contactsRecyclerView;
    View contactFragmentView;
    private ArrayList<User> users;
    private TextView noFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contactFragmentView = inflater.inflate(R.layout.fragment_people_tab, container, false);

        init();
        setContacts();

        // Inflate the layout for this fragment
       return contactFragmentView;
    }

    public void init(){

        users = new ArrayList<User>();

        noFriends = contactFragmentView.findViewById(R.id.no_people_message);
        noFriends.setVisibility(View.GONE);

        contactsRecyclerView = contactFragmentView.findViewById(R.id.contact_recycler_view);

    }

    public void setContacts(){

        Call<ContactListResponse> getContactsCall = ContactsAPIClient
                .getInstance()
                .getContactsAPI()
                .getContacts(SharedPrefManager.getInstance(getContext()).getUser().getToken());

        getContactsCall.enqueue(new Callback<ContactListResponse>() {
            @Override
            public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {

                if(response.isSuccessful()){
                    ContactListResponse clr = response.body();

                    if (clr.getUsers() != null && clr.getUsers().length > 0) {

                        users = clr.getUserAsList();
                        PeopleTabRecyclerViewAdapter peopleTabRecyclerViewAdapter = new PeopleTabRecyclerViewAdapter(contactFragmentView.getContext());
                        peopleTabRecyclerViewAdapter.setUsersList(users);

                        contactsRecyclerView.setAdapter(peopleTabRecyclerViewAdapter);
                        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(contactFragmentView.getContext()));
                    }

                    else{

                        noFriends.setVisibility(View.VISIBLE);
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

                        Toast.makeText(getContext(), errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactListResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}