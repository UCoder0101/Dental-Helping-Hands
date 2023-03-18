package com.dentalhelpinghands.activities

import android.content.Intent
import android.os.Bundle
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.office.OfficeSignInActivity
import com.dentalhelpinghands.activities.professional.ProfessionalSignInActivity
import com.dentalhelpinghands.databinding.ActivityFlowTypeBinding

class FlowTypeActivity : BaseActivity() {
    private lateinit var binding: ActivityFlowTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityFlowTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initClick()
    }

    private fun initClick() {

        binding.btnDentalOffice.setOnClickListener {
            hideKeyboard()
            startActivity(Intent(activity, OfficeSignInActivity::class.java))
        }

        binding.btnDentalProfessional.setOnClickListener {
            hideKeyboard()
            startActivity(Intent(activity, ProfessionalSignInActivity::class.java))
        }
    }

}