package com.example.farmingapp.models;

import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CropModel {
    //List<String> cropNameList;
    String cropName;
    String year;
    //String season;
    String season;
    long sowingArea;
    //List<String> plotList;
    String plot;

    public CropModel(String cropName, String year, String season, long sowingArea, String plot) {
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
