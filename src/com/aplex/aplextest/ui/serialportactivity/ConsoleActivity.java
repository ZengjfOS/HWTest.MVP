package com.aplex.aplextest.ui.serialportactivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.view.FlatButton;
import com.aplex.aplextest.flatui.view.FlatEditText;

/**
 * Created by APLEX on 2016/6/24.
 */
public class ConsoleActivity extends SerialPortActivity{
    private FlatEditText mReception;
    private FlatEditText Emission;
    private FlatButton   mEmissonBtn;
    private FlatButton   mAutoSendBtn;
    private boolean isAutoSending = false;
    private boolean mAutoSend     = false;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_console);
        mReception = (FlatEditText) findViewById(R.id.EditTextReception);
        mEmissonBtn = (FlatButton) findViewById(R.id.Emission_btn);
        mAutoSendBtn = (FlatButton) findViewById(R.id.send_auto);
        Emission = (FlatEditText) findViewById(R.id.EditTextEmission);
        sdf = new SimpleDateFormat("HH:mm:ss");
        mEmissonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoSend){
                    if (!isAutoSending) {
                        isAutoSending = !isAutoSending;
                        new Thread(){
                            @Override
                            public void run() {
                                while (mAutoSend){
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendMessage();
                                        }
                                    });
                                }
                            }
                        }.start();
                    }
                }else{
                    sendMessage();
                }
            }
        });
        mAutoSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoSend){
                    mAutoSendBtn.setText(getResources().getString(R.string.send_auto_off));
                }else{
                    mAutoSendBtn.setText(getResources().getString(R.string.send_auto_on));
                }
                mAutoSend = !mAutoSend;
                if (isAutoSending){
                    isAutoSending = !isAutoSending;
                }
            }
        });

    }
    private void sendMessage(){
        Date date = new Date();
        String format = sdf.format(date);
        String s = format + " : " +Emission.getText().toString();
        char[] text = new char[s.length()];
        int i ;
        for (i=0; i<s.length(); i++) {
            text[i] = s.charAt(i);
        }
        try {
            mOutputStream.write(new String(text).getBytes());
            mOutputStream.write("\n\r".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                	if (mReception.getLineCount() >= 100) {
						mReception.setText("");
					}
                    mReception.append(new String(buffer, 0, size));
                }
            }
        });
    }
}
