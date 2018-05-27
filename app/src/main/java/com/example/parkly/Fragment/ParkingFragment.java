package com.example.parkly.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

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

    private Context mContext;
    private ExpandableListView lst_zones;
    private ExpandableListAdapter adapter;
    private List<String> zoneList;
    private HashMap<String, List<String>> addressList;
    private int searchTextLength = 0;
    SearchView search;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lst_zones = view.findViewById(R.id.expl_parking);

        //Init data
        prepareData();

        mContext = this.getContext();
        adapter = new ExpandableListAdapter(mContext,zoneList,addressList);

        search = view.findViewById(R.id.sview_parking);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });

        lst_zones.setAdapter(adapter);
        lst_zones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    final String address = addressList.get(zoneList.get(groupPosition)).get(childPosition);

                    String getLocationAlert = getString(R.string.get_location_alert);

                    new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                            .setMessage(getLocationAlert + " " + address + " ?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("geo:0,0?q="+address+"+Kaunas"));
                                    startActivity(intent);
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                    return true;
                }

                return false;
            }
        });

        SearchView searchView = view.findViewById(R.id.sview_parking);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  String newTrimmedText = trimText(newText);
                  if (newTrimmedText != null && !newTrimmedText.isEmpty())
                  {
                      // trimint reikia newText, tai šioje vietoje metodas turėtų gražinti patrimintą newText
                      if (searchTextLength > newTrimmedText.length()) {
                          prepareData();
                          searchTextLength = newTrimmedText.length();
                      }
                      else searchTextLength = newTrimmedText.length();

                      ArrayList<String> tempList = new ArrayList<>();
                      for (int i = 0; i < 5; i++) {
                          for (String item : addressList.get(zoneList.get(i))) {
                              if (item.toUpperCase().contains(newTrimmedText.toUpperCase()))
                              {
                                  tempList.add(item);
                              }
                          }
                          addressList.get(zoneList.get(i)).clear();
                          addressList.get(zoneList.get(i)).addAll(tempList);
                          tempList.clear();
                      }
                      adapter = new ExpandableListAdapter(mContext,zoneList,addressList);
                      lst_zones.setAdapter(adapter);
                      lst_zones.expandGroup(0);
                      lst_zones.expandGroup(1);
                      lst_zones.expandGroup(2);
                      lst_zones.expandGroup(3);
                      lst_zones.expandGroup(4);
                      adapter.notifyDataSetChanged();
                  }
                  else
                  {
                      prepareData();
                      adapter = new ExpandableListAdapter(mContext,zoneList,addressList);
                      lst_zones.setAdapter(adapter);
                      adapter.notifyDataSetChanged();
                  }
                  return true;
              }
          }
        );
    }

    public String trimText(String newText)
    {
        return newText.trim().replaceAll(" +", " ");
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

        String Green = getString(R.string.green_zone_areas);
        String Blue = getString(R.string.blue_zone_areas);
        String Red = getString(R.string.red_zone_areas);
        String Yellow = getString(R.string.yellow_zone_areas);
        String Orange = getString(R.string.orange_zone_areas);

        zoneList.add(Green);
        zoneList.add(Blue);
        zoneList.add(Red);
        zoneList.add(Yellow);
        zoneList.add(Orange);

        greenZones.add("Kaunakiemio g.");
        greenZones.add("Šiaulių g.");
        greenZones.add("Girstupio g.");
        greenZones.add("Bažnyčios g.");
        greenZones.add("Karo Ligoninės g.");
        greenZones.add("Trakų g.");
        greenZones.add("Savanorių pr.");
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
        yellowZones.add("S. Daukanto g.");
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