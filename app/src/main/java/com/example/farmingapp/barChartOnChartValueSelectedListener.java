package com.example.farmingapp;

//public class barChartOnChartValueSelectedListener implements com.github.mikephil.charting.listener.OnChartValueSelectedListener {
//}

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class barChartOnChartValueSelectedListener implements OnChartValueSelectedListener {

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        //trigger activity when the bar value is selected

    }

    @Override
    public void onNothingSelected() {

    }
}
