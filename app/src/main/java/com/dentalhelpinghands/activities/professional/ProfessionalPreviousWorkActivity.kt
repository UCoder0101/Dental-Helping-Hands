package com.dentalhelpinghands.activities.professional

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.adapters.office.OfficeMyListingAdapter
import com.dentalhelpinghands.adapters.professional.ProfessionalPreviousWorkAdapter
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityProfessionalPreviousWorkBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseArrayModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalPreviousWorkActivity : BaseActivity() {
    private lateinit var binding: ActivityProfessionalPreviousWorkBinding
    //private lateinit var lstModel: ArrayList<DataX>
    private var lstModel = mutableListOf<DataX>()
    private var adapter: ProfessionalPreviousWorkAdapter? = null
    private val TAG = "Previous_work_response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityProfessionalPreviousWorkBinding.inflate(layoutInflater)
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
        var userId: String = Preference.getStringName(Preference.USER_ID)!!
        if (Constants.IS_TESTING_MODE) {
            userId = "16"
        }
        callJobApproveListApi(
            RetrofitBuilder.HEADER_KEY_VALUE,
            Preference.getStringName(Preference.LOGIN_TOKEN)!!,
            userId
        )
    }

    private fun setDataToList() {
        if (lstModel.size > 0) {
            if (adapter == null) {
                binding.rvPreviousWork.layoutManager = LinearLayoutManager(
                    activity)
                adapter =
                    ProfessionalPreviousWorkAdapter(activity, lstModel)
                binding.rvPreviousWork.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
            binding.rvPreviousWork.visibility = View.VISIBLE
            binding.ilEmptyList.tvNotFound.visibility = View.GONE
        } else {
            binding.rvPreviousWork.visibility = View.GONE
            binding.ilEmptyList.tvNotFound.visibility = View.VISIBLE
        }
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_previous_work)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }

    private fun callJobApproveListApi(key: String, token: String, userId: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))


            val call: Call<ResponseArrayModel> =
                Constants.retrofitBuilder.callApprovedJobsListApi(key, token, userId)

            Logger.d(TAG, userId)
            call.enqueue(object : Callback<ResponseArrayModel> {
                override fun onResponse(
                    call: Call<ResponseArrayModel>,
                    response: Response<ResponseArrayModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.ResponseCode == 1) {
                            Logger.d(TAG, responseBody.job_apply_master)
                            //var model = responseBody.job_apply_master
                            lstModel.clear()
                            lstModel.addAll(responseBody.job_apply_master)
                            setDataToList()
                        } else {
                            Logger.d(TAG, responseBody.ResponseMsg)
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
                    Logger.d(TAG, t.message!!)
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}