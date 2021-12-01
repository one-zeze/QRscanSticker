package com.example.studyline

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_log.*
import java.sql.Time
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //하루가 지나면 프리퍼런스 초기화를 위한 시간
        val AppStartTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("실행시간: dd일 HH시 mm분 ss초"))

        //QR코드 정보(ex건물정보) 키, 값으로 저장
        loadData()
        LogBt.setOnClickListener(){
            val Intent = Intent(this, LogActivity::class.java)
            val pref = getSharedPreferences("pref", 0)
            val where = pref.getString("QRwhere", "")
            val where1 = pref.getString("1QRwhere", "")
            val where2 = pref.getString("2QRwhere", "")
            val where3 = pref.getString("3QRwhere", "")
            val where4 = pref.getString("4QRwhere", "")

            Intent.putExtra("where", where)
            Intent.putExtra("where1", where1)
            Intent.putExtra("where2", where2)
            Intent.putExtra("where3", where3)
            Intent.putExtra("where4", where4)
            startActivity(Intent)
        }
    }

    //자동초기화
    fun timer() {
        val pref = getSharedPreferences("pref", 0)
        val editor = pref.edit()
        var second: Int = 0
        kotlin.concurrent.timer(period = 1000, initialDelay = 1000) {
            second++

            if (second == 43200) {
                editor.clear().apply()
            }
        }
        return
    }

    fun QRcodeScan(view: android.view.View) {
        IntentIntegrator(this).initiateScan()
    }

    fun setImage(Time: Int) {
        var default: Int = 0

        if (Time == 1) {
            default = R.drawable.sunday_view
        }
        if (Time == 2) {
            default = R.drawable.monday_view
        }
        if (Time == 3) {
            default = R.drawable.tuesday_view
        }
        if (Time == 4) {
            default = R.drawable.wednesday_view
        }
        if (Time == 5) {
            default = R.drawable.thursday_view
        }
        if (Time == 6) {
            default = R.drawable.friday_view
        }
        if (Time == 7) {
            default = R.drawable.saturday_view
        }
        if (Time == 8) {
            default = R.drawable.duduwithmask
            Toast.makeText(this, "입장 기록이 없습니다.", Toast.LENGTH_LONG).show()
        }
        dayImage.setImageResource(default)
    }

    private fun saveData(today: Int, QRbuilding: String) {
        val pref = getSharedPreferences("pref", 0)
        val editor = pref.edit()
        val ddwhere = pref.getString("QRwhere", "none")
        val ddwhere1 = pref.getString("1QRwhere", "none")
        val ddwhere2 = pref.getString("2QRwhere", "none")
        val ddwhere3 = pref.getString("3QRwhere", "none")
        /*var count: Int = 0*/

        editor.putInt("today", today).apply()

        if (ddwhere == "none")
            editor.putString("QRwhere", QRbuilding).apply()
        else if (ddwhere1 == "none")
            editor.putString("1QRwhere", QRbuilding).apply()
        else if (ddwhere2 == "none")
            editor.putString("2QRwhere", QRbuilding).apply()
        else if (ddwhere3 == "none")
            editor.putString("3QRwhere", QRbuilding).apply()

    }

    private fun loadData() {
        val pref = getSharedPreferences("pref", 0)
        val todaytime = pref.getInt("today", 8)
        setImage(todaytime)
    }

    fun resetData(view: View) {
        val pref = getSharedPreferences("pref", 0)
        val editor = pref.edit()
        editor.clear().apply()
        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        setImage(8)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val ScanTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분 ss초"))
        val Day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)


        if (result != null) {
            if (result.contents != null) {

                val QRbuilding = "\n장소 :" + result.contents.toString() + "\n시간 :" + ScanTime
                timer()
                setImage(Time = Day)
                saveData(Day, QRbuilding)
                Toast.makeText(this, ScanTime.toString() + "\n" + result.contents, Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(this, "QR코드 내용이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

    }
}