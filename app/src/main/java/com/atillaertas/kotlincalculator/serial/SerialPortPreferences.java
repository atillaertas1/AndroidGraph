package com.atillaertas.kotlincalculator.serial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



import android_serialport_api.SerialPortFinder;

public class SerialPortPreferences extends AppCompatActivity{

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		SharedPreferences sharedPreferences = getSharedPreferences("com.atillaertas.kotlincalculator.serial", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("DEVICE","dev/ttyS4");
		editor.putString("BAUDRATE","115200");
		editor.apply();
	}
}
