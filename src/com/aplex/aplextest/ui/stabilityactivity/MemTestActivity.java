package com.aplex.aplextest.ui.stabilityactivity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.ColorfulRingProgressView;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.jninative.StabilityTest;

@SuppressLint("NewApi")
public class MemTestActivity extends Activity implements OnClickListener {
	//测试内存大小选项框
	private Spinner mMemSizeOpt;
	//测试次数选项框
	private Spinner mLoopTime;
	//开始测试按钮
	private FlatButton mMem1Btn;
	//停止测试按钮
	private FlatButton mStopTestBtn;
	
	//测试内存大小
	private int mMemSizeFlag = 50;
	//测试次数
	private int mLoopTimeFlag = 1;
	private int currentLoopTime = 0;
	
	
	private List<Integer> mList;
	//jni类
	private StabilityTest mStabilityTest = new StabilityTest();
	private Handler mHandler = new Handler();
	
	//进度条
	private ColorfulRingProgressView mProgressView;
	//进度条指示器
	private FlatTextView mCurrentPercent;
	//显示结果表格
	private ListView mListView;
	//结果数据适配器
	private ResultAdapter mAdapter;
	private static final int TRUE_FLAG = 0;
	private static final int FALSE_FLAG = 1;

	private static final int NORMAL_FLAG = 0;
	private static final int PARAM_ERROR_FLAG = 1;
	private static final int MEM_ADDR_ERROR_FLAG = 2;
	private static final int MEM_ADDR_GET_FLAG_FLAG = 4;

