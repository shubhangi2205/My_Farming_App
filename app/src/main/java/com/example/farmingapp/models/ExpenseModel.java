package com.example.farmingapp.models;

import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ExpenseModel {

    SpinnerAdapter typeOfExpense;
    String dateOfExpense;
    String desc;
    int expenseAmount;


    public ExpenseModel(SpinnerAdapter typeOfExpense, String dateOfExpense, String desc, int expenseAmount) {
        //this.cropNameList = cropNameList;
        this.typeOfExpense = typeOfExpense;
        this.dateOfExpense = dateOfExpense;
        this.desc = desc;
        this.expenseAmount = expenseAmount;
    }
    public Map<String,Object> mapAllTheData(){
        Map<String,Object> map = new HashMap<>();
        map.put("TypeOfExpense",this.typeOfExpense);
        map.put("DateOfExpense",this.dateOfExpense);
        map.put("Description",this.desc);
        map.put("ExpenseAmount",this.expenseAmount);

        return map;
    }

}
