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

        String Klausimas1 = getResources().getString(R.string.Question1);
        String Klausimas2 = getResources().getString(R.string.Question2);
        String Klausimas3 = getResources().getString(R.string.Question3);
        String Klausimas4 = getResources().getString(R.string.Question4);
        String Klausimas5 = getResources().getString(R.string.Question5);
        String Klausimas6 = getResources().getString(R.string.Question6);
        String Klausimas7 = getResources().getString(R.string.Question7);
        String Klausimas8 = getResources().getString(R.string.Question8);
        String Klausimas9 = getResources().getString(R.string.Question9);

        questionList.add(Klausimas1);
        questionList.add(Klausimas2);
        questionList.add(Klausimas3);
        questionList.add(Klausimas4);
        questionList.add(Klausimas5);
        questionList.add(Klausimas6);
        questionList.add(Klausimas7);
        questionList.add(Klausimas8);
        questionList.add(Klausimas9);

        atsNr1.add(getResources().getString(R.string.Answer1));
        atsNr2.add(getResources().getString(R.string.Answer2));
        atsNr3.add(getResources().getString(R.string.Answer3));
        atsNr4.add(getResources().getString(R.string.Answer4));
        atsNr5.add(getResources().getString(R.string.Answer5));
        atsNr6.add(getResources().getString(R.string.Answer6));
        atsNr7.add(getResources().getString(R.string.Answer7));
        atsNr8.add(getResources().getString(R.string.Answer8));
        atsNr9.add(getResources().getString(R.string.Answer9));


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