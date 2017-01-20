package com.aplex.aplextest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.aplex.aplextest.fragment.BuzzFragment;
import com.aplex.aplextest.fragment.EepromFragment;
import com.aplex.aplextest.fragment.EthernetAutoConfFragment;
import com.aplex.aplextest.fragment.GPIOFragment;
import com.aplex.aplextest.fragment.SerialPortToolFragment;
import com.aplex.aplextest.viewpagerindicator.TabPageIndicator;

public class MainUi extends FragmentActivity {
	private ViewPager mViewPager;
	private String[] mViewPagerTabsData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_main);
		initData();
		initView();
	}

	private void initData() {
		mViewPagerTabsData = getResources().getStringArray(
				R.array.viewpager_tabs);
	}

	private void initView() {
		FragmentPagerAdapter adapter = new TestAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.main_vp);
		mViewPager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		TabPageIndicator tabIndicator = (TabPageIndicator) findViewById(R.id.Tabs);
		tabIndicator.setBackground(getResources().getDrawable(
				R.color.viewPagerTabBackground));
		tabIndicator.setViewPager(mViewPager);
	}

	private class TestAdapter extends FragmentPagerAdapter {
		public TestAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// return FragmentFactory.getFragment(position);
			return FragmentFactory.getFragment(position);
		}

		@Override
		public int getCount() {
			return mViewPagerTabsData.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mViewPagerTabsData[position % mViewPagerTabsData.length];
		}
	}
}
