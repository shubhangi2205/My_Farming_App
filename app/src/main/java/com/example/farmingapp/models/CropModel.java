package com.example.farmingapp.models;

import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CropModel {
    //List<String> cropNameList;
    String cropName;
    SpinnerAdapter year;
    //String season;
    SpinnerAdapter season;
    long sowingArea;
    //List<String> plotList;
    SpinnerAdapter plot;

    public CropModel(String cropName, SpinnerAdapter year, SpinnerAdapter season, long sowingArea, SpinnerAdapter plot) {
        //this.cropNameList = cropNameList;
        this.cropName = cropName;
        this.year = year;
        this.season = season;
        this.sowingArea = sowingArea;
        this.plot = plot;
    }
    public Map<String,Object> mapAllTheData(){
        Map<String,Object> map = new HashMap<>();
        map.put("cropName",this.cropName);
        map.put("year",this.year);
        map.put("season",this.season);
        map.put("sowingArea",this.sowingArea);
        map.put("plot",this.plot);

        return map;
    }

}
