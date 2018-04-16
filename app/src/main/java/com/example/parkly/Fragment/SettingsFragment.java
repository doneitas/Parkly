package com.example.parkly.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.parkly.R;

/**
 * Created by Marius on 2018-03-12.
 */

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action:");
        menu.add(Menu.NONE, 0, Menu.NONE, "Mark as default");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
        //menu.add(Menu.NONE, 0, Menu.NONE, "DELETE");
    }



}
