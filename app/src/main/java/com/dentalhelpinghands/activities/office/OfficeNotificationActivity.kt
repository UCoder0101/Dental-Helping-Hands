package com.dentalhelpinghands.activities.office

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.adapters.office.OfficeNotificationAdapter
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.*
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseArrayModel
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfficeNotificationActivity : BaseActivity(), OfficeNotificationAdapter.OpenCloseNotificationDialog {
    private lateinit var binding: ActivityOfficeNotificationBinding
    private val lstModel = mutableListOf<DataX>()
    private var adapter: OfficeNotificationAdapter? = null
    private var rowOfficeNotificationItemBinding: RowOfficeNotificationItemBinding? = null
    private val TAG = "Job_List_notification"

    interface OnBackClickListener {
        fun backClick()
    }

    companion object {
        var onBackClickListener: OnBackClickListener? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityOfficeNotificationBinding.inflate(layoutInflater)
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
        //var userId: String = Preference.getStringName(Preference.USER_ID)!!

        callJobListApi()
    }

    private fun setDataToList() {
        if (lstModel.size > 0) {
            if (adapter == null) {
                Logger.d(TAG, lstModel.size)
                binding.rvNotification.layoutManager = LinearLayoutManager(
                    activity)
                adapter =
                    OfficeNotificationAdapter(activity, lstModel, this)
                binding.rvNotification.adapter = adapter
            } else {
                Logger.d(TAG, lstModel.size)
                adapter!!.notifyDataSetChanged()
            }
            binding.rvNotification.visibility = View.VISIBLE
            binding.ilEmptyList.tvNotFound.visibility = View.GONE
        } else {
            binding.rvNotification.visibility = View.GONE
            binding.ilEmptyList.tvNotFound.visibility = View.VISIBLE
        }
    }

    private fun callJobListApi() {

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
                            val data: ResponseArrayModel = responseBody

                            lstModel.addAll(data.job_apply_master)
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

    /*private fun callOfficeDeleteJobApi(jobId: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callJobApplicationApproveApi(
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


    private fun callOfficeConfirmJobApi(jobApplyId: String, actionType: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callJobApplicationApproveApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!, Preference.getStringName(Preference.USER_ID)!!, actionType, jobApplyId)

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
                            callJobListApi()
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
            onBackClickListener?.backClick()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }

    override fun confirmNotificationDialog(position: Int) {
        val jobApplyId = lstModel[position].job_apply_id

        callOfficeConfirmJobApi(jobApplyId, Constants.ACTION_TYPE_APPROVE)
    }

    override fun deleteNotificationDialog(position: Int) {
        val jobApplyId = lstModel[position].job_apply_id
        Logger.d(TAG, jobApplyId)
        callOfficeConfirmJobApi(jobApplyId, Constants.ACTION_TYPE_REJECTED)
    }
}