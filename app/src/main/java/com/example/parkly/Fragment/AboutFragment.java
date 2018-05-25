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

        String Klausimas1 = "Kokia yra šios skilties paskirtis?";
        String Klausimas2 = "What is the meaning of life?";
        String Klausimas3 = "Where is this?";
        String Klausimas4 = "whomst'd've'ly'yaint'nt'ed'ies's'y'es made this?";
        String Klausimas5 = "idk";

        questionList.add(Klausimas1);
        questionList.add(Klausimas2);
        questionList.add(Klausimas3);
        questionList.add(Klausimas4);
        questionList.add(Klausimas5);

        atsNr1.add("Atsakymas į šį klausimą gali pasirodyti labai lengvas, tačiau taip nėra. Daugelis pasakytų kad jis yra skirtas tam, kad padėtų vartotojams naudotis šia programėle, tačiau daugelis žmonių tiesiog čia neis");
        atsNr2.add("Atsakymas");
        atsNr3.add("Atsakymas");
        atsNr4.add("Atsakymas");
        atsNr5.add("Atsakymas");


        addressList.put(questionList.get(0), atsNr1);
        addressList.put(questionList.get(1), atsNr2);
        addressList.put(questionList.get(2), atsNr3);
        addressList.put(questionList.get(3), atsNr4);
        addressList.put(questionList.get(4), atsNr5);
    }
}