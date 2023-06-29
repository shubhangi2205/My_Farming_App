package com.example.farmingapp.models;

import java.util.HashMap;
import java.util.Map;

public class FarmingOutputModel {

    String cropName;
    int year;
    String season;
    int yield;

    String plot;

    public FarmingOutputModel(String cropName, int year, String season, int yield, String plot) {
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
