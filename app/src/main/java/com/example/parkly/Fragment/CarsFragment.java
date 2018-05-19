package com.example.parkly.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkly.Activity.addCarActivity;
import com.example.parkly.Adapters.LicensePlateAdapter;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by donvel on 2018-03-12.
 */

public class CarsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cars_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database(view);
        loadData();
        init(view);
    }

    TextView txt_defaultCar;
    public static boolean deleteClicked = false;
    public Button btn_removeAll;
    public Button btn_add;
    public ListView lst_Car;

    //Adapter
    List<LicensePlate> licensePlateList = new ArrayList<>();
    List<String> selectedLicensePlateList = new ArrayList<>();
    LicensePlateAdapter adapter;
    ArrayAdapter<String> arrayAdapter;

    //Database
    private CompositeDisposable compositeDisposable;
    private LicensePlateRepository licensePlateRepository;
    LicensePlateDatabase licensePlateDatabase;

    public void init(final View view)
    {
        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), addCarActivity.class));
            }
        });

        btn_removeAll = view.findViewById(R.id.btn_removeAll);
        disableRemove();
        lst_Car.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (deleteClicked) {
                        String selectedItem = ((TextView) view).getText().toString();
                        if (selectedLicensePlateList.contains(selectedItem)) {
                            selectedLicensePlateList.remove(selectedItem);
                        } else {
                            selectedLicensePlateList.add(selectedItem);
                        }
                    }
                }
        });

        btn_removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst_Car.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    if (!deleteClicked) {
                        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.cars_tab_layout_1, numbers());
                        lst_Car.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        deleteClicked = true;
                        btn_add.setClickable(false);
                        btn_add.setEnabled(false);
                        selectedLicensePlateList.clear();
                    }
                    else
                    {
                        lst_Car.clearFocus();
                        if(!selectedLicensePlateList.isEmpty()) {
                            deleteSelectedLicensePlates();
                        }
                        deleteClicked = false;
                        refreshAdapter(view);
                        btn_add.setClickable(true);
                        btn_add.setEnabled(true);
                    }
                }
        });
    }

    private List<String> numbers() {
        List<String> numbers = new ArrayList<>();
        for (LicensePlate l:licensePlateList)
        {
            numbers.add(l.getNumber());
        }
        return numbers;
    }

    private void deleteSelectedLicensePlates() {
        List<LicensePlate> licensePlates = new ArrayList<>();
        for (LicensePlate l:licensePlateList) {
            if (selectedLicensePlateList.contains(l.getNumber())) {
                licensePlates.add(l);
            }
        }
        for (LicensePlate l:licensePlates)
        {
            deleteLicensePlate(l);
            licensePlateList.remove(l);
        }
        selectedLicensePlateList.clear();
    }

    public void disableRemove()
    {
        if (licensePlateList.size() == 0)
        {
            btn_removeAll.setClickable(false);
            btn_removeAll.setEnabled(false);
        }
        else
        {
            btn_removeAll.setClickable(true);
            btn_removeAll.setEnabled(true);
        }
    }

    public void database(View view){
        //Init
        compositeDisposable = new CompositeDisposable();

        //init View
        refreshAdapter(view);
        txt_defaultCar = view.findViewById(R.id.txt_defaultCar);

        licensePlateDatabase = LicensePlateDatabase.getInstance(getActivity());
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));
    }

    public void refreshAdapter(View view)
    {
        adapter = new LicensePlateAdapter(getActivity(), licensePlateList);
        lst_Car = view.findViewById(R.id.lst_Cars);
        registerForContextMenu(lst_Car);
        lst_Car.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadData()
    {
        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) {
                        onGetAllLicensePlateSuccess(licensePlates);
                        if (licensePlates.size()==1 && !licensePlates.get(0).getCurrent())
                        {
                            setDefault(licensePlates.get(0));
                        }
                        else refreshDefault();
                        disableRemove();
                        btn_add.setClickable(true);
                        btn_add.setEnabled(true);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();
        licensePlateList.addAll(licensePlates);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        String selectAction = getString(R.string.select_action);
        String markDefault = getString(R.string.mark_default);

        menu.setHeaderTitle(selectAction);
        menu.add(Menu.NONE, 0, Menu.NONE, markDefault);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final LicensePlate licensePlate = licensePlateList.get(info.position);
        switch (item.getItemId())
        {
            case 0:
            {

                String setDefault1 = getString(R.string.set_default1);
                String setDefault2 = getString(R.string.set_default2);

                new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                        .setMessage(setDefault1 + " " + licensePlate.getNumber() + " " + setDefault2)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setDefault(licensePlate);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            break;
        }

        return true;
    }

    private void setDefault(final LicensePlate licensePlate) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                LicensePlate oldLicensePlate = licensePlateRepository.findDefault();
                if (oldLicensePlate != null)
                {
                    oldLicensePlate.setCurrent(false);
                    licensePlateRepository.updateLicensePlate(oldLicensePlate);
                }
                licensePlate.setCurrent(true);
                licensePlateRepository.updateLicensePlate(licensePlate);
                refreshDefault();
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                }, new Action() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void deleteLicensePlate(final LicensePlate licensePlate) {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                licensePlateRepository.delete(licensePlate);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void refreshDefault()
    {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {

                LicensePlate licensePlate = licensePlateRepository.findDefault();
                if (licensePlate != null)
                {
                    txt_defaultCar.setText(licensePlate.getNumber());
                }
                else if (licensePlateList.size() != 0){

                    String notSelectedText = getString(R.string.not_selected);

                    txt_defaultCar.setText(notSelectedText);
                }
                else {

                    String listIsEmpty = getString(R.string.list_empty);

                    txt_defaultCar.setText(listIsEmpty);
                }

                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
