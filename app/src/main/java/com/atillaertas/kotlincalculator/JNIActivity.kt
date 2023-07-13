package com.atillaertas.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class JNIActivity : AppCompatActivity() {
    companion object{
        init {
            System.loadLibrary("")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jniactivity)

    }
}