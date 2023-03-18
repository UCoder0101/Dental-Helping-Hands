package com.dentalhelpinghands.activities.professional

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.office.OfficeHomeActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityProfesssionalDetailBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProfesssionalDetailBinding
    private val TAG = "apply new job response"
    private var lstModel: DataX? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityProfesssionalDetailBinding.inflate(layoutInflater)
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
        binding.btnApply.setOnClickListener {
            if (lstModel != null) {
                val jobId = lstModel!!.job_id
                callApplyNewJob(jobId)
            }
        }
    }

    private fun callApplyNewJob(jobId: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callJobApplyNowApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Preference.getStringName(Preference.USER_ID)!!,
                    jobId
                )

            call.enqueue(object :
                Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<ResponseObjectModel>,
                    response: Response<ResponseObjectModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.ResponseCode == 1) {

                            Utils.showToast(activity, responseBody.ResponseMsg)
                            onBackPressed()
                        } else {
                            Utils.showAlert(activity, responseBody.ResponseMsg)
                        }
                    } else {
                        Utils.showAlertsWithTitle(
                            activity,
                            getString(R.string.app_name),
                            getString(R.string.something_went_wrong)
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ResponseObjectModel?>,
                    t: Throwable
                ) {
                    hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }

    private fun initView() {
        /*val extras = intent.extras
        if (extras != null && lstModel != null) {
            lstModel =
                intent.getSerializableExtra("responseObject") as DataX? //Obtaining data
        }*/

        val intent = intent
        lstModel = intent.getSerializableExtra("requestDataModel") as DataX?

        if (lstModel != null) {
            binding.tvProfessional.text = lstModel!!.professional
            binding.tvOfficeAddress.text = lstModel!!.office_address
            binding.tvPhoneNumber.text = lstModel!!.contact
            binding.tvTypePractice.text = lstModel!!.type_of_practice
            binding.tvDentalSoftware.text = lstModel!!.dental_software
            binding.tvAveragePatient.text = lstModel!!.average_patients
            binding.tvTimePerPatient.text = lstModel!!.time_per_patients
            binding.tvStartTime.text = lstModel!!.start_time
            binding.tvEndTime.text = lstModel!!.end_time
            binding.tvLunchTime.text = lstModel!!.lunch_time_start + " to " + lstModel!!.lunch_time_end
            binding.tvPrice.text = "$" + lstModel!!.price.toString() + "/per week"
            binding.tvPostAddedTime.text =  lstModel!!.date
        }
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = "Dental Practice" // TODO: static data

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }
}