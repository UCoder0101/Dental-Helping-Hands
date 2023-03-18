package com.dentalhelpinghands.fragments.professional

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.adapters.professional.ProfessionalAdapter
import com.dentalhelpinghands.adapters.professional.ProfessionalPreviousWorkAdapter
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.FragmentProfessionalHomeBinding
import com.dentalhelpinghands.fragments.BaseFragment
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseArrayModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ProfessionalHomeFragment : BaseFragment() {
    private var _binding: FragmentProfessionalHomeBinding? = null
    private val binding get() = _binding!!

    //private lateinit var lstModel: ArrayList<String>
    private var lstModel = mutableListOf<DataX>()
    private var adapter: ProfessionalAdapter? = null
    private val TAG = "professional show data response"

    interface OnBackClickListener {
        fun backClick()
    }

    companion object {
        var onBackClickListener: OnBackClickListener? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfessionalHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
        initView()
        initClick()
        setDataToList()
    }

    private fun initClick() {

    }

    private fun initView() {
        /*var userId: String = Preference.getStringName(Preference.USER_ID)!!
        if(Constants.IS_TESTING_MODE) {
            userId = "11"
        }*/

        callJobListApi()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDataToList() {
        if (lstModel.size > 0) {
            if (adapter == null) {
                //Logger.d(TAG, adapter!!)
                binding.rvProfessional.layoutManager = LinearLayoutManager(
                    activity
                )
                adapter =
                    ProfessionalAdapter(activity, lstModel)
                binding.rvProfessional.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
            binding.rvProfessional.visibility = View.VISIBLE
            binding.ilEmptyList.tvNotFound.visibility = View.GONE
        } else {
            binding.rvProfessional.visibility = View.GONE
            binding.ilEmptyList.tvNotFound.visibility = View.VISIBLE
        }

    }

    private fun callJobListApi() {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            BaseActivity.showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseArrayModel> =
                Constants.retrofitBuilder.callProfessionalJobListApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Preference.getStringName(Preference.USER_ID)!!
                )

            //Logger.d(TAG, userId)
            call.enqueue(object : Callback<ResponseArrayModel> {
                override fun onResponse(
                    call: Call<ResponseArrayModel>,
                    response: Response<ResponseArrayModel>
                ) {
                    BaseActivity.hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody: ResponseArrayModel = response.body()!!

                        if (responseBody.ResponseCode == 1) {
                            Logger.d(TAG, responseBody.data)
                            lstModel.clear()
                            lstModel.addAll(responseBody.data)
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
                    BaseActivity.hideProgressDialog()
                    Logger.d(TAG, t.message!!)
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }

    private fun initToolBar() {

        binding.ilToolBar.ivHeaderMenu.setOnClickListener {
            onBackClickListener?.backClick()
        }

        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_professional)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}