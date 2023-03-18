package com.dentalhelpinghands.activities.office

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.adapters.office.OfficeMyListingAdapter
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficeMyListingBinding
import com.dentalhelpinghands.databinding.RowOfficeMyListingItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseArrayModel
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfficeMyListingActivity : BaseActivity(), OfficeMyListingAdapter.DeleteJobPostClick{
    private lateinit var binding: ActivityOfficeMyListingBinding
    private var rowOfficeMyListingItemBinding: RowOfficeMyListingItemBinding? = null
    private var lstModel = mutableListOf<DataX>()
    private var adapter: OfficeMyListingAdapter? = null
    private val TAG = "Office My Listing response"
    private val TAG_USER_SIZE = "user Size"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficeMyListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        setDataToList()

        callJobListApi()
    }

    private fun initClick() {
    }

    private fun setDataToList() {
        if (lstModel.size > 0) {
            if (adapter == null) {
                binding.rvMyListing.layoutManager = LinearLayoutManager(
                    activity
                )
                adapter =
                    OfficeMyListingAdapter(activity, lstModel, this)
                binding.rvMyListing.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
            binding.rvMyListing.visibility = View.VISIBLE
            binding.ilEmptyList.tvNotFound.visibility = View.GONE
        } else {
            binding.rvMyListing.visibility = View.GONE
            binding.ilEmptyList.tvNotFound.visibility = View.VISIBLE
        }
    }

    private fun initView() {


    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_my_listing)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }

    private fun callJobListApi() {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseArrayModel> =
                Constants.retrofitBuilder.callOfficeJobListApi(RetrofitBuilder.HEADER_KEY_VALUE, Preference.getStringName(Preference.LOGIN_TOKEN)!!, Preference.getStringName(Preference.USER_ID)!!)

            call.enqueue(object : Callback<ResponseArrayModel> {
                override fun onResponse(
                    call: Call<ResponseArrayModel>,
                    response: Response<ResponseArrayModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody: ResponseArrayModel = response.body()!!

                        if (responseBody.ResponseCode == 1) {
                            val data: ResponseArrayModel = responseBody
                            Logger.d(TAG, data)
                            lstModel.clear()
                            lstModel.addAll(data.data)
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
                    Logger.d(TAG, t.message!!)
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }

    override fun jobPostDelete(adapterPosition: Int) {

        if (lstModel.size > 0 && lstModel.size > adapterPosition) {
            val jobId = lstModel[adapterPosition].job_id
            callJobPostDeleteApi(jobId)
        }

    }

    override fun itemClickUpdate(adapterPosition: Int) {
        val model: DataX? = lstModel[adapterPosition]
        if(model != null) {
            val intent = Intent(activity, OfficePostEditActivity::class.java)
            intent.putExtra("model", model)
            startActivityForResult(intent, Constants.REQUEST_CODE_2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Constants.REQUEST_CODE_2) {
            if (resultCode === RESULT_OK) {
                val isUpdate: Boolean = data!!.getBooleanExtra(Constants.KEY_IS_UPDATE, false)
                //Logger.d(TAG, strEditText)

                if(isUpdate) {
                    callJobListApi()
                }
            }
        }
    }

    private fun callJobPostDeleteApi(jobId: String) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            BaseActivity.showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callDeleteJobPostApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    jobId,
                    Preference.getStringName(Preference.USER_ID)!!
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
                    BaseActivity.hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}