package com.aplex.aplextest.fragment;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.fragment.SerialPortToolFragment.ViewHolder;
import com.aplex.aplextest.ui.stabilityactivity.MemTestActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class StabilityTestFragment extends Fragment implements OnItemClickListener{
	private LinearLayout RootView;
	private ListView mOptList;
	private String[] mStabilityOptDatas;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState) {
		initView(inflater);
		initData();
        initListener();
        RootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));

        return RootView;
	}
	private void initView(LayoutInflater inflater) {
		RootView = (LinearLayout) inflater.inflate(R.layout.ui_stability, null);
		mOptList = (ListView) RootView.findViewById(R.id.stability_list);
	}

	private void initData() {
		mStabilityOptDatas = getResources().getStringArray(R.array.stability_opt);
		mOptList.setAdapter(new StabilityOptAdapter());
		
	}

	private void initListener() {
		mOptList.setOnItemClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		performByPosition(position);
	}
	private void performByPosition(int position) {
		switch (position) {
		case 0:selectMem1Activity();
			break;

		default:
			break;
		}
	}
	private void selectMem1Activity() {
		Intent intent = new Intent(getActivity(),MemTestActivity.class);
		startActivity(intent);
	}
	private class StabilityOptAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mStabilityOptDatas.length;
		}

		@Override
		public Object getItem(int position) {
			return mStabilityOptDatas[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.item_ui_serialport_opt, null);
				holder = new ViewHolder();
				holder.title = (FlatTextView) convertView
						.findViewById(R.id.serialporttool_opt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(mStabilityOptDatas[position]);
			return convertView;
		}
	}

	static class ViewHolder {
		FlatTextView title;
	}

	
}
