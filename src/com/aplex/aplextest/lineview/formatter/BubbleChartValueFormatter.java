package com.aplex.aplextest.lineview.formatter;

import com.aplex.aplextest.lineview.model.BubbleValue;



public interface BubbleChartValueFormatter {

    public int formatChartValue(char[] formattedValue, BubbleValue value);
}
