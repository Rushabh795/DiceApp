package com.rushabh.diceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
Spinner spChooseSide;
    Integer[] arrChooseDiceSide = {4,6,8,10,12,20};
TextInputEditText edCustomDiceSide;
MaterialButton btAdd;
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
        setSpinnerData(arrChooseDiceSide);
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
    }
    private void setSpinnerData(Integer[] arrChooseSide) {
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_dropdown_item_1line, arrChooseSide);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); // The drop down view
        spChooseSide.setAdapter(spinnerArrayAdapter);
    }

}