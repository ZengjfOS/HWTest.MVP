package com.aplex.aplextest.lineview.listener;

import com.aplex.aplextest.lineview.model.SliceValue;



public interface PieChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int arcIndex, SliceValue value);

}
