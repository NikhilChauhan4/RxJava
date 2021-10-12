package com.example.rxjava

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var openRxJavaButton: Button
    lateinit var openCoroutinesButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openRxJavaButton = findViewById(R.id.open_rx_java_button)
        openCoroutinesButton = findViewById(R.id.open_coroutines)
        openRxJavaButton.setOnClickListener {
            val intent = Intent(this, VideoDownloadActivity::class.java)
            startActivity(intent)
        }

        openCoroutinesButton.setOnClickListener {
           val intent = Intent(this,CoroutinesActivity::class.java)
           startActivity(intent)
        }
    }
}