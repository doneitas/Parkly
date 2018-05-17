package com.example.parkly.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.parkly.DataBase.LicensePlateDatabase;
import com.example.parkly.DataBase.LicensePlateRepository;
import com.example.parkly.DataBase.LocalUserDataSource;
import com.example.parkly.DataBase.Tables.LicensePlate;
import com.example.parkly.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marius on 2018-03-04.
 */

public class addCarActivity extends Activity {
    EditText txt_plate;
    Button btn_confirm;
    ImageButton closeButton;
    //Database
    private CompositeDisposable compositeDisposable;
    public LicensePlateRepository licensePlateRepository;
    List<LicensePlate> licensePlateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_car);

        setPopUpDimensions();
        loadData();
        init();
    }

    public void setPopUpDimensions(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }

    private String blockCharacterSet = "~#^|$%&*! ";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    public static boolean isNumberCorrect(String number) {
        String expression = "[a-zA-Z]{3}+[0-9]{3}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public void init() {
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_plate = findViewById(R.id.txt_plate);
        txt_plate.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6), filter});

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value = txt_plate.getText().toString().toUpperCase();
                if(isNumberCorrect(value)) {
                    if (!checkIfContains(value))
                    {
                        insertLicensePlate(value);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "This car is already added", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong format", Toast.LENGTH_LONG).show();
                }
            }
        });
        closeButton = findViewById(R.id.btn_exit);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }



    private boolean checkIfContains(String value)
    {
        for (LicensePlate l:licensePlateList)
        {
            if (l.getNumber().equalsIgnoreCase(value))return true;
        }
        return false;
    }

    private void insertLicensePlate(final String value)
    {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                LicensePlate licensePlate = new LicensePlate();
                licensePlate.setNumber(value);
                if(licensePlateRepository.findDefault() == null)
                {
                    licensePlate.setCurrent(true);
                }
                licensePlateRepository.insertAll(licensePlate);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                               @Override
                               public void accept(Object o) {
                                   Toast.makeText(getApplicationContext(), "License Plate added !", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) {
                                   Toast.makeText(getApplicationContext(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                );
        compositeDisposable.add(disposable);
        finish();
    }

    private void loadData()
    {
        //Init
        compositeDisposable = new CompositeDisposable();
        LicensePlateDatabase licensePlateDatabase = LicensePlateDatabase.getInstance(this);
        licensePlateRepository = LicensePlateRepository.getInstance(LocalUserDataSource.getInstance(licensePlateDatabase.licensePlateDao()));

        Disposable disposable = licensePlateRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<LicensePlate>>() {
                    @Override
                    public void accept(List<LicensePlate> licensePlates) {
                        onGetAllLicensePlateSuccess(licensePlates);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLicensePlateSuccess(List<LicensePlate> licensePlates)
    {
        licensePlateList.clear();
        licensePlateList.addAll(licensePlates);
    }
}