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
		// 获取tab数据字段
		mViewPagerTabsData = getResources().getStringArray(
				R.array.viewpager_tabs);
	}

	private void initView() {
		// ViewPager 适配器，获取FragmentManager生成ViewPager适配器，将前面的tab数据设置进去
		FragmentPagerAdapter adapter = new TestAdapter(
				getSupportFragmentManager());

		// 查找控件，设置适配器
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

		// 根据当前滑动的页面下标，通过getItem决定显示哪个Fragment
		// 这个函数主要是为了给左右侧滑触发事件使用
		@Override
		public Fragment getItem(int position) {
			// return FragmentFactory.getFragment(position);
			return FragmentFactory.getFragment(position);
		}

		@Override
		public int getCount() {
			return mViewPagerTabsData.length;
		}

		// 这个函数主要是给TabPageIndicator对象使用，显示tab title内容，同时会绑定对应的index的tab title和Fragment,
		// 当你点击上面的tab键和滑动下面的ViewPager的时候，相应的地方会有相应的动作。
		@Override
		public CharSequence getPageTitle(int position) {
			return mViewPagerTabsData[position % mViewPagerTabsData.length];
		}
	}
}
