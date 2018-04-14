package com.example.parkly.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.parkly.Adapters.ExpandableListAdapter;
import com.example.parkly.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marius on 2018-03-12.
 */

public class ParkingFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parking_fragment, null);
    }

    private ExpandableListView lst_zones;
    private ExpandableListAdapter adapter;
    private List<String> zoneList;
    private HashMap<String, List<String>> addressList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lst_zones = view.findViewById(R.id.expl_parking);

        //Init data
        prepareData();

        adapter = new ExpandableListAdapter(this.getContext(),zoneList,addressList);


        lst_zones.setAdapter(adapter);
    }

    private void prepareData()
    {
        zoneList = new ArrayList<>();
        addressList = new HashMap<>();

        List<String> emptyList = new ArrayList<>();
        emptyList.add("");

        zoneList.add("Green");
        zoneList.add("Blue");
        zoneList.add("Red");
        zoneList.add("Yellow");
        zoneList.add("Orange");

        addressList.put(zoneList.get(0), emptyList);
        addressList.put(zoneList.get(1), emptyList);
        addressList.put(zoneList.get(2), emptyList);
        addressList.put(zoneList.get(3), emptyList);
        addressList.put(zoneList.get(4), emptyList);
    }

}