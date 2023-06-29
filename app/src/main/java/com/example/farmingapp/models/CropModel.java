package com.example.farmingapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropModel {
    List<String> cropNameList;
    int year;
    String season;
    long sowingArea;
    List<String> plotList;

    public CropModel(List<String> cropNameList, int year, String season, long sowingArea, List<String> plotList) {
        this.cropNameList = cropNameList;
        this.year = year;
        this.season = season;
        this.sowingArea = sowingArea;
        this.plotList = plotList;
    }
    public Map<String,Object> mapAllTheData(){
        Map<String,Object> map = new HashMap<>();
        map.put("cropNameList",this.cropNameList);
        map.put("year",this.year);
        map.put("season",this.season);
        map.put("sowingArea",this.sowingArea);
        map.put("plotList",this.plotList);

        return map;
    }

}
