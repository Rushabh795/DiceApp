package com.rushabh.diceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
LinearLayout lvMain;
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
        lvMain= findViewById(R.id.lvMain);
        //initate Shared Pref
        SharedPrefManager.init(MainActivity.this);
        setSpinnerData(arrChooseDiceSide);
        arrListViewArray = new ArrayList<>();
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding new array element to spinner
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
                    Toast.makeText(MainActivity.this ,"Your custom Dice " + strNewCustomSide +" is added to dropdown" ,Toast.LENGTH_SHORT).show();
                    edCustomDiceSide.setText("");
                }
            }
        });
        btRollOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRollTwoText.setText("");
                String stID = spChooseSide.getSelectedItem().toString();
                Dice d = new Dice(Integer.parseInt(stID));
                tvRollTwoText.setVisibility(View.GONE);
                diceResultBeforeAndAfter(d,1);
            }
        });
        btRollTwice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID = spChooseSide.getSelectedItem().toString();
                Dice d = new Dice(Integer.parseInt(stID));
                tvRollTwoText.setVisibility(View.VISIBLE);
                diceResultBeforeAndAfter(d,2);
            }
        });

    }
    private void setSpinnerData(Integer[] arrChooseSide) {
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_dropdown_item_1line, arrChooseSide);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); // The drop down view
        spChooseSide.setAdapter(spinnerArrayAdapter);
    }
    private void diceResultBeforeAndAfter(Dice dice ,int i) {
        if(i==1) {
            dice.roll();
            int sideUpAfterRoll = dice.getSideUp();
            tvRollOneText.setText(String.valueOf(sideUpAfterRoll).toString());
        }else {
            dice.roll();
            int sideUpAfterRoll = dice.getSideUp();
            tvRollOneText.setText(String.valueOf(sideUpAfterRoll).toString());
            dice.roll();
            int sideUpAfterRolltwo = dice.getSideUp();
            tvRollTwoText.setText(String.valueOf(sideUpAfterRolltwo).toString());
        }
        //save data to shared pref
        saveDataToSharePref();
    }
    private void saveDataToSharePref() {
        String stOne = "First Dice " +tvRollOneText.getText().toString();
        String stTwo = tvRollTwoText.getText().toString().trim();
        arrListViewArray.add(stOne);
        if(!stTwo.equalsIgnoreCase(""))
        {
           String stNew =  "Second Dice " + stTwo ;
            arrListViewArray.add(stNew);
        }
        //Using Gson i am saving data
        Gson gson = new Gson();
        //Saving string to Gson
        String json = gson.toJson(arrListViewArray);
        //pass object into sharePref
        SharedPrefManager.putString("newSaveObject",json);
        getDataFromSharedPref();
    }

    private void getDataFromSharedPref() {
        //getting data from gson
        Gson gson = new Gson();
        String json = SharedPrefManager.getString("newSaveObject", "");
        if (json.isEmpty()) {
            Toast.makeText(MainActivity.this, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            //getting array from gson and saved
            List<String> arrNewSavedList = gson.fromJson(json, type);
          //set array to listview
            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrNewSavedList);
            lvSavedList.setAdapter(mHistory);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Get Menu item from menu.xml
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            //Change the menu color from toolbar
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_gr_strat)), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //handle menu click event
            case R.id.nav_nightMode:
                //set night mode
                int i = SharedPrefManager.getInt("NightMode",0);
               if(i == 0)
               {
                   //Night mode on
                   lvMain.setBackgroundColor(getResources().getColor(R.color.black));
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                   SharedPrefManager.putInt("NightMode", 1);
                   Toast.makeText(MainActivity.this, "Night Mode On", Toast.LENGTH_SHORT).show();
               }else{
                   //night mode off
                   lvMain.setBackgroundColor(getResources().getColor(R.color.white));
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   SharedPrefManager.putInt("NightMode", 0);
                   Toast.makeText(MainActivity.this, "Night Mode Off", Toast.LENGTH_SHORT).show();
               }return true;
        } return super.onOptionsItemSelected(item);
    }

}