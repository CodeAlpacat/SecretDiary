package com.example.secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextMenu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {


    private val handler = Handler(Looper.getMainLooper()) //main쓰레드에 연결된 핸들러가 됨.

    private val diaryEditText: EditText by lazy {
        findViewById<EditText>(R.id.diaryEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        //sharedpreferences에서 detail 값을 불러오지만 값의 디폴트는 공백이다.
        diaryEditText.setText(detailPreferences.getString("detail", ""))

        //쓰레드기능
        //changePassword에서 사용한 commit을 true로 UI를 블럭했지만 이번엔 apply로 사용. 기다리지 않고 계속 백그라운드에서 저장함.
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
        }
        //postdelay : 몇 초 후에 쓰레드를 실행시키는 기능
        //addTextChanged Listner : 텍스트가 변경될 때마다 리스너가 호출된다.
        //이대로 SharedPreferences를 사용해 저장하면 너무 자주 저장하므로 0.5초 멈추면 저장.
        //runnable은 메인쓰레드가 아니므로 메인쓰레드에서 사용하기위해 연결해주려면 handler가 필요하다.
         diaryEditText.addTextChangedListener{
            handler.removeCallbacks(runnable) //0.5초 이전에 실행되지않으면 지워줌.
            handler.postDelayed(runnable, 500)
            }
        }


    }