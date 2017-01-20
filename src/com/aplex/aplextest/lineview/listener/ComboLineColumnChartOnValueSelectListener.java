package com.aplex.aplextest.lineview.listener;

import com.aplex.aplextest.lineview.model.PointValue;
import com.aplex.aplextest.lineview.model.SubcolumnValue;




public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value);

}
