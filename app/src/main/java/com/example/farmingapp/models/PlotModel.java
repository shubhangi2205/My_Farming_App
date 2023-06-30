package com.example.farmingapp.models;

import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;

public class PlotModel {
    //List<String> cropNameList;
    String plotName;
    long plotArea;
    String location;
    boolean landReg;


    public PlotModel(String plotName, long plotArea, String location) {
        //this.cropNameList = cropNameList;
        this.plotName = plotName;
        this.plotArea = plotArea;
        this.location = location;
//        this.landReg = landReg;

    }

    public Map<String, Object> mapAllTheData() {
        Map<String, Object> map = new HashMap<>();
        map.put("plotName", this.plotName);
        map.put("plotArea", this.plotArea);
        map.put("location", this.location);
        //map.put("landRegesitered", this.landReg);


        return map;
    }
}