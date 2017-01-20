package com.aplex.aplextest.lineview.formatter;

import com.aplex.aplextest.lineview.model.PointValue;



public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
