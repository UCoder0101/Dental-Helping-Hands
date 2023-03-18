package com.dentalhelpinghands.activities.professional

import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityProfessionalForgotPasswordBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityProfessionalForgotPasswordBinding
    private val TAG = "professional_forgot_password_response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityProfessionalForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
        binding.btnSendLink.setOnClickListener {
            validationForgotPassword()

        }
    }

    private fun initView() {
        binding.etEmailAddress.requestFocus()
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_forgot_password)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

    private fun validationForgotPassword() {
        val email: String = binding.etEmailAddress.text.toString().trim()

        if (Utils.isValidationEmpty(email)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_email_address))
        } else if (!Utils.isValidEmail(email)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else {
            callForgotPasswordApi(RetrofitBuilder.HEADER_KEY_VALUE, email)
        }
    }

    private fun callForgotPasswordApi(key: String, email: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callForgotPasswordApi(key, email)

            call.enqueue(object : Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<ResponseObjectModel>,
                    response: Response<ResponseObjectModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        when (responseBody.ResponseCode) {
                            1 -> {
                                Utils.showAlert(activity, responseBody.ResponseMsg, true)

                            }
                            else -> {
                                Utils.showAlert(activity, responseBody.ResponseMsg)
                            }
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
                    Logger.d(TAG, t.message!!)
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}