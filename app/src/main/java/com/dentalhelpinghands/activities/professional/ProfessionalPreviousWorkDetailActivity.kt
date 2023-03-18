package com.dentalhelpinghands.activities.professional

import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityProfessionalPreviousWorkDetailBinding
import com.dentalhelpinghands.models.office.loginModel.DataX

class ProfessionalPreviousWorkDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProfessionalPreviousWorkDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityProfessionalPreviousWorkDetailBinding.inflate(layoutInflater)
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
        val responseDataModel: DataX? = intent.getSerializableExtra("previousWorkDetail") as DataX?

        if(responseDataModel != null) {
//            Utils.setProfileImage(activity, responseDataModel.profile, binding.ivProfile)
            binding.ilToolBar.tvHeaderTitle.text = responseDataModel.practice_name

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
            binding.tvPostAddedTime.text = responseDataModel.date
        }

    }

    private fun initToolBar() {
//        binding.ilToolBar.tvHeaderTitle.text = "Dental Practice" // TODO: static data

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }
}