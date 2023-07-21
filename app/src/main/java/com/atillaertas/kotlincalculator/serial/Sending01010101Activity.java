package com.atillaertas.kotlincalculator.serial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atillaertas.kotlincalculator.R;

import java.io.IOException;
import java.util.Arrays;

public class Sending01010101Activity extends SerialPortActivity {

	SendingThread mSendingThread;
	byte[] mBuffer;
	byte value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serial_sending01010101);
		TextView textView = findViewById(R.id.textView1);
		Button backButton = findViewById(R.id.backButton);


		mBuffer = new byte[128];
		value = 0x59;
		String binarString = Integer.toBinaryString(value);

		Arrays.fill(mBuffer, (byte) value);

		if (mSerialPort != null) {
			mSendingThread = new SendingThread();
			mSendingThread.start();
		}
		textView.setText("Sending" + " " + binarString);

		backButton.setOnClickListener(view -> startActivity(new Intent(Sending01010101Activity.this,SerialMainMenu.class)));


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
						sleep(10);
					} else {
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
