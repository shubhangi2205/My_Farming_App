package com.example.farmingapp.models;

import android.widget.SpinnerAdapter;

import java.util.HashMap;
import java.util.Map;

public class FarmingOutputModel {

    String cropName;
    String year;
    //String season;
    String season;
    long yield;
    //List<String> plotList;
    String plot;

    public FarmingOutputModel(String cropName, String year, String season, long sowingArea, String plot) {
        //this.cropNameList = cropNameList;
        this.cropName = cropName;
        this.year = year;
        this.season = season;
        this.yield = yield;
        this.plot = plot;
    }
    public Map<String,Object> mapAllTheData(){
        Map<String,Object> map = new HashMap<>();
        map.put("cropName",this.cropName);
        map.put("year",this.year);
        map.put("season",this.season);
        map.put("yield",this.yield);
        map.put("plot",this.plot);

        return map;
    }

}
