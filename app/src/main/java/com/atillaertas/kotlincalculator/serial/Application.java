package com.atillaertas.kotlincalculator.serial;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.util.Log;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;

	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			String path = "dev/ttyS3";
			int baudrate = 921600;
			Log.d("devicename","" + path);
			Log.d("baudrate","" + baudrate);

			/* Check parameters */
			/*
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}*/

			System.out.println("ysjie path = " + path + ",baudrate = " + baudrate);
			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
