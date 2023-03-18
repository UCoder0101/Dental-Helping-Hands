package com.dentalhelpinghands.activities.professional

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.adapters.professional.ProfessionalNotificationAdapter
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityProfessionalNotificationBinding
import com.dentalhelpinghands.databinding.DialogApproveBinding
import com.dentalhelpinghands.databinding.DialogDeleteBinding
import com.dentalhelpinghands.databinding.RowProfessionalNotificationItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseArrayModel
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalNotificationActivity : BaseActivity(),
    ProfessionalNotificationAdapter.OpenCloseNotificationDialog {
    private lateinit var binding: ActivityProfessionalNotificationBinding
    private val lstModel = mutableListOf<DataX>()
    private var rowProfessionalNotificationItemBinding: RowProfessionalNotificationItemBinding? =
        null
    private var adapter: ProfessionalNotificationAdapter? = null
    private val TAG = "professional notification response"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityProfessionalNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        setDataToList()
    }

    private fun initClick() {
    }

    private fun initView() {
        /*val intent = intent.extras
        val responseDataModel: DataX? = intent!!.getSerializable("model") as DataX?

        if(responseDataModel != null) {
            Utils.setProfileImage(activity, responseDataModel.profile, rowProfessionalNotificationItemBinding!!.ivProfile)
            rowProfessionalNotificationItemBinding!!.tvProfessional.setText(responseDataModel.professional)
            //rowProfessionalNotificationItemBinding!!.tvNotificationMsg.setText(responseDataModel.)
        }*/



        setDataToList()

        callProfessionalJobNotificationListApi()
    }

    private fun setDataToList() {
        if (lstModel.size > 0) {
            if (adapter == null) {
                binding.rvProfessionalNotification.layoutManager = LinearLayoutManager(
                    activity
                )
                adapter =
                    ProfessionalNotificationAdapter(activity, lstModel, this)
                binding.rvProfessionalNotification.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
            binding.rvProfessionalNotification.visibility = View.VISIBLE
            binding.ilEmptyList.tvNotFound.visibility = View.GONE
        } else {
            binding.rvProfessionalNotification.visibility = View.GONE
            binding.ilEmptyList.tvNotFound.visibility = View.VISIBLE
        }
    }

    private fun callProfessionalJobNotificationListApi() {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseArrayModel> =
                Constants.retrofitBuilder.callApplyJobListApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Preference.getStringName(Preference.USER_ID)!!
                )

            call.enqueue(object :
                Callback<ResponseArrayModel> {
                override fun onResponse(
                    call: Call<ResponseArrayModel>,
                    response: Response<ResponseArrayModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.ResponseCode == 1) {
                            //Logger.d(TAG, responseBody.data)
                            lstModel.clear()
                            lstModel.addAll(responseBody.job_apply_master)
                            setDataToList()

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
                    call: Call<ResponseArrayModel?>,
                    t: Throwable
                ) {
                    hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
        }

    }

    /*private fun callProfessionalDeleteJobApi(jobId: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callDeleteJobPostApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!, jobId, Preference.getStringName(Preference.USER_ID)!!)

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

                            Utils.showAlert(activity, responseBody.ResponseMsg)
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

    }*/


    private fun callProfessionalConfirmJobApi(jobApplyId: String, actionType: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callJobApplicationApproveApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Preference.getStringName(Preference.USER_ID)!!,
                    actionType,
                    jobApplyId
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
                            callProfessionalJobNotificationListApi()
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

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_notification)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }
        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

    override fun confirmNotificationDialog(position: Int) {
        val jobApplyId = lstModel[position].job_apply_id
        callProfessionalConfirmJobApi(jobApplyId, Constants.ACTION_TYPE_APPROVE)
    }

    override fun deleteNotificationDialog(position: Int) {
        val jobApplyId = lstModel[position].job_apply_id

        Logger.d(TAG, jobApplyId)

        callProfessionalConfirmJobApi(jobApplyId, Constants.ACTION_TYPE_REJECTED)
    }
}