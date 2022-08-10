package com.rushabh.diceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Spinner spChooseSide;
    Integer[] arrChooseDiceSide = {4,6,8,10,12,20};
TextInputEditText edCustomDiceSide;
MaterialButton btAdd,btRollOnce,btRollTwice;
TextView tvRollOneText,tvRollTwoText;
ArrayList<String> arrListViewArray;
ListView lvSavedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindID();
    }

    private void bindID() {
        spChooseSide = findViewById(R.id.spChooseSide);
        edCustomDiceSide=findViewById(R.id.edCustomDiceSide);
        btAdd = findViewById(R.id.btAdd);
        btRollOnce = findViewById(R.id.btRollOnce);
        btRollTwice = findViewById(R.id.btRollTwice);
        tvRollOneText = findViewById(R.id.tvRollOneText);
        tvRollTwoText = findViewById(R.id.tvRollTwoText);
        lvSavedList= findViewById(R.id.lvSavedList);
        SharedPrefManager.init(MainActivity.this);
        setSpinnerData(arrChooseDiceSide);
        arrListViewArray = new ArrayList<>();
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNewCustomSide = edCustomDiceSide.getText().toString().trim();
                if(!strNewCustomSide.equalsIgnoreCase(""))
                {
                    Integer arrNewSide[]  = new Integer[arrChooseDiceSide.length +1 ];
                    for (int i = 0; i < arrChooseDiceSide.length; i++) {
                        //Copy element from old array to new array
                        arrNewSide[i] = arrChooseDiceSide[i];
                    }
                    arrNewSide[arrNewSide.length -1] = Integer.parseInt(strNewCustomSide);
                    //sorting array
                    Arrays.sort(arrNewSide);
                    //copy new array to old array
                    arrChooseDiceSide= Arrays.copyOf(arrNewSide, arrNewSide.length);
                    setSpinnerData(arrChooseDiceSide);
                }
            }
        });
        btRollOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID = spChooseSide.getSelectedItem().toString();
                Dice d = new Dice(Integer.parseInt(stID));
                tvRollTwoText.setVisibility(View.GONE);
                diceResultBeforeAndAfter(d);
            }
        });
    }
    private void setSpinnerData(Integer[] arrChooseSide) {
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_dropdown_item_1line, arrChooseSide);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); // The drop down view
        spChooseSide.setAdapter(spinnerArrayAdapter);
    }
    private void diceResultBeforeAndAfter(Dice dice) {
        dice.roll();
        int sideUpAfterRoll = dice.getSideUp();
        tvRollOneText.setText(String.valueOf(sideUpAfterRoll).toString());
        saveDataToSharePref();
    }

    private void saveDataToSharePref() {
        String nameData = tvRollOneText.getText().toString();
        arrListViewArray.add(nameData);
        //Using Gson i am saving data
        Gson gson = new Gson();
        //Saving string to Gson
        String json = gson.toJson(arrListViewArray);
        //pass object into sharePref
SharedPrefManager.putString("newSaveObject",json);
getDataFromSharedPref();
    }

    private void getDataFromSharedPref() {
        Gson gson = new Gson();
        String json = SharedPrefManager.getString("newSaveObject", "");
        if (json.isEmpty()) {
            Toast.makeText(MainActivity.this, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> arrPackageData = gson.fromJson(json, type);
            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrPackageData);
            lvSavedList.setAdapter(mHistory);

        }
    }
}