package com.emotext.chatapp.Fragments.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emotext.chatapp.Adapters.SettingsRecyclerViewAdapter;
import com.emotext.chatapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private View view;

    private RecyclerView settings;
    private CircleImageView profilePic;

    private ArrayList<Fragment> settingsList;
    private ArrayList<String> settingsNamesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        init();

        return view;
    }

    public void init(){

        profilePic = view.findViewById(R.id.big_profile_image);
        settings = view.findViewById(R.id.settings_recycler_view);

        settingsList = new ArrayList<>();
        settingsNamesList = new ArrayList<>();
        fillLists();

        SettingsRecyclerViewAdapter settingsAdapter = new SettingsRecyclerViewAdapter(getContext());
        settingsAdapter.setLists(settingsList, settingsNamesList);

        settings.setAdapter(settingsAdapter);
        settings.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void fillLists(){

        AccountFragment accountFragment = new AccountFragment();
        settingsList.add(accountFragment);
        settingsNamesList.add(accountFragment.getSettingName());

    }
}