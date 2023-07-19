package com.atillaertas.kotlincalculator.serial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atillaertas.kotlincalculator.R;

public class SerialMainMenu extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_main_menu);


        final Button buttonConsole = (Button)findViewById(R.id.ButtonConsole);
        buttonConsole.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SerialMainMenu.this, ConsoleActivity.class));
			}
		});

		final Button buttonHexConsole = (Button)findViewById(R.id.ButtonHexConsole);
		buttonHexConsole.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SerialMainMenu.this, HexConsoleActivity.class));
			}
		});

        final Button buttonLoopback = (Button)findViewById(R.id.ButtonLoopback);
        buttonLoopback.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SerialMainMenu.this, LoopbackActivity.class));
			}
		});

        final Button button01010101 = (Button)findViewById(R.id.Button01010101);
        button01010101.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SerialMainMenu.this, Sending01010101Activity.class));
			}
		});

//        final Button buttonAbout = (Button)findViewById(R.id.ButtonAbout);
//        buttonAbout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
//				builder.setTitle("About");
//				builder.setMessage(R.string.about_msg);
//				builder.show();
//			}
//		});

        final Button buttonQuit = (Button)findViewById(R.id.ButtonQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SerialMainMenu.this.finish();
			}
		});
    }
}
