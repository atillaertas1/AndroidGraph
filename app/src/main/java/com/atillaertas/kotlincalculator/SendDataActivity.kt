package com.atillaertas.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SendDataActivity : AppCompatActivity() {
    val devicePath = "/dev/ttyS0"
    val baudrate = 9600
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_data)
    }
}