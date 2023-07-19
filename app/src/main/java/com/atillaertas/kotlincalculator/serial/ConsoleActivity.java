package com.atillaertas.kotlincalculator.serial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atillaertas.kotlincalculator.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ConsoleActivity extends SerialPortActivity {
	private final String TAG = ConsoleActivity.class.getSimpleName();
	EditText mReception;
	Button mButtonSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window. FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//				WindowManager.LayoutParams. FLAG_FULLSCREEN);

		setContentView(R.layout.activity_serial_console);

		final EditText Emission = findViewById(R.id.EditTextEmission);
		mReception = findViewById(R.id.EditTextReception);
		mButtonSend = findViewById(R.id.btnSend);

		//按下SEND按钮，即可将Emission中的内容通过串口发送出去
		mButtonSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String string = Emission.getText().toString();
				//Integer in = new Integer(0xfa);
				try {
					//mOutputStream.write(in.byteValue());
					mOutputStream.write(string.getBytes());
					//mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) throws UnsupportedEncodingException {
		String string = new String(buffer,0,size);
		Log.d(TAG,"onDataReceived buffer = " + string);

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null) {
					mReception.append(new String(buffer, 0, size));
				}
			}
		});
	}
}
