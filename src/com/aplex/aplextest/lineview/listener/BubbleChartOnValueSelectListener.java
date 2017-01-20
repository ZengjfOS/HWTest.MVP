package com.aplex.aplextest.lineview.listener;

import com.aplex.aplextest.lineview.model.BubbleValue;




public interface BubbleChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int bubbleIndex, BubbleValue value);

}
