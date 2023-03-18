package com.dentalhelpinghands.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.office.OfficeHomeActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityChangePasswordBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    companion object {
        private val TAG = "Change Password response"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
        binding.btnSave.setOnClickListener {
            validationChangePassword()
        }
    }

    private fun initView() {
        binding.etOldPassword.requestFocus()
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_change_password)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

    private fun validationChangePassword() {
        val oldPassword = binding.etOldPassword.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()
        val confirmNewPassword = binding.etConfirmNewPassword.text.toString().trim()

        if (Utils.isValidationEmpty(oldPassword)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_old_password))
        } else if (Utils.isValidationEmpty(newPassword)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_new_password))
        } else if (!Utils.isValidPassword(
                newPassword,
                resources.getInteger(R.integer.min_length_password)
            )
        ) {
            Utils.showAlert(
                activity, getString(
                    R.string.valid_strong_new_password,
                    resources.getInteger(R.integer.min_length_password)
                )
            )
        } else if (Utils.isValidationEmpty(confirmNewPassword)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_confirm_new_password))
        } else if (!Utils.isPassMatch(newPassword, confirmNewPassword)) {
            Utils.showAlert(
                activity,
                getString(R.string.valid_new_password_and_confirm_new_password_not_match)
            )
        } else {
             callChangePasswordApi(oldPassword, newPassword)
        }
    }

    private fun callChangePasswordApi(oldPassword: String, newPassword: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val oldPass = Utils.getMD5(oldPassword)
            val newPass = Utils.getMD5(newPassword)
            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callChangePasswordApi(RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Preference.getStringName(Preference.USER_ID)!!,
                    oldPass!!, newPass!!)

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

                            Utils.showAlert(activity, responseBody.ResponseMsg, DialogInterface.OnClickListener { _, _ ->
                                onBackPressed()
                            })

                           /* Utils.showAlert(activity, responseBody.ResponseMsg)
                            onBackPressed()
                            activity.finish()*/
                        } else {
                            Utils.showAlert(activity, responseBody.ResponseMsg)
                        }
                    } else {
                        Utils.showAlertsWithTitle(activity, getString(R.string.app_name),getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(
                    call: Call<ResponseObjectModel?>,
                    t: Throwable) {
                    hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}