package com.aplex.aplextest.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.FlatTextView;

@SuppressLint("NewApi")
public class AboutFragment extends Fragment {
	private LinearLayout mRootView;
	private FlatTextView mCupModel;
	private FlatTextView mCupFrequency;
	private FlatTextView mHardWare;
	
	private FlatTextView mMemTotalSize;
	
	private FlatTextView mEmmcTotalSize;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView(inflater);
		initData();
		initListener();
		mRootView.setBackground(getResources().getDrawable(
				R.color.viewPagerTabBackground));
		return mRootView;
	}

	private void initListener() {
		// TODO Auto-generated method stub

	}

	private void initData() {
		mCupModel.setText(getCpuName());
		String maxCpuFreq = getMaxCpuFreq();
		mCupFrequency.setText((Integer.valueOf(maxCpuFreq)/1000)+"MHZ");
		mHardWare.setText(getHardWare());
		mMemTotalSize.setText(getTotalMemory());
		mEmmcTotalSize.setText(getTotalInternalMemorySize());

	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_about, null);
		mCupModel = (FlatTextView) mRootView.findViewById(R.id.cpu_model);
		mCupFrequency = (FlatTextView) mRootView.findViewById(R.id.frequency);
		mHardWare = (FlatTextView) mRootView.findViewById(R.id.hardware);
		mMemTotalSize = (FlatTextView) mRootView.findViewById(R.id.mem_total);
		mEmmcTotalSize = (FlatTextView) mRootView.findViewById(R.id.intarnal_total);
	}

	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 获取CPU最小频率（单位KHZ）
	public static String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 实时获取CPU当前频率（单位KHZ）
	public static String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader(
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取CPU名字
	public static String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
			}
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	// 获取CPU名字
	public static String getCpuHardware() {
		return Build.HARDWARE;
	}
	public static String getHardWare() {
		String filter = "Hardware";
		String line ="";
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine ()) != null && line.contains(filter)== false) {   
		        //result += line;
		        Log.i("test","line: "+line);
		    }
			String[] array = line.split(":\\s+", 2);
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
     * 获取手机内存大小 
     * 
     * @return 
     */  
    private String getTotalMemory() {  
        String str1 = "/proc/meminfo";// 系统内存信息文件  
        String str2;  
        String[] arrayOfString;  
        long initial_memory = 0;  
        try {  
            FileReader localFileReader = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);  
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小  
  
            arrayOfString = str2.split("\\s+");  
            for (String num : arrayOfString) {  
                Log.i(str2, num + "\t");  
            }  
  
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte  
            localBufferedReader.close();  
  
        } catch (IOException e) {  
        }  
        return Formatter.formatFileSize(getActivity().getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化  
    }  
  
    /** 
     * 获取当前可用内存大小 
     * 
     * @return 
     */  
    private String getAvailMemory() {  
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);  
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();  
        am.getMemoryInfo(mi);  
        return Formatter.formatFileSize(getActivity().getBaseContext(), mi.availMem);  
    }  
    /**
     * 获取手机内部总的存储空间
     * 
     * @return
     */
    public String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(getActivity().getBaseContext(),totalBlocks * blockSize);
    }
}
