package com.aplex.aplextest.lineview.formatter;

import com.aplex.aplextest.lineview.model.SliceValue;



public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
