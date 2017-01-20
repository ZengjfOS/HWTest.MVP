package com.aplex.aplextest.ui.serialportactivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.aplex.aplextest.Application;
import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.FlatTextView;
import com.aplex.aplextest.utils.SPUtils;

/**
 * Created by APLEX on 2016/6/23.
 */
public class SerialPortPreferences extends Activity {

    private Application      mApplication;
    private SerialPortFinder mSerialPortFinder;

    private SetupAdapter mSetupAdapter;

    private ListView     mSetupListView;
    /**
     * 数组用来给dialog设置单选参数使用
     */
    private String[]     mDevicePaths;
    private String[]     mDevice;
    String[] mmfilterDevice;
    String[] mmfilterDevicePaths;
    private String[]     mBaudrateNames;
    private String[]     mBaudrateValues;
    
    
    /**
     * list用来获取position使用
     */
    private List<String> mDeviceList;
    private List<String> mDevicePathsList;
    List<String> mfilterDeviceList;
    List<String> mfilterDevicePathList;
    
    private List<String> mBaudrateValuesList;
    /**
     * titleDatas listview适配的大标题数据
     */
    private String[] titleDatas = {"Device", "Baud rate"};
    /**
     * smallTitleDatas listview适配的小标题数据
     */
    private String[] smallTitleDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_setup);
        SPUtils.setApplication(getApplication());
        SPUtils.setSPFileName("config");
        initData();
        initView();
        initListener();
        
    }

    private void initListener() {
        mSetupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) showDeviceDialog();
                if (position == 1) showBaudRate();
            }
        });
    }
    private AlertDialog mBaudRateDialog;
    private void showBaudRate() {

        int checkedId = 0;
        if (mBaudrateValuesList.contains(SPUtils.getString("BAUDRATE"))) {
            checkedId = mBaudrateValuesList.indexOf(SPUtils.getString("BAUDRATE"));
        }
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
		builder.setTitle("Baud rate");
		builder.setSingleChoiceItems(mBaudrateValues, checkedId,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SPUtils.pushString("BAUDRATE", mBaudrateValuesList.get(which));
		                smallTitleDatas[1] = mBaudrateValuesList.get(which);
		                mSetupAdapter.notifyDataSetChanged();
		                if (mBaudRateDialog != null) {
		                	mBaudRateDialog.dismiss();
						}
					}
				});
		
		builder.setCancelable(true);
		mBaudRateDialog = builder.show();
    }
    private AlertDialog mDeviceDialog;
    private void showDeviceDialog() {
      int checkedId = 0;
      if (mfilterDevicePathList.contains(SPUtils.getString("DEVICE"))) {
          checkedId = mfilterDevicePathList.indexOf(SPUtils.getString("DEVICE"));
      }
        final AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
		builder.setTitle("Device");
		builder.setSingleChoiceItems(mmfilterDevice, checkedId,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 SPUtils.pushString("DEVICE", mfilterDevicePathList.get(which));
		                smallTitleDatas[0] = mfilterDevicePathList.get(which);
		                mSetupAdapter.notifyDataSetChanged();
		                if (mDeviceDialog != null) {
		                	mDeviceDialog.dismiss();
						}
					}
				});
		builder.setCancelable(true);
		mDeviceDialog = builder.show();
//        int checkedId = 0;
//        if (mDevicePathsList.contains(SPUtils.getString("DEVICE"))) {
//            checkedId = mDevicePathsList.indexOf(SPUtils.getString("DEVICE"));
//        }
//        final CanDialog.Builder builder = new CanDialog.Builder(this);
//        builder.setTitle("Device");
//        builder.setSingleChoiceItems(mDevice, checkedId
//                , new CanDialogInterface.OnClickListener() {
//            @Override
//            public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
//                SPUtils.pushString("DEVICE", mDevicePathsList.get(position));
//                smallTitleDatas[0] = mDevicePathsList.get(position);
//                mSetupAdapter.notifyDataSetChanged();
//                builder.create().dismiss();
//            }
//        });
//        builder.setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT);
//        builder.setCancelable(true);
//        builder.show();
    }
   
    private void initData() {
        /**获得application**/
        mApplication = (Application) getApplication();
        /**串口寻找者**/
        mSerialPortFinder = mApplication.mSerialPortFinder;
        /**获取设备名和设备路径**/
        mDevice = mSerialPortFinder.getAllDevices();
        mDevicePaths = mSerialPortFinder.getAllDevicesPath();
        
        mDeviceList = Arrays.asList(mDevice);
        mDevicePathsList = Arrays.asList(mDevicePaths);
        
        /********************************过滤设备节点 ***************************/
        mfilterDeviceList = new ArrayList<String>();
        for (String string : mDevice) {
			if (string.contains("IMX")) {
				mfilterDeviceList.add(string);
			}
		}
        //排序
        Collections.sort(mfilterDeviceList);
        mmfilterDevice = new String[mfilterDeviceList.size()];
        for (int i = 0; i < mmfilterDevice.length; i++) {
        	mmfilterDevice[i]=mfilterDeviceList.get(i);
		}
        /************************************************************************/
  
        
         /******************************过滤设备节点 路径**************************/
        mfilterDevicePathList = new ArrayList<String>();
        for (String string : mDevicePaths) {
			if (string.contains("mxc")) {
				mfilterDevicePathList.add(string);
			}
		}
        //排序
        Collections.sort(mfilterDevicePathList);
        mmfilterDevicePaths = new String[mfilterDevicePathList.size()];
        for (int i = 0; i < mmfilterDevicePaths.length; i++) {
        	mmfilterDevicePaths[i]=mfilterDevicePathList.get(i);
		}
        

        /*************************************************************************/
        
        /**获取listView的小标题**/
        smallTitleDatas = new String[2];
        smallTitleDatas[0] = SPUtils.getString("DEVICE").equals("")?mmfilterDevice[0]:SPUtils.getString("DEVICE");
        smallTitleDatas[1] = SPUtils.getString("BAUDRATE").equals("")?getResources().getStringArray(R.array.baudrates_value)[0]:SPUtils.getString("BAUDRATE");
        /**只用value**/
        mBaudrateNames = getResources().getStringArray(R.array.baudrates_name);
        mBaudrateValues = getResources().getStringArray(R.array.baudrates_value);
        mBaudrateValuesList = Arrays.asList(mBaudrateValues);
    }

    private void initView() {
        mSetupListView = (ListView) findViewById(R.id.setup_lv);
        if (mSetupAdapter == null) {
            mSetupAdapter = new SetupAdapter();
        }
        mSetupListView.setAdapter(mSetupAdapter);
    }

    private class SetupAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return titleDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return titleDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(SerialPortPreferences.this, R.layout.item_ui_setup, null);
                holder = new ViewHolder();
                holder.title = (FlatTextView) convertView.findViewById(R.id.setup_title);
                holder.smallTitle = (FlatTextView) convertView.findViewById(R.id.setup_small_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(titleDatas[position]);
            holder.smallTitle.setText(smallTitleDatas[position]);

            return convertView;
        }
    }

    static class ViewHolder {
        FlatTextView title;
        FlatTextView smallTitle;
    }
}
