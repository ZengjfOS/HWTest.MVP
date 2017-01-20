package com.aplex.aplextest.fragment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.flatui.view.FlatEditText;
import com.aplex.aplextest.jninative.I2CController;

/**
 * Created by APLEX on 2016/6/22.
 */
public class EepromFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mRootView;
    private FlatEditText   mAddressBtn;
    private FlatEditText   mAddressOffsetBtn;
    //private FlatButton   mWriteBtn;
    private FlatButton   mReadBtn;
    //private FlatEditText mWriteEdt;
    private FlatEditText mReadEdt;
    private Spinner      mSpinner;
    private String i2c_node ;
    I2CController i2cController = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        i2cController = new I2CController();	//i2c通信需要调用本地接口
        i2c_node = "/dev/i2c-0";
        FlatUI.initDefaultValues(getActivity());
        FlatUI.setDefaultTheme(FlatUI.SKY);
        initView(inflater);
        initListener();
       // writeMessage();
        mRootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
        return mRootView;
    }

    private void initView(LayoutInflater inflater) {
        mRootView = (LinearLayout) inflater.inflate(R.layout.ui_eeprom, null);

        //init spinner
        mSpinner = (Spinner) mRootView.findViewById(R.id.themes_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button, getDevices());
        adapter.setDropDownViewResource(R.layout.simple_flat_list_item);
        mSpinner.setAdapter(adapter);


        //init button
        mAddressBtn = (FlatEditText) mRootView.findViewById(R.id.address_btn);
        mAddressOffsetBtn = (FlatEditText) mRootView.findViewById(R.id.address_offset_btn);
        //mWriteBtn = (FlatButton) mRootView.findViewById(R.id.write_message_btn);
        mReadBtn = (FlatButton) mRootView.findViewById(R.id.read_message_btn);
        mAddressBtn.setText("50");
        mAddressOffsetBtn.setText("00");

        //mWriteEdt = (FlatEditText) mRootView.findViewById(R.id.write_message);
        mReadEdt = (FlatEditText) mRootView.findViewById(R.id.read_message);
    }
    public String[] getDevices() {
        ArrayList<String> mDevices = new ArrayList<String>();
        File dev = new File("/dev");
        //File[] files = dev.listFiles();
        File[] files = dev.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getAbsolutePath().startsWith("/dev/i2c-")) {
                    return true;
                }
                return false;
            }
        });
        int i;
        /**
         * 这里使用这种方式加入节点名，是因为只有这样加，spinner中节点显示才能显示正确的顺序
         */
        for (i=files.length-1; i >= 0; i--) {
            mDevices.add(files[i].getAbsolutePath());
        }
        return (String[])mDevices.toArray(new String[mDevices.size()]);
    }
    private void initListener() {
        //mWriteBtn.setOnClickListener(this);
        mReadBtn.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                /**
                 * 获取节点文件路径
                 */
                //防止空指针
                if (arg1 != null) {
                    i2c_node = ((TextView) arg1).getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
/*            case R.id.write_message_btn:writeMessage();
                break;*/
            case R.id.read_message_btn:readMessage();
                break;
        }
    }

    private void readMessage() {
        Toast.makeText(getActivity(), "read", Toast.LENGTH_LONG).show();
        int writeAddr = Integer.decode("0x"+mAddressBtn.getText().toString());
        int writeOffset = Integer.decode("0x"+mAddressOffsetBtn.getText().toString());
        Log.e("open file ", i2c_node);
        i2cController.fd = i2cController.open(i2c_node, 0);

        Log.e("open file ", i2c_node);
        Log.e("i2cController.fd ", i2cController.fd+"");
        if (i2cController.fd < 3) {
            /**
             * 错误提示
             */
            Toast.makeText(getActivity(), "Missing read/write permission, \ntrying to chmod the file.", Toast.LENGTH_LONG).show();

        } else {
            String readData = i2cController.readStr(i2cController.fd, writeAddr, writeOffset, 256);
            /**
             * 这里和-1进行比较是在jni里设定好的，如果出错，那么返回字符串-1
             */
            if("-1".equals(readData)) {
                Toast.makeText(getActivity(), "Read failed.", Toast.LENGTH_SHORT).show();
            } else {
                mReadEdt.setText(readData);
                Toast.makeText(getActivity(), "Read successed.", Toast.LENGTH_SHORT).show();
            }
        }
        i2cController.close(i2cController.fd);
    }
  
    private void writeMessage() {
    	// TODO Auto-generated method stub
        String writeString = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
        		+"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        int writeAddr = Integer.decode("0x"+mAddressBtn.getText().toString());
        int writeOffset = Integer.decode("0x"+mAddressOffsetBtn.getText().toString());

        Log.e("open file ", i2c_node);

        if (writeString.length() > 0) {

            i2cController.fd = i2cController.open(i2c_node, 0);
            if (i2cController.fd < 3) {
                
                Toast.makeText(getActivity(), "Missing read/write permission, \ntrying to chmod the file.", Toast.LENGTH_LONG).show();

            } else {
                if(i2cController.writeStr(i2cController.fd, writeAddr, writeOffset, writeString, writeString.length()) == writeString.length()) {
                    Toast.makeText(getActivity(), "Write successed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Write failed.", Toast.LENGTH_SHORT).show();
                }
            }
            i2cController.close(i2cController.fd);
        }
    }
    
}
