package com.aplex.aplextest.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.ServiceManager;
import android.os.INetworkManagementService;
import android.net.InterfaceConfiguration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.fragment.ethernet.EthernetConfigDialog;
import com.aplex.aplextest.fragment.ethernet.EthernetDevInfo;
import com.aplex.aplextest.fragment.ethernet.EthernetEnabler;


public class EthernetAutoConfFragment extends Fragment{
	private String MacAddr;
	private LinearLayout mRootView;
	private EthernetEnabler mEthEnabler;
	private EthernetConfigDialog mEthConfigDialog;
	private FlatButton mBtnInfo;
	private FlatButton mBtnConfig;
//	private Button mBtnCheck;
	private EthernetDevInfo  mSaveConfig;
//	private String TAG = "MainActivity";
//	private static String Mode_dhcp = "dhcp";
//	private boolean shareprefences_flag = false;
//	private boolean first_run = true;
	public static final String FIRST_RUN = "ethernet";
	public boolean isInit = false;
//	private Button mBtnAdvanced;
//	private EthernetAdvDialog mEthAdvancedDialog;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);      
        mRootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
        return mRootView;
    }
	@Override
	public void onResume() {
		super.onResume();
		initData();
	    initListener();
	}
	
	private void initData() {
		mEthEnabler = new EthernetEnabler(getActivity());
		if (!isInit) {
			new Thread(){
				public void run() {
					isInit = mEthEnabler.getManager().initProxy();
				};
			}.start();
		}
        mEthConfigDialog = new EthernetConfigDialog(getActivity(), mEthEnabler);
        mEthEnabler.setConfigDialog(mEthConfigDialog);
//        mEthAdvancedDialog = new EthernetAdvDialog(getActivity(), mEthEnabler);
//        mEthEnabler.setmEthAdvancedDialog(mEthAdvancedDialog);
        MacAddr = getMacAddrByOs();
	}
	private void initListener() {
		addListenerOnBtnConfig();
        addListenerOnBtnCheck();
        //addListenerOnBtnAdvanced();
		
	}
	private void initView(LayoutInflater inflater) {
		 mRootView = (LinearLayout) inflater.inflate(R.layout.ui_ethernet_configure, null);
	}
	//显示配置的dialog
	public void addListenerOnBtnConfig() {
        mBtnConfig = (FlatButton) mRootView.findViewById(R.id.btnConfig);

        mBtnConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEthConfigDialog.show();
            }
        });
    }
	//显示配置信息
    public void addListenerOnBtnCheck() {
    	mBtnInfo = (FlatButton) mRootView.findViewById(R.id.btnCheck);
    	mBtnInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) mRootView.findViewById(R.id.tvConfig);
                text.setMovementMethod(ScrollingMovementMethod.getInstance());
                mSaveConfig = mEthEnabler.getManager().getSavedConfig();
                if (mSaveConfig != null) {
                    final String config_detail = 
                    		"\nEthernet Devices : " + mEthEnabler.getManager().getDeviceNameList()[0] + "\n"
                    		+ "Connection Type : " + mEthEnabler.getManager().getSharedPreMode() + "\n"
                            + "IP Address      : " + mEthEnabler.getManager().getSharedPreIpAddress() + "\n"
                    		+ "Netmask         : " + mEthEnabler.getManager().getSharedPreNetmask() + "\n"
                    		+ "Gateway         : " + mEthEnabler.getManager().getSharedPreRoute() + "\n"
                            + "DNS Address     : " + mEthEnabler.getManager().getSharedPreDnsAddress() + "\n"
                    		+ "Mac Address     : " + MacAddr + "\n";
                            //+ "Proxy Address : " + mEthEnabler.getManager().getSharedPreProxyAddress() + "\n"
                            //+ "Proxy Port    : " + mEthEnabler.getManager().getSharedPreProxyPort() + "\n";
                    text.setText(config_detail);
                }
            }
        });
    }
    public void addListenerOnBtnAdvanced() {
    	/*
        mBtnAdvanced = (Button) findViewById(R.id.btnAdvanced);

        mBtnAdvanced.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveConfig = mEthEnabler.getManager().getSavedConfig();
                if (mSaveConfig != null) {
                    mEthAdvancedDialog.show();
                }
            }
        });
        */
    }
    
    /**
     * 获取mac地址
     * @return
     */
    public String getMacAddress(){   
	     String result = "";     
	     String Mac = "";
	     result = callCmd("busybox ifconfig","HWaddr");
	      
	     //如果返回的result == null，则说明网络不可取
	     if(result==null){
	         return "网络出错，请检查网络";
	     }
	      
	     //对该行数据进行解析
	     //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
	     if(result.length()>0 && result.contains("HWaddr")==true && result.contains("encap:Ethernet")){
	         Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
	         Log.i("test","Mac:"+Mac+" Mac.length: "+Mac.length());
	          
	         if(Mac.length()>1){
	             Mac = Mac.replaceAll(" ", "");
	             result = "";
	             String[] tmp = Mac.split(":");
	             for(int i = 0;i<tmp.length;++i){
	                 result +=tmp[i];
	             }
	         }
	         Log.i("test",result+" result.length: "+result.length());            
	     }
	     
	     return parseMacAddr(result);
	 }   
	 private String parseMacAddr(String Mac){
		 String mac="";
		 if (Mac.length() != 12) {
			Toast.makeText(getActivity(), "mac显示地址异常  "+Mac, Toast.LENGTH_LONG).show();
			return "";
		}
		
		for (int i = 0; i < Mac.length(); i++) {
			mac = mac + Mac.charAt(i);
			if (i%2 != 0 && i != Mac.length() - 1) {
				mac = mac + ":";
			}
		}
		return mac;
	 }
	  
	 public String callCmd(String cmd,String filter) {   
	     String result = "";   
	     String line = "";   
	     try {
	         Process proc = Runtime.getRuntime().exec(cmd);
	         InputStreamReader is = new InputStreamReader(proc.getInputStream());   
	         BufferedReader br = new BufferedReader (is);   
	          
	         //执行命令cmd，只取结果中含有filter的这一行
	         while ((line = br.readLine ()) != null && line.contains(filter)== false) {   
	             //result += line;
	             Log.i("test","line: "+line);
	         }
	          
	         result = line;
	         Log.i("test","result: "+result);
	     }   
	     catch(Exception e) {   
	         e.printStackTrace();   
	     }   
	     return result;   
	 }
	 /**
	  * 
	  */
	 public String getMacAddrByOs(){
		String mHwAddr = "";
		IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
		INetworkManagementService mNMService = INetworkManagementService.Stub.asInterface(b);
		try{
			InterfaceConfiguration config = mNMService.getInterfaceConfig("eth0");  //这个eth0是网口端号，实际上也可以用代码来获取；
			mHwAddr = config.getHardwareAddress(); //mHwAddr  String类型
		}catch (RemoteException e) {
			Log.e("ethrnet", "Could not get list of interfaces " + e);
		}
		return mHwAddr;
	 }
}