	private List<ResultBean> mResultList;
	private boolean StopTestFlag = false;
	private boolean TestingFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_memtest1);
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		mMemSizeOpt.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mMemSizeFlag = Integer.valueOf((getResources().getStringArray(
						R.array.mem1_size)[position]).replace("M", ""));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		mLoopTime.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mLoopTimeFlag = Integer.valueOf((getResources().getStringArray(
						R.array.mem1_loop_time)[position]));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		mMem1Btn.setOnClickListener(this);
		mStopTestBtn.setOnClickListener(this);
		if (mAdapter ==null) {
			mAdapter = new ResultAdapter();
		}else {
			mAdapter.notifyDataSetChanged();
		}
		mListView.setAdapter(mAdapter);
	}

	private void initData() {
		ArrayAdapter<String> SizeOptAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_button, getResources().getStringArray(
						R.array.mem1_size));
		SizeOptAdapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mMemSizeOpt.setAdapter(SizeOptAdapter);

		ArrayAdapter<String> loopTimeAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_button, getResources().getStringArray(
						R.array.mem1_loop_time));
		loopTimeAdapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mLoopTime.setAdapter(loopTimeAdapter);

		mList = new ArrayList<Integer>();
		mResultList = new ArrayList<MemTestActivity.ResultBean>();
	}
	@Override
	protected void onDestroy() {
		//停止循环
		StopTestFlag = true;
		//停止测试
		mStabilityTest.StopMemTest();
		super.onDestroy();
	}
	private void initView() {
		mMemSizeOpt = (Spinner) findViewById(R.id.memsize_spinner);
		mLoopTime = (Spinner) findViewById(R.id.looptime_spinner);
		mMem1Btn = (FlatButton) findViewById(R.id.start_test_btn);
		mStopTestBtn = (FlatButton) findViewById(R.id.stop_test_btn);
		
		mProgressView = (ColorfulRingProgressView) findViewById(R.id.crpv);
		mCurrentPercent = (FlatTextView) findViewById(R.id.current_percent);
		mListView = (ListView) findViewById(R.id.test_result);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.start_test_btn) {
			if (TestingFlag) {
				//提示
				Toast.makeText(this, "Don't repeat the operation in memory tests.", Toast.LENGTH_LONG).show();
				return;
			}
			StopTestFlag = false;
			//初始化UI
			mProgressView.setPercent(0);
			mCurrentPercent.setText(0 + "%");
			//清除数据缓存
			mResultList.clear();
			//UI数据清除
			mAdapter.notifyDataSetChanged();
			
			//执行异步任务
			new TestTask(mLoopTimeFlag).execute();
			//创建个线程专门获取百分比进度
			new Thread(){
				public void run() {
					while (TestingFlag) {
						SystemClock.sleep(500);
						final float percent  = mStabilityTest.getPercent();
						Log.e("MemTestActivity", percent+"");
						mHandler.post((new Runnable() {
							@Override
							public void run() {				
//								Log.e("MemTestActivity", "##################################################");
//								Log.e("MemTestActivity", "percent : " + percent);
//								Log.e("MemTestActivity", "currentLoopTime : " + currentLoopTime);
//								Log.e("MemTestActivity", "mLoopTimeFlag : " + mLoopTimeFlag);
//								Log.e("MemTestActivity", "##################################################");
								mProgressView.setPercent((int)((percent+ (currentLoopTime*1f) * 100)/((mLoopTimeFlag*1f) * 100)*100));
								mCurrentPercent.setText((int)((percent+ (currentLoopTime*1f) * 100)/((mLoopTimeFlag*1f) * 100)*100) + "%");
								if (currentLoopTime == (mLoopTimeFlag - 1) && !TestingFlag && !StopTestFlag) {
									mProgressView.setPercent(100);
									mCurrentPercent.setText(100 + "%");
								}
								//Log.e("MemTestActivity", ""+(percent+ currentLoopTime * 100)/(mLoopTimeFlag * 100)*100);
							}
						}));
						
					}
				};
			}.start();
			
		}else{
			//停止测试提示
			if (!TestingFlag) {
				return;
			}
			//设置测试中标志false
			TestingFlag = false;
			//提示停止测试，将从头测试
			
			
			//停止循环
			StopTestFlag = true;
		}	
	}

	private class TestTask extends AsyncTask<Void, Integer, Integer> {
		private int LooperTime;
		public TestTask(int LooperTime){
			this.LooperTime = LooperTime;
		}
		// 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
		@Override
		protected void onPreExecute() {
			TestingFlag = true;
			currentLoopTime = 0;
			// textView.setText("开始执行异步线程");
			Log.e("TestTask", "onPreExecute");
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			for (int i = 0; i < LooperTime; i++) {
				//点击了停止
				if (StopTestFlag) {
					return null;
				}	
				// 结果添加到集合中
				currentLoopTime = i;
				int flag = mStabilityTest.StartMestTest(mMemSizeFlag);
				
				// 先处理数据
				// 如果放回0， 说明正常
				// 如果返回1， 说明是参数错误
				// 如果返回2， 说明是内存地址有问题。
				// 如果返回4， 说明是内存存取有问题。
				int testResult = flag;
				ResultBean rBean = new ResultBean();
				rBean.success = ((testResult & NORMAL_FLAG) == NORMAL_FLAG) ? TRUE_FLAG
						: FALSE_FLAG;
				rBean.param = ((testResult & PARAM_ERROR_FLAG) == PARAM_ERROR_FLAG) ? TRUE_FLAG
						: FALSE_FLAG;
				rBean.memAddr = ((testResult & MEM_ADDR_ERROR_FLAG) == MEM_ADDR_ERROR_FLAG) ? TRUE_FLAG
						: FALSE_FLAG;
				rBean.memRw = ((testResult & MEM_ADDR_GET_FLAG_FLAG) == MEM_ADDR_GET_FLAG_FLAG) ? TRUE_FLAG
						: FALSE_FLAG;
				mResultList.add(rBean);
				
				publishProgress(i);  
				Log.e("TestTask", "doInBackground " + i);
			}
			return null;
		}

		/**
		 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
		 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
		 */
		@Override
		protected void onPostExecute(Integer result) {
			TestingFlag = false;
			if (StopTestFlag) {
				mProgressView.setPercent(0);
				mCurrentPercent.setText(0 + "%");
				//清除数据缓存
				mResultList.clear();
				//UI数据清除
				mAdapter.notifyDataSetChanged();
				
				mStabilityTest.StopMemTest();
				return;
			}
			Log.e("TestTask", "onPostExecute ");
			// textView.setText("异步操作执行结束" + result);
			// 结束后，进行UI的变化
			
			mAdapter.notifyDataSetChanged();
		}

		/**
		 * 这里的Intege参数对应AsyncTask中的第二个参数
		 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
		 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			
			Log.e("onProgressUpdate", "onProgressUpdate ");
			mAdapter.notifyDataSetChanged();
		
		}
	}

	/**
	 * -----------------------------测试结果数据适配器----------------------------------
	 * -----
	 **/
	private class ResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mResultList.size()+1;
		}

		@Override
		public Object getItem(int arg0) {
			return mResultList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(MemTestActivity.this,
						R.layout.item_ui_stability_mem_test, null);
				holder = new ViewHolder();
				holder.time = (FlatTextView) convertView
						.findViewById(R.id.test_time);
				holder.success = (FlatTextView) convertView
						.findViewById(R.id.test_success);
				holder.error_param = (FlatTextView) convertView
						.findViewById(R.id.test_error_param);
				holder.error_memAdd = (FlatTextView) convertView
						.findViewById(R.id.test_error_memadd);
				holder.error_memRw = (FlatTextView) convertView
						.findViewById(R.id.test_error_memrw);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (position == 0) {
				holder.time.setText("Test times");	
				holder.success.setText("success");	
				holder.error_param.setText("param error");	
				holder.error_memAdd.setText("mem addr error");	
				holder.error_memRw.setText("mem rw error");
			}else {
				ResultBean bean = mResultList.get(position - 1);
				holder.time.setText(position+"");	
				holder.success.setText((bean.success == TRUE_FLAG)?"√":"-");	
				holder.error_param.setText((bean.param == TRUE_FLAG)?"×":"-");	
				holder.error_memAdd.setText((bean.memAddr == TRUE_FLAG)?"×":"-");	
				holder.error_memRw.setText((bean.memRw == TRUE_FLAG)?"×":"-");
			}
			return convertView;
		}
	}
	static class ViewHolder {
		FlatTextView time;
		FlatTextView success;
		FlatTextView error_param;
		FlatTextView error_memAdd;
		FlatTextView error_memRw;
	}
	private class ResultBean {
		int success;
		int param;
		int memAddr;
		int memRw;
	}
}
