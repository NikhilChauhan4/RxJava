package com.example.rxjava

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var openRxJavaButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openRxJavaButton = findViewById(R.id.open_rx_java_button)
        openRxJavaButton.setOnClickListener {
            val intent: Intent = Intent(this, VideoDownloadActivity::class.java)
            startActivity(intent)
        }
    }
}