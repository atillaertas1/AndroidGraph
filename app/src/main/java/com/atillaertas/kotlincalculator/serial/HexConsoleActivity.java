package com.atillaertas.kotlincalculator.serial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atillaertas.kotlincalculator.R;
import com.atillaertas.kotlincalculator.helper.HexHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HexConsoleActivity extends SerialPortActivity {
    private final String TAG = HexConsoleActivity.class.getSimpleName();

    EditText mReception;
    Button mButtonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serial_hex_console);

        final EditText Emission = findViewById(R.id.EditHexTextEmission);
        mReception = findViewById(R.id.EditHexTextReception);
        mButtonSend = findViewById(R.id.btnHexSend);

        //按下SEND按钮，即可将Emission中的内容通过串口发送出去
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = Emission.getText().toString();
                byte[] byteHex = HexHelper.getInstance().hexToByteArray(string);
                try {
                    mOutputStream.write(byteHex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDataReceived(final byte[]  buffer, final int size) throws UnsupportedEncodingException {
        final String string = HexHelper.getInstance().bytesToHex(buffer, size);
        Log.d(TAG,"onDataReceived buffer = " + string);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(string);
                }
            }
        });
    }
}
