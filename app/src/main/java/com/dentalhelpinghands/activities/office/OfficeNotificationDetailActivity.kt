package com.dentalhelpinghands.activities.office

import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficeNotificationDetailBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import kotlinx.android.synthetic.main.activity_professional_my_profile.*

class OfficeNotificationDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficeNotificationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficeNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
    }

    private fun initView() {
        val intent = intent
        val responseDataModel: DataX? = intent.getSerializableExtra("requestDataModel") as DataX?

        if (responseDataModel != null) {
            binding.tvName.text = responseDataModel.name

            Utils.setProfileImage(activity, responseDataModel.profile, binding.ivProfile)

            if (!Utils.isValidationEmpty(responseDataModel.sc_license_number)) {
                binding.llScLicenseNumber.visibility = View.VISIBLE
                binding.tvSCLicenseNumber.text = responseDataModel.sc_license_number
            } else {
                binding.llScLicenseNumber.visibility = View.GONE
            }

            if (!Utils.isValidationEmpty(responseDataModel.ga_license_number)) {
                binding.llGaLicenseNumber.visibility = View.VISIBLE
                binding.tvGaLicenseNumber.text = responseDataModel.ga_license_number
            } else {
                binding.llGaLicenseNumber.visibility = View.GONE
            }

            if (!Utils.isValidationEmpty(responseDataModel.fl_license_number)) {
                binding.llFlLicenseNumber.visibility = View.VISIBLE
                binding.tvFlLicenceNumber.text = responseDataModel.fl_license_number
            } else {
                binding.llFlLicenseNumber.visibility = View.GONE
            }
            if (!Utils.isValidationEmpty(responseDataModel.nc_license_number)) {
                binding.llNcLicenseNumber.visibility = View.VISIBLE
                binding.tvNCLicenceNumber.text = responseDataModel.nc_license_number
            } else {
                binding.llNcLicenseNumber.visibility = View.GONE
            }
            if (!Utils.isValidationEmpty(responseDataModel.other_license_number)) {
                binding.llOtherLicenseNumber.visibility = View.VISIBLE
                binding.tvOtherLicenseNumber.text = responseDataModel.other_license_number
            } else {
                binding.llOtherLicenseNumber.visibility = View.GONE
            }

            binding.tvYearPracticing.text = responseDataModel.years_of_practicing
//            binding.tvStartEndTime.text =
//                responseDataModel.start_time + "-" + responseDataModel.end_time
            binding.tvStartEndTime.text = responseDataModel.availability

            binding.tvPhoneNumber.text = responseDataModel.contact
            binding.tvEmailAddress.text = responseDataModel.email
            binding.tvRequestPayRate.text = responseDataModel.pay_rate
        }


    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = "" // TODO: static data

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }
}