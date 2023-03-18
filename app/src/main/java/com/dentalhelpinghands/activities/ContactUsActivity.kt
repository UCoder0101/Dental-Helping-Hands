package com.dentalhelpinghands.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityContactUsBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : BaseActivity() {
    private lateinit var binding: ActivityContactUsBinding
    private  val TAG = "Contact us response"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
        binding.btnSubmit.setOnClickListener {
            //onBackPressed()
            validationContactUs()
        }
    }

    private fun validationContactUs() {
        val userName: String = binding.etName.text.toString().trim()
        val userEmail: String = binding.etEmailAddress.text.toString().trim()
        val subject: String = binding.etSubject.text.toString().trim()
        val message: String = binding.etMessage.text.toString().trim()

        if(Utils.isValidationEmpty(userName)) {
           Utils.showAlert(activity, getString(R.string.valid_empty_user_name) )
        } else if(Utils.isValidationEmpty(userEmail)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_email))
        } else if (Utils.isEmail(userEmail) && !Utils.isValidEmail(userEmail)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if(Utils.isValidationEmpty(subject)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_subject) )
        } else if(Utils.isValidationEmpty(message)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_message))
        } else {
            callContactUsApi(RetrofitBuilder.HEADER_KEY_VALUE, Preference.getStringName(Preference.LOGIN_TOKEN)!!, userName, userEmail, subject, message)
        }
    }

    private fun callContactUsApi(key: String, token: String, username: String, email: String, subject: String, message: String) {
        if(Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callContactUsApi(key, token, username,email, subject, message)

            call.enqueue(object : Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<ResponseObjectModel>,
                    response: Response<ResponseObjectModel>) {
                    hideProgressDialog()

                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.ResponseCode == 1) {

                            Utils.showAlert(activity, responseBody.ResponseMsg, true)
                            //onBackPressed()
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
                    Logger.d(TAG, t.message!!)
                    Utils.handleErrorCode(activity, t)
                }
            })
        }

    }

    private fun initView() {
        binding.etName.requestFocus()

        binding.etMessage.setOnTouchListener(OnTouchListener { v, event ->
            if (binding.etMessage.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_SCROLL) {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_contact_us)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }
}