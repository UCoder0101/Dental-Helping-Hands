package com.dentalhelpinghands.activities.office

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficeSignInBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfficeSignInActivity : BaseActivity() {

    private lateinit var binding: ActivityOfficeSignInBinding

    private val TAG = "user_login_response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficeSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
        binding.tvForgotPassword.setOnClickListener {
            hideKeyboard()
            startActivity(Intent(activity, OfficeForgotPasswordActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener {
            hideKeyboard()
            validationSignIn()
        }

        binding.tvSignUp.setOnClickListener {
            hideKeyboard()
            startActivityForResult(Intent(activity, OfficeSignUpActivity::class.java), Constants.REQUEST_CODE_1)
        }
    }

    fun showResendConfirmAlert(activity: Activity, message: String?) {
        val builder = AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
        builder.setTitle(activity.resources.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setPositiveButton(activity.getString(R.string.resend)) { dialog, _ ->
            val userNameEmail: String = binding.etUserNameEmail.text.toString().trim()

            if (Utils.isValidationEmpty(userNameEmail)) {
                Utils.showAlert(activity, getString(R.string.valid_empty_username_or_email_address))
            } else if (Utils.isEmail(userNameEmail) && !Utils.isValidEmail(userNameEmail)) {
                Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
            } else {
                dialog.dismiss()

                callResendConfirmApi(
                    userNameEmail,
                )
            }

        }.setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


    private fun callResendConfirmApi(email: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callResendConfirmApi(
                    RetrofitBuilder.HEADER_KEY_VALUE, email
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

                        Utils.showAlert(activity, responseBody.ResponseMsg)

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
        binding.etUserNameEmail.requestFocus()

        if (Constants.IS_TESTING_MODE) {
            binding.etUserNameEmail.setText("Dhruv.kmphitech@gmail.com")
            binding.etUserNameEmail.setSelection(binding.etUserNameEmail.length())

            binding.etPassword.setText("123456")
            binding.etPassword.setSelection(binding.etPassword.length())
        }
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_dental_office_sign_in)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

    private fun validationSignIn() {
        val userNameEmail: String = binding.etUserNameEmail.text.toString().trim()
        val password: String = binding.etPassword.text.toString().trim()

        if (Utils.isValidationEmpty(userNameEmail)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_username_or_email_address))
        } else if (Utils.isEmail(userNameEmail) && !Utils.isValidEmail(userNameEmail)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if (Utils.isValidationEmpty(password)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_password))
        } else if (Utils.isPassword(password) && Utils.isValidPassword(
                password,
                resources.getInteger(R.integer.min_length_password)
            )
        ) {
            Utils.showAlert(activity, getString(R.string.enter_valid_password))
        } else {
            callLoginApi(userNameEmail,
                password,
                Constants.device_type,
                Constants.device_token
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Constants.REQUEST_CODE_1) {
            if (resultCode === RESULT_OK) {
                val email: String = data!!.getStringExtra("email")!!
                //Logger.d(TAG, strEditText)

                if(!Utils.isValidationEmpty(email)) {
                    binding.etUserNameEmail.setText(email)
                    binding.etUserNameEmail.setSelection(binding.etUserNameEmail.length())
                }
            }
        }
    }

    private fun callLoginApi(userName: String,
        password: String,
        deviceType: String,
        deviceToken: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val dataPassword = Utils.getMD5(password)
            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callLoginApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    userName,
                    dataPassword!!, deviceType, deviceToken
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

                        when (responseBody.ResponseCode) {
                            1 -> {

                                val data = responseBody.data
                                //Logger.d(TAG,data)
                                Preference.setUserObject(data)


                                Preference.SetStringName(
                                    Preference.USER_ID,
                                    data.user_id
                                )

                                Preference.SetStringName(
                                    Preference.LOGIN_TOKEN,
                                    data.token
                                )

                                Preference.SetStringName(
                                    Preference.USER_TYPE,
                                    data.user_type
                                )

                                Utils.showToast(activity, responseBody.ResponseMsg)

                                activity.startActivity(
                                    Intent(activity, OfficeHomeActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                activity.finish()
                            }
                            2 -> {
                                showResendConfirmAlert(activity, responseBody.ResponseMsg)

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
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}