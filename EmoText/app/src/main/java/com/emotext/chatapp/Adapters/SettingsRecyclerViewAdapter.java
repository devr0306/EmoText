package com.emotext.chatapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.emotext.chatapp.R;
import com.emotext.chatapp.SettingsActivity;

import java.util.ArrayList;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.SettingsViewHolder> {

    ArrayList<Fragment> fragmentList;
    ArrayList<String> fragmentNames;
    Context context;

    public SettingsRecyclerViewAdapter(Context cont){

        context = cont;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_ind_cells, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    //TODO- Doesn't show text
    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {

        holder.setSettingText(fragmentNames.get(position));

        holder.settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsActivity sa = new SettingsActivity();
                sa.changeFragment(fragmentList.get(position));
            }
        });
    }

    public void setLists(ArrayList<Fragment> list, ArrayList<String> fragNames){

        fragmentList = list;
        fragmentNames = fragNames;

        Log.d("names size", fragmentNames.toString());
        notifyDataSetChanged();
    }


    public class SettingsViewHolder extends RecyclerView.ViewHolder{

        private TextView settingsTextView;
        private RelativeLayout settingsLayout;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            settingsTextView = itemView.findViewById(R.id.settings_ind_textview);
            settingsLayout = itemView.findViewById(R.id.settings_ind_layout);
        }

        public void setSettingText(String text){
            settingsTextView.setText(text);
        }
    }
}
