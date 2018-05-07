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
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.Adapters.LicensePlateAdapter;
import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
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
    public static Button btn_removeAll;
    public static Button btn_add;

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
        final ListView lst_Car = view.findViewById(R.id.lst_Cars);
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
                Disposable disposable = licensePlateRepository.getAllNumbers()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> licensePlates) throws Exception {
                                if (!deleteClicked) {
                                    arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.cars_tab_layout_1, licensePlates);
                                    lst_Car.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();
                                    deleteClicked = true;
                                    btn_add.setClickable(false);
                                    btn_add.setEnabled(false);
                                }
                                else
                                {
                                    deleteSelectedLicensePlates();
                                    deleteClicked = false;
                                    refreshAdapter(view);
                                    btn_add.setClickable(true);
                                    btn_add.setEnabled(true);
                                    database(view);
                                    loadData();

                                }
                            }

                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                compositeDisposable.add(disposable);
            }
        });
    }

    private void deleteSelectedLicensePlates() {
        Disposable disposable = licensePlateRepository.findAllByNumber(selectedLicensePlateList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        for (LicensePlate l:licensePlates)
                        {
                            deleteLicensePlate(l);
                            licensePlateList.remove(l);
                        }
                        selectedLicensePlateList.clear();
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
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
        ListView lst_Car = view.findViewById(R.id.lst_Cars);
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
                    public void accept(List<LicensePlate> licensePlates) throws Exception {
                        onGetAllLicensePlateSuccess(licensePlates);
                        if (licensePlates.size()==1 && licensePlates.get(0).getCurrent() == false)
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
                    public void accept(Throwable throwable) throws Exception {
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action:");
        menu.add(Menu.NONE, 0, Menu.NONE, "Mark as default");
        //menu.add(Menu.NONE, 1, Menu.NONE, "Remove");
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
                new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                        .setMessage("Do you want to set "+licensePlate.getNumber() + " as default car?")
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
            /*case 1:
            {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to remove "+licensePlate.getNumber()+" ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteLicensePlate(licensePlate);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            break;*/
        }

        return true;
    }

    private void setDefault(final LicensePlate licensePlate) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
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
                    public void accept(Object o) throws Exception {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void deleteLicensePlate(final LicensePlate licensePlate) {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                licensePlateRepository.delete(licensePlate);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void refreshDefault()
    {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                LicensePlate licensePlate = licensePlateRepository.findDefault();
                if (licensePlate != null)
                {
                    txt_defaultCar.setText(licensePlate.getNumber());
                }
                else if (licensePlateList.size() != 0){
                    txt_defaultCar.setText("Not selected");
                }
                else txt_defaultCar.setText("List is empty");

                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {}
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
        compositeDisposable.add(disposable);
    }
}
