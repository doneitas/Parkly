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
import com.example.parkly.Adapters.ExpandableListAdapter2;
import com.example.parkly.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marius on 2018-03-12.
 */

public class AboutFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, null);
    }

    private Context mContext;
    private ExpandableListView lst_questions;
    private ExpandableListAdapter2 adapter;
    private List<String> questionList;
    private HashMap<String, List<String>> addressList;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lst_questions = view.findViewById(R.id.list_QnA);

        //Init data
        prepareData();

        mContext = this.getContext();
        adapter = new ExpandableListAdapter2(mContext,questionList,addressList);

        lst_questions.setAdapter(adapter);
    }

    public String trimText(String newText)
    {
        return newText.trim().replaceAll(" +", " ");
    }



    private void prepareData()
    {
        questionList = new ArrayList<>();
        addressList = new HashMap<>();

        List<String> atsNr1 = new ArrayList<>();
        List<String> atsNr2 = new ArrayList<>();
        List<String> atsNr3 = new ArrayList<>();
        List<String> atsNr4 = new ArrayList<>();
        List<String> atsNr5 = new ArrayList<>();
        List<String> atsNr6 = new ArrayList<>();
        List<String> atsNr7 = new ArrayList<>();
        List<String> atsNr8 = new ArrayList<>();
        List<String> atsNr9 = new ArrayList<>();

        String Klausimas1 = "Kam yra skirta PARKLY programėlė?";
        String Klausimas2 = "Kaip yra vykdomas apmokėjimas už stovėjimą?";
        String Klausimas3 = "Kokiuose miestuose galiu naudotis šia programėle?";
        String Klausimas4 = "Kaip nustatyti automobilį naudojamu?";
        String Klausimas5 = "Kaip galiu pamatyti gatvės realią vietą žemėlapių programėlėje?";
        String Klausimas6 = "Kaip galiu pašalinti įvestus automobilių valstybinius numerius?";
        String Klausimas7 = "Kaip sužinoti tam tikros stovėjimo zonos nemokamą stovėjimo laiką?";
        String Klausimas8 = "Kaip reguliuoti pranešimus apie artėjančią stovėjimo pabaigą?";
        String Klausimas9 = "Koks yra automatiškai generuojamos žinutės formatas?";

        questionList.add(Klausimas1);
        questionList.add(Klausimas2);
        questionList.add(Klausimas3);
        questionList.add(Klausimas4);
        questionList.add(Klausimas5);
        questionList.add(Klausimas6);
        questionList.add(Klausimas7);
        questionList.add(Klausimas8);
        questionList.add(Klausimas9);

        atsNr1.add("PARKLY programėlė skirta atsiskaitymo už stovėjimą palengvinimui t.y. programėlė pati sugeneruoja ir reikiamu numeriu išsiunčia SMS žinutę, patvirtinančią ir apmokančią stovėjimą.");
        atsNr2.add("Apmokėjimas yra vykdomas paspaudus PATVIRTINTI mygtuką, tai padarius yra automatiškai išsiunčiama sugeneruota SMS žinutė numeriu 1332 ir stovėjimas yra apmokamas iš vartotojo mobiliojo ryšio sąskaitos.");
        atsNr3.add("Šiuo metu programėlė yra skirta tik Kauno miesto teritorijai.");
        atsNr4.add("Norint nustatyti automobilį naudojamu galima rinktis vieną iš dviejų kelių:\n\n1. Atverti skiltį pavadinimu AUTOMOBILIAI, paspausti ir palaikyti ant norimo automobilio valstybinio numerio ir spustelti NUSTATYTI KAIP NAUDOJAMĄ.\n\n2. Atverti skiltį pavadinimu PAGRINDINIS ir pasirinkti norimą automobilį, išskleidus jų sąrašą.");
        atsNr5.add("Norint pamatyti realią gatvės vietą žemėlapių programėlėje, tereikia atverti skiltį pavadinimu STOVĖJIMO ZONOS, paspausti ir palaikyti ant norimos gatvės pavadinimo ir pasirinkti žemėlapių programėlę, kurią norima naudoti (Pasirinkti galima tik tada, kai telefone yra įdiegta daugiau nei viena žemėlapių programėlė).");
        atsNr6.add("Norint pašalinti automobilių valstybinius numerius, galima rinktis vieną iš dviejų kelių:\n\n1. Atverti skiltį pavadinimu AUTOMOBILIAI, paspausti ir palaikyti ant pasirinkto automobilio ir spustelti PAŠALINTI.\n\n2. Atverti skiltį pavadinimu AUTOMOBILIAI, paspausti mygtuką PAŠALINTI ir pažymėti automobilių valstybinius numerius, kuriuos norima pašalinti. ");
        atsNr7.add("Norint sužinoti stovėjimo zonos nemokamą stovėjimo laiką, tereikia atverti skiltį pavadinimu STOVĖJIMO ZONOS - šalia kiekvienos stovėjimo zonos galima matyti savaitės dienas ir laiką, kada stovėjimas yra mokamas.");
        atsNr8.add("Reguliuoti pranešimus galima skiltyje pavadinimu NUSTATYMAI, pranešimams yra skirti du jungikliai, vienas (pirmasis) išjungia visus pranešimus apie artėjančią stovėjimo pabaigą, o kitas (antrasis) yra skirtas įjungti/išjungti SMS pranešimus arba pranešimus, ateinančius iš pačios programėlės (push notifications). SMS ir programėlės pranešimai vienu metu įjungti būti negali, jeigu įjungtas vienas, kitas išsijungia, ir atvirkščiai.");
        atsNr9.add("PK L H XXX000\n\nPK – rinkliavos apmokėjimas Kauno mieste;\n\nL – Stovėjimo trukmė valandomis ir minutėmis:\n0.15 – 15 min.; 0.3 – 30 min.;\n0.45 – 45 min.; 1 – 1 val.;\n1.15 – 1 val. 15 min. ir t.t.\n\nH – stovėjimo zona:\n\nG – geltonoji zona.\nA – oranžinė zona,\nR – raudonoji zona,\nM – mėlynoji zona,\nZ – žalioji zona;\n\nXXX000 – transporto priemonės valstybinis numeris (numeris rašomas be tarpo);");


        addressList.put(questionList.get(0), atsNr1);
        addressList.put(questionList.get(1), atsNr2);
        addressList.put(questionList.get(2), atsNr3);
        addressList.put(questionList.get(3), atsNr4);
        addressList.put(questionList.get(4), atsNr5);
        addressList.put(questionList.get(5), atsNr6);
        addressList.put(questionList.get(6), atsNr7);
        addressList.put(questionList.get(7), atsNr8);
        addressList.put(questionList.get(8), atsNr9);
    }
}