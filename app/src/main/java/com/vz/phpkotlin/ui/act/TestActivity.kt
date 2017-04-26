package com.vz.phpkotlin.ui.act

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.vz.phpkotlin.R
import com.vz.phpkotlin.widget.circleprogress.CircleProgress
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity(), View.OnClickListener {

    //    private var btnStop: Button? = null
//    private var btnStart: Button? = null
//    private var btnRestart: Button? = null
    private var circleProgress: CircleProgress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        circleProgress = findViewById(R.id.progress) as CircleProgress?
        btn_stop.setOnClickListener(this@TestActivity)
        btn_start.setOnClickListener(this@TestActivity)
        btn_restart.setOnClickListener(this@TestActivity)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_stop ->{
                circleProgress!!.stopAnim()
            }
            R.id.btn_restart ->{
                circleProgress!!.reset()
            }
            R.id.btn_start -> {
                circleProgress!!.startAnim()
            }
        }
    }

}
