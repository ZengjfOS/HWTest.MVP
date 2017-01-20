package com.aplex.aplextest.ui.stresstestActivity;

import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.lineview.listener.LineChartOnValueSelectListener;
import com.aplex.aplextest.lineview.model.Axis;
import com.aplex.aplextest.lineview.model.Line;
import com.aplex.aplextest.lineview.model.LineChartData;
import com.aplex.aplextest.lineview.model.PointValue;
import com.aplex.aplextest.lineview.model.ValueShape;
import com.aplex.aplextest.lineview.util.ChartUtils;
import com.aplex.aplextest.lineview.view.LineChartView;

public class StressTestDetailActivity extends Activity {
	private LineChartView mBatteryElectricity;
	private LineChartView mBatteryTemp;
	private LineChartView mBatteryVoltage;
	private LineChartView mCPUCoreLoad;
	private LineChartView mCPUCorFrequency;
	private LineChartView mCPUPerforms;
	private LineChartView mCPUAllCoreLoad;
	private LineChartData data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_stresstestdetail);
		initView();
		initData();
	}

	private void initData() {
		// 很坐标最大值
		int numValues = 50;
		List<PointValue> values = new ArrayList<PointValue>();
		// 横坐标每个值得纵坐标设置对应的值
		for (int i = 0; i < numValues; ++i) {
			values.add(new PointValue(i, (float) Math.random() * 100f));
		}
		// 把值放到折线对象中
		Line line = new Line(values);
		// 设置折线颜色
		line.setColor(ChartUtils.COLOR_GREEN);
		// 在每个坐标值设置点
		line.setHasPoints(false);// too many values so don't draw points.
		line.setHasLabelsOnlyForSelected(true);
		// 把折线放到折线集合中
		List<Line> lines = new ArrayList<Line>();
		
		
//		private boolean isFilled = false;
//        private boolean hasLabels = false;
//        private boolean isCubic = false;
//        private boolean hasLabelForSelected = false;
		line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setPointColor(ChartUtils.COLOR_GREEN);
		lines.add(line);
		
		// 把折线集合转换成表格能识别的数据
		data = new LineChartData(lines);
		data.setAxisXBottom(new Axis());
		data.setAxisYLeft(new Axis().setHasLines(true));
		mBatteryElectricity.setLineChartData(data);
		mBatteryTemp.setLineChartData(data);
		mBatteryVoltage.setLineChartData(data);
		mCPUCoreLoad.setLineChartData(data);
		mCPUCorFrequency.setLineChartData(data);
		mCPUPerforms.setLineChartData(data);
		mCPUAllCoreLoad.setLineChartData(data);
	
		
	}

	private void initView() {
//		private LineChartView mBatteryTemp;
//		private LineChartView mBatteryVoltage;
//		private LineChartView mCPUCoreLoad;
//		private LineChartView mCPUCorFrequency;
//		private LineChartView mCPUPerforms;
//		private LineChartView mCPUAllCoreLoad;
		mBatteryElectricity = (LineChartView) findViewById(R.id.Battery_Electricity);
		mBatteryTemp = (LineChartView) findViewById(R.id.Battery_temp);
		mBatteryVoltage = (LineChartView) findViewById(R.id.Battery_Voltage);
		mCPUCoreLoad = (LineChartView) findViewById(R.id.CPU_core_load);
		mCPUCorFrequency = (LineChartView) findViewById(R.id.CPU_core_frequency);
		mCPUPerforms =  (LineChartView) findViewById(R.id.CPU_performs);
		mCPUAllCoreLoad = (LineChartView) findViewById(R.id.CPU_All_core_load);
		// Disable zoom/scroll for previewed chart, visible chart ranges depends
		// on preview chart viewport so
		// zoom/scroll is unnecessary.
		mBatteryElectricity.setZoomEnabled(false);
		mBatteryElectricity.setScrollEnabled(false);
		
		mBatteryTemp.setZoomEnabled(false);
		mBatteryTemp.setScrollEnabled(false);
		mBatteryVoltage.setZoomEnabled(false);
		mBatteryVoltage.setScrollEnabled(false);
		mCPUCoreLoad.setZoomEnabled(false);
		mCPUCoreLoad.setScrollEnabled(false);
		mCPUCorFrequency.setZoomEnabled(false);
		mCPUCorFrequency.setScrollEnabled(false);
		mCPUPerforms.setZoomEnabled(false);
		mCPUPerforms.setScrollEnabled(false);
		mCPUAllCoreLoad.setZoomEnabled(false);
		mCPUAllCoreLoad.setScrollEnabled(false);
		
		mBatteryTemp.setOnValueTouchListener(new LineChartOnValueSelectListener() {
			
			@Override
			public void onValueDeselected() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
				Toast.makeText(getApplicationContext(), value.toString(), Toast.LENGTH_LONG).show();
				
			}
		});
	}
}
