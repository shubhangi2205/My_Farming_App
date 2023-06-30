package com.example.farmingapp.models;

import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.HashMap;
import java.util.Map;

public class HarvestModel {


    String dateOfSell;
    int sellQuantity;
    int sellingAmount;


    public HarvestModel(String dateOfSell, int sellQuantity, int sellingAmount) {
        //this.cropNameList = cropNameList;
        this.dateOfSell = dateOfSell;
        this.sellQuantity = sellQuantity;
        this.sellingAmount = sellingAmount;

    }
    public Map<String,Object> mapAllTheData(){
        Map<String,Object> map = new HashMap<>();
        map.put("DateOfSell",this.dateOfSell);
        map.put("SellQuantity",this.sellQuantity);
        map.put("SellingAmount",this.sellingAmount);

        return map;
    }

}
