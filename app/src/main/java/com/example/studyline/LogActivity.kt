package com.example.studyline

import android.app.LauncherActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        logtv.text=intent.getStringExtra("where")
        logtv2.text=intent.getStringExtra("where1")
        logtv3.text=intent.getStringExtra("where2")
        logtv4.text=intent.getStringExtra("where3")

        Backbt.setOnClickListener(){
            finish()
        }
    }
}
