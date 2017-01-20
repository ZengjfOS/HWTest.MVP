package com.aplex.aplextest.fragment;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.jninative.AplexGPIO;

/**
 * Created by APLEX on 2016/6/22.
 */
public class GPIOFragment extends Fragment {
    /**
     * command type
     */
    public final static int GPIO_IN0_CMD_APLEX = 66;
    public final static int GPIO_IN1_CMD_APLEX = 67;
    public final static int GPIO_IN2_CMD_APLEX = 68;
    public final static int GPIO_IN3_CMD_APLEX = 69;

    public final static int GPIO_OUT0_CMD_APLEX = 70;
    public final static int GPIO_OUT1_CMD_APLEX = 71;
    public final static int GPIO_OUT2_CMD_APLEX = 72;
    public final static int GPIO_OUT3_CMD_APLEX = 73;
    /**
     * 输入按钮
     */
    private FlatButton mInBtn0;
    private FlatButton mInBtn1;
    private FlatButton mInBtn2;
    private FlatButton mInBtn3;

    /**
     * 输出按钮
     */
    private FlatButton mOutBtn0;
    private FlatButton mOutBtn1;
    private FlatButton mOutBtn2;
    private FlatButton mOutBtn3;


    private LinearLayout mRootView;
    /**
     * 定时任务
     */
    //定时任务
    private GetInMessageTask mGetInMessageTask;
    /**
     * Native接口
     */
    private AplexGPIO mAplexGPIO;
    /**
     * 保存本地接口返回的值
     */
    long in0Data = 0;
    long in1Data = 0;
    long in2Data = 0;
    long in3Data = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        initData();
        initListener();
        FlatUI.initDefaultValues(getActivity());
        FlatUI.setDefaultTheme(FlatUI.SKY);
        mRootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
        return mRootView;
    }


    private void initListener() {
        /**
         * 检查设备节点是否存在
         */
        File file = new File("dev/gpio_aplex");
        if (file.exists()) {
            mAplexGPIO.fd = mAplexGPIO.open("/dev/gpio_aplex", 0);

            /**
             * 检查设备节点是否打开正确，0,1,2被系统占用
             */
            if (mAplexGPIO.fd > 2) {
                /**
                 * 设置按键监听事件
                 */
                GPIOOnClick gpioOnClick = new GPIOOnClick();
                mOutBtn0.setOnClickListener(gpioOnClick);
                mOutBtn1.setOnClickListener(gpioOnClick);
                mOutBtn2.setOnClickListener(gpioOnClick);
                mOutBtn3.setOnClickListener(gpioOnClick);

                /**
                 * 开启定时器调度，主要用于更新输入按钮的界面
                 */

            }
        }else {
            /**
             * 文件节点可能不存在
             */
        	Toast.makeText(getActivity(), "Can't find driver node file.", Toast.LENGTH_SHORT).show();
        }
    }
//    @Override
//    public void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        /**
//         * 界面恢复的时候，重新打开文件
//         */
//        if (mAplexGPIO == null) {
//            mAplexGPIO = new AplexGPIO();
//            mAplexGPIO.fd = mAplexGPIO.open("/dev/gpio_aplex", 0);
//        }
//        if ( mGetInMessageTask != null ){
//            mGetInMessageTask = new GetInMessageTask();
//            mGetInMessageTask.startTasK();
//        }
//
//    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        /**
         * 界面隐藏的时候，关闭文件
         */
        if (mAplexGPIO != null)
            mAplexGPIO.close(mAplexGPIO.fd);
        mAplexGPIO = null;
        if ( mGetInMessageTask != null ){
            mGetInMessageTask.stopTask();
        }
        mGetInMessageTask = null;
    }

    private void initData() {
        if (mGetInMessageTask == null){
            mGetInMessageTask = new GetInMessageTask();
        }
        mGetInMessageTask.startTasK();
        mAplexGPIO = new AplexGPIO();
    }


    private void initView(LayoutInflater inflater) {
    	
        mRootView = (LinearLayout) inflater.inflate(R.layout.ui_gpio, null);
        mOutBtn0 = (FlatButton) mRootView.findViewById(R.id.gpio_out_0);
        mOutBtn1 = (FlatButton) mRootView.findViewById(R.id.gpio_out_1);
        mOutBtn2 = (FlatButton) mRootView.findViewById(R.id.gpio_out_2);
        mOutBtn3 = (FlatButton) mRootView.findViewById(R.id.gpio_out_3);

        mInBtn0 = (FlatButton) mRootView.findViewById(R.id.gpio_in_0);
        mInBtn1 = (FlatButton) mRootView.findViewById(R.id.gpio_in_1);
        mInBtn2 = (FlatButton) mRootView.findViewById(R.id.gpio_in_2);
        mInBtn3 = (FlatButton) mRootView.findViewById(R.id.gpio_in_3);
    }
    /***
     * 定时器
     */
    private class GetInMessageTask extends Handler implements Runnable{
        @Override
        public void run() {
            /**
             * 获取GPIO 输入端口的状态
             */
            if (mAplexGPIO != null) {
                in0Data = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN0_CMD_APLEX, 0);
                in1Data = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN1_CMD_APLEX, 0);
                in2Data = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN2_CMD_APLEX, 0);
                in3Data = mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_IN3_CMD_APLEX, 0);

                /**
                 * 在按钮上显示当前的IO口状态
                 */
                String text = mInBtn0.getText().toString();
                mInBtn0.setText(text.substring(0, text.length() - 1) + (in0Data != 0 ? "H" : "L"));

                text = mInBtn1.getText().toString();
                mInBtn1.setText(text.substring(0, text.length() - 1) + (in1Data != 0 ? "H" : "L"));

                text = mInBtn2.getText().toString();
                mInBtn2.setText(text.substring(0, text.length() - 1) + (in2Data != 0 ? "H" : "L"));

                text = mInBtn3.getText().toString();
                mInBtn3.setText(text.substring(0, text.length() - 1) + (in3Data != 0 ? "H" : "L"));
                this.postDelayed(this, 300);
            }
        }
        public void startTasK(){
           stopTask();
            this.postDelayed(this,300);
        }
        public void stopTask(){
            this.removeCallbacks(this);
        }
    }
    class GPIOOnClick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

            /**
             * 获取当前按下的按钮
             */
            Button currentButton = (Button)arg0;
            /**
             * 获取按钮上的文字
             */
            String text = currentButton.getText().toString();
            /**
             * 获取按钮号，主要是因为，按钮文字里面正好有相应的按钮号
             */
            int buttonNumber = Integer.valueOf(""+text.charAt(6));
            /**
             * 根据按钮上的文字最后的字符来判断当前按钮的状态,并做对应的事情
             */

            if (text.endsWith("_H")) {
                currentButton.setText(text.replace('H', 'L'));
                /**
                 * 设置状态1，也就是高电平
                 */
                setGPIOStatus(buttonNumber, 1);
            } else {
                currentButton.setText(text.replace('L', 'H'));
                /**
                 * 设置状态0，也就是低电平
                 */
                setGPIOStatus(buttonNumber, 0);

            }
        }

        private void setGPIOStatus(int buttonNumber, int status) {
            switch (buttonNumber) {
                case 0:
                    mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT0_CMD_APLEX, status);
                    break;
                case 1:
                    mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT1_CMD_APLEX, status);
                    break;
                case 2:
                    mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT2_CMD_APLEX, status);
                    break;
                case 3:
                    mAplexGPIO.ioctl(mAplexGPIO.fd, GPIO_OUT3_CMD_APLEX, status);
                    break;
            }
        }

    }
}




