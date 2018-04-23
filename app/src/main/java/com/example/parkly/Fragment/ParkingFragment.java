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

        List<String> greenZones = new ArrayList<>();
        List<String> blueZones = new ArrayList<>();
        List<String> redZones = new ArrayList<>();
        List<String> yellowZones = new ArrayList<>();
        List<String> orangeZones = new ArrayList<>();

        /*
        zones.add("Green 0.30€/h");
        zones.add("Blue 0.60€/h");
        zones.add("Red 1.20€/h");
        zones.add("Yellow 2€/h");
        zones.add("Orange 2€/h");
         */
        zoneList.add("Green 0.30€/h");
        zoneList.add("Blue 0.60€/h");
        zoneList.add("Red 1.20€/h");
        zoneList.add("Yellow 2€/h");
        zoneList.add("Orange 2€/h");

        greenZones.add("Kaunakiemio g.");
        greenZones.add("Šiaulių g.");
        greenZones.add("Girstupio g.");
        greenZones.add("Bažnyčios g.");
        greenZones.add("Karo Ligoninės g.");
        greenZones.add("Trakų g.");
        greenZones.add(" Savanorių pr.");
        greenZones.add("Tvirtovės al.");

        blueZones.add("Savanorių pr.");
        blueZones.add("Vytauto pr.");
        blueZones.add("Totorių g.");
        blueZones.add("Trakų g.");
        blueZones.add("Krėvos g.");
        blueZones.add("I. Kanto g.");
        blueZones.add("Nemuno g.");
        blueZones.add("D. Poškos g.");
        blueZones.add("Puodžių g.");
        blueZones.add("Šilutės g.");
        blueZones.add("Smalininkų g.");
        blueZones.add("Druskininkų g.");
        blueZones.add("Tvirtovės al.");
        blueZones.add("Sukilėlių pr.");
        blueZones.add("Iniciatorių g.");
        blueZones.add("V.Lašo g.");
        blueZones.add("Lankos g.");
        blueZones.add("Klonio g.");
        blueZones.add("Dusetų g.");
        blueZones.add("A. Jasaičio g.");
        blueZones.add("Eivenių g.");
        blueZones.add("K. Genio g.");
        blueZones.add("Sukilėlių pr.");
        blueZones.add("Lazūnų g.");
        blueZones.add("Ūmėdžių g.");
        blueZones.add("Ruduokių g.");
        blueZones.add("J. Lukšos-Daumanto g.");
        blueZones.add("Žeimentos g.");
        blueZones.add("Šv. Gertrūdos g.");
        blueZones.add("Birštono g.");
        blueZones.add("Karaliaus Mindaugo pr.");
        blueZones.add("Palangos g.");
        blueZones.add("Kurpių g.");
        blueZones.add("Vilniaus g.");
        blueZones.add("Gimnazijos g.");
        blueZones.add("J. Jablonskio g.");
        blueZones.add("A Mapu g.");
        blueZones.add("L. Zamenhofo g.");
        blueZones.add("Muitinės g.");
        blueZones.add("M. Daukšos g.");
        blueZones.add("J. Naugardo g.");
        blueZones.add("Muitinės g.");
        blueZones.add("V. Sladkevičiaus g.");
        blueZones.add("Šaukliū g.");
        blueZones.add("Jonavos g.");
        blueZones.add("A. Jakšto g.");
        blueZones.add("Kumelių g.");
        blueZones.add("V. Kuzmos g.");
        blueZones.add("Aleksoto g.");
        blueZones.add("Jėzuitų skg.");
        blueZones.add("Prieplaukos krant.");
        blueZones.add("T. Daugirdo g.");
        blueZones.add("Santakos g.");
        blueZones.add("Muziejaus g.");
        blueZones.add("Bernardinų skg.");
        blueZones.add("M. Valančiaus g.");
        blueZones.add("Raguvos g.");
        blueZones.add("Pilies g.");
        blueZones.add("Papilio g.");
        blueZones.add("Rotušės a.");

        redZones.add("Gedimino g.");
        redZones.add("Griunvaldo g.");
        redZones.add("Miško g.");
        redZones.add("Vaidilutės g.");
        redZones.add("Kęstučio g.");
        redZones.add("Vytauto pr.");
        redZones.add("Laisvės al.");
        redZones.add("K. Donelaičio g.");
        redZones.add("M. Dobužinskio g.");
        redZones.add("V. Putvinskio g.");

        yellowZones.add("Gedimino g.");
        yellowZones.add("A. Mickevičiaus g.");
        yellowZones.add("Spaustuvininkų g.");
        yellowZones.add("Kęstučio g.");
        yellowZones.add(" S. Daukanto g.");
        yellowZones.add("Maironio g.");
        yellowZones.add("Laisvės al.");
        yellowZones.add("K. Donelaičio g.");
        yellowZones.add("Vasario 16-osios g.");
        yellowZones.add("J. Gruodžio g.");
        yellowZones.add("I. kanto g.");
        yellowZones.add("L. Sapiegos g.");
        yellowZones.add("E. Ožeškienės g.");
        yellowZones.add("V. Putvinskio g.");
        yellowZones.add("Aušros tak.");

        orangeZones.add("Rotušės a.");

        addressList.put(zoneList.get(0), greenZones);
        addressList.put(zoneList.get(1), blueZones);
        addressList.put(zoneList.get(2), redZones);
        addressList.put(zoneList.get(3), yellowZones);
        addressList.put(zoneList.get(4), orangeZones);
    }

}