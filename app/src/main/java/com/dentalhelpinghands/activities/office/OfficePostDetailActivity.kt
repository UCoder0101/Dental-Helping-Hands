package com.dentalhelpinghands.activities.office

import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.databinding.ActivityOfficePostDetailBinding
import com.dentalhelpinghands.models.office.loginModel.DataX

class OfficePostDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficePostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficePostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        setDataToView()
    }

    private fun setDataToView() {

    }

    private fun initClick() {

    }


    private fun initView() {
        val intent = intent
        val responseDataModel: DataX? = intent.getSerializableExtra("requestDataModel") as DataX?

        if(responseDataModel != null) {
            binding.tvProfessional.text = responseDataModel.professional
            binding.tvAddress.text = responseDataModel.office_address
            binding.tvPhoneNumber.text = responseDataModel.contact
            binding.tvTypePractice.text = responseDataModel.type_of_practice
            binding.tvDentalSoftware.text = responseDataModel.dental_software
            binding.tvTimePerPatient.text = responseDataModel.time_per_patients
            binding.tvStartTime.text = responseDataModel.start_time
            binding.tvEndTime.text = responseDataModel.end_time
            binding.tvLunchTime.text = responseDataModel.lunch_time_start + " to " + responseDataModel.lunch_time_end
            binding.tvPrice.text = responseDataModel.price.toString()
            binding.tvPostAddedTime.text = responseDataModel.price_type
        }

    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = "Practices In Dental" // TODO: static data

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }
}