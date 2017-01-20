package com.aplex.aplextest.lineview.formatter;

import com.aplex.aplextest.lineview.model.SubcolumnValue;



public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
