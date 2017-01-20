package com.aplex.aplextest.lineview.listener;

import com.aplex.aplextest.lineview.model.PointValue;




public interface LineChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int lineIndex, int pointIndex, PointValue value);

}
