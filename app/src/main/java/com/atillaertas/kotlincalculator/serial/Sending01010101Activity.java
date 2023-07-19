package com.atillaertas.kotlincalculator.serial;

import android.os.Bundle;

import com.atillaertas.kotlincalculator.R;

import java.io.IOException;
import java.util.Arrays;

public class Sending01010101Activity extends SerialPortActivity {

	SendingThread mSendingThread;
	byte[] mBuffer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serial_sending01010101);
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
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
}
