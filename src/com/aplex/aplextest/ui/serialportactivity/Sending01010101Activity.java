package com.aplex.aplextest.ui.serialportactivity;

import android.os.Bundle;

import com.aplex.aplextest.R;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by APLEX on 2016/6/24.
 */
public class Sending01010101Activity extends SerialPortActivity {

    SendingThread mSendingThread;
    byte[] mBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending01010101);
        mBuffer = new byte[1024];
        Arrays.fill(mBuffer, (byte) 0x55);
        if (mSerialPort != null) {
            mSendingThread = new SendingThread();
            mSendingThread.start();
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        // ignore incoming data
    }

    private class SendingThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {

                    if (mOutputStream != null) {
                        mOutputStream.write(mBuffer);
                    } else {
                        return;
                    }
                    Thread.sleep(2000);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mSendingThread != null)
            mSendingThread.interrupt();
        super.onDestroy();
    }
}
