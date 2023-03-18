package com.dentalhelpinghands

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.ChangePasswordActivity
import com.dentalhelpinghands.activities.FlowTypeActivity
import com.dentalhelpinghands.activities.office.*
import com.dentalhelpinghands.activities.professional.ProfessionalHomeActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter, R.anim.exit)
        setContentView(R.layout.activity_splash)

        activity = this
        processNext()
    }


    private fun processNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            //complete code for sign in or not user
            val userId = Preference.getStringName(Preference.USER_ID)
            val userType = Preference.getStringName(Preference.USER_TYPE)

            if (!Utils.isValidationEmpty(userId) && userType == "1")
                startActivity(Intent(activity, OfficeHomeActivity::class.java))
            else if(!Utils.isValidationEmpty(userId) && userType == "2")
                startActivity(Intent(activity, ProfessionalHomeActivity::class.java))
            else
                startActivity(Intent(activity, FlowTypeActivity::class.java))
            finish()
        }, Constants.SPLASH_TIME_OUT)

    }
}