package com.vz.phpkotlin.ui.act

import android.content.Intent
import com.vz.phpkotlin.MainActivity
import com.vz.phpkotlin.ui.base.VzBaseActivity
import com.vz.phpkotlin.utils.ToolUI

class WelcomeActivity : VzBaseActivity() {

    override fun initView() {
        ToolUI.postTaskDelay(Runnable {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }

    override fun resume() {
    }

    override fun destroy() {
    }


}
