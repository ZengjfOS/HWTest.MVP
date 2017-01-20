package com.aplex.aplextest.lineview.listener;

import com.aplex.aplextest.lineview.model.SubcolumnValue;



public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

}
