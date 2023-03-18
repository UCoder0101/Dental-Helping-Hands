package com.dentalhelpinghands.activities.office

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TimePicker
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficePostEditBinding
import com.dentalhelpinghands.databinding.DialogTimePickerBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfficePostEditActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficePostEditBinding
    private var model: DataX? = null
    private var opensTime: String? = null
    private var closesTime: String? = null
    private val TAG = "Update profile response"
//    private var getSelectDate = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficePostEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerProfessional()
        processSpinnerTypeOfPractice()
        processSpinnerPriceType()
        setDataToView()
        getDataList()
    }

    private fun setDataToView() {
        // TODO: static data
        if (Constants.IS_TESTING_MODE) {
            binding.etPracticeName.setText("Practices Dental")
            binding.etPracticeName.setSelection(binding.etPracticeName.length())
            binding.spProfessional.setSelection(1)
            binding.etWebAddress.setText("North Augusta, SC")
            binding.etPhoneNumber.setText("88950 89789")
            binding.spTypeOfPractice.setSelection(1)
        }
    }

    private fun processSpinnerPriceType() {

        binding.etPriceType.setOnClickListener {
            binding.spPriceType.performClick()
        }

        binding.llPriceType.setOnClickListener {
            binding.spPriceType.performClick()
        }

        val spinnerPriceType = resources.getStringArray(R.array.spinner_price_type)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerPriceType
        )
        binding.spPriceType.adapter = adapter

        binding.spPriceType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etPriceType.setText(spinnerPriceType[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }


    private fun initClick() {

        binding.btnDone.setOnClickListener {
            validationPostEdit()
        }

        binding.llStartTime.setOnClickListener {
            if (opensTime != null) {
                openDialogTimePickerOpens(binding.etStartTime)
            }
        }

        binding.etStartTime.setOnClickListener {
            binding.llStartTime.performClick()
        }

        binding.etEndTime.setOnClickListener {
            binding.llEndTime.performClick()
        }


        binding.llEndTime.setOnClickListener {
            if (closesTime != null) {
                openDialogTimePickerOpens(binding.etEndTime)
            }

        }

        binding.llLunchTimeStart.setOnClickListener {
            openDialogTimePickerOpens(binding.etLunchTimeStart)
        }

        binding.etLunchTimeStart.setOnClickListener {
            binding.llLunchTimeStart.performClick()
        }

        binding.llLunchTimeEnd.setOnClickListener {
            openDialogTimePickerOpens(binding.etLunchTimeEnd)
        }

        binding.etLunchTimeEnd.setOnClickListener {
            binding.llLunchTimeEnd.performClick()
        }
    }

    private fun validationPostEdit() {
        val practiceName = binding.etPracticeName.text.toString().trim()
        val professional = binding.etProfessional.text.toString().trim()
        val webAddress = binding.etWebAddress.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val typePractice = binding.etTypeOfPractice.text.toString().trim()
        val dentalSoftware = binding.etDentalSoftware.text.toString().trim()
        val timePerPatient = binding.etTimePerPatients.text.toString().trim()
        val officeAddress = binding.etOfficeAddress.text.toString().trim()
        val startTime = binding.etStartTime.text.toString().trim()
        val endTime = binding.etEndTime.text.toString().trim()
        val lunchStartTime = binding.etLunchTimeStart.text.toString().trim()
        val lunchEndTime = binding.etLunchTimeEnd.text.toString().trim()
        val priceType = binding.etPriceType.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()


        if (Utils.isValidationEmpty(practiceName)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_practice_name))
        } else if (Utils.isValidationEmpty(professional) || professional == getString(R.string.select_professional)) {
            Utils.showAlert(activity, getString(R.string.select_type_of_professional))
        } else if (Utils.isValidationEmpty(phoneNumber)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_phone_number))
        } else if (Utils.isValidationEmpty(typePractice) || typePractice == getString(R.string.select_type_of_practice)) {
            Utils.showAlert(activity, getString(R.string.select_type_of_practice))
        } else if (Utils.isValidationEmpty(timePerPatient)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_time_per_patient))
        } else if (Utils.isValidationEmpty(officeAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_office_address))
        } else if (Utils.isValidationEmpty(lunchStartTime) || lunchStartTime == getString(R.string.select_start_lunch_time)) {
            Utils.showAlert(activity, getString(R.string.select_start_lunch_time))
        } else if (Utils.isValidationEmpty(lunchEndTime) || lunchEndTime == getString(R.string.select_end_lunch_time)) {
            Utils.showAlert(activity, getString(R.string.select_end_lunch_time))
        } else if (Utils.isValidationEmpty(priceType) || priceType == getString(R.string.price_type)) {
            Utils.showAlert(activity, getString(R.string.price_type))
        } else if (Utils.isValidationEmpty(price)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_price))
        } else {
            /*Logger.d(TAG, model!!.practice_name)
            Logger.d(TAG, model!!.professional)
            Logger.d(TAG, model!!.contact)
            Logger.d(TAG, model!!.type_of_practice)
            Logger.d(TAG, model!!.time_per_patients)
            Logger.d(TAG, model!!.office_address)
            Logger.d(TAG, model!!.start_time)
            Logger.d(TAG, model!!.end_time)
            Logger.d(TAG, model!!.lunch_time_start)
            Logger.d(TAG, model!!.lunch_time_end)
            Logger.d(TAG, model!!.price_type)
            Logger.d(TAG, model!!.price)*/
            /*var jobId = model!!.job_id
            if(Constants.IS_TESTING_MODE) {
               jobId = "29"
            }*/
            val jobId = model!!.job_id
            Logger.d(TAG, jobId)
            callUpdatePracticeApi(
                jobId, /*getSelectDate,*/
                practiceName,
                professional,
                webAddress,
                phoneNumber,
                typePractice,
                dentalSoftware,
                timePerPatient,
                officeAddress,
                startTime,
                endTime,
                lunchStartTime,
                lunchEndTime,
                priceType,
                price
            )
        }
    }

    private fun callUpdatePracticeApi(
        jobId: String, /*date: String, */
        practiceName: String,
        typeProfessional: String,
        webAddress: String,
        mobile: String,
        typePractice: String,
        dentalSoftware: String,
        timePerPatient: String,
        address: String,
        startTime: String,
        endTime: String,
        startLunchtime: String,
        endLunchtime: String,
        priceType: String,
        price: String
    ) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callUpdatePracticeApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    jobId,
                    Preference.getStringName(Preference.USER_ID)!!,
                    /*date,*/
                    practiceName,
                    typeProfessional,
                    webAddress,
                    mobile,
                    typePractice,
                    dentalSoftware,
                    timePerPatient,
                    address,
                    startTime,
                    endTime,
                    startLunchtime,
                    endLunchtime,
                    priceType,
                    price
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
                            //data reload previous listing screen
                            val intent = Intent()
                            intent.putExtra(Constants.KEY_IS_UPDATE, true)
                            setResult(RESULT_OK, intent)
                            finish()

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

    private fun getDataList() {
        val intent = intent
        model = intent.getSerializableExtra("model") as DataX

        if (model != null) {
            Logger.d(TAG, model!!.job_id)
            binding.etPracticeName.setText(model!!.practice_name)
            binding.etPracticeName.setSelection(binding.etPracticeName.length())

            /*binding.etProfessional.setText(model.professional)
            binding.etProfessional.setSelection(binding.etProfessional.length())*/

            val spinnerProfessional = resources.getStringArray(R.array.spinner_professional)
            spinnerProfessional.forEachIndexed { index, _ ->
                if (model!!.professional == spinnerProfessional[index]) {
                    binding.spProfessional.setSelection(index)
                }
            }

            binding.etWebAddress.setText(model!!.web_address)
            binding.etWebAddress.setSelection(binding.etWebAddress.length())

            binding.etPhoneNumber.setText(model!!.contact)
            binding.etPhoneNumber.setSelection(binding.etPhoneNumber.length())

            binding.etTypeOfPractice.setText(model!!.type_of_practice)
            binding.etTypeOfPractice.setSelection(binding.etTypeOfPractice.length())
            val spinnerTypeOfPractice = resources.getStringArray(R.array.spinner_type_of_practice)
            spinnerTypeOfPractice.forEachIndexed { index, _ ->
                if (model!!.type_of_practice == spinnerTypeOfPractice[index]) {
                    binding.spTypeOfPractice.setSelection(index)
                }
            }

            binding.etDentalSoftware.setText(model!!.dental_software)
            binding.etDentalSoftware.setSelection(binding.etDentalSoftware.length())

            binding.etTimePerPatients.setText(model!!.time_per_patients)
            binding.etTimePerPatients.setSelection(binding.etTimePerPatients.length())

            binding.etOfficeAddress.setText(model!!.office_address)
            binding.etOfficeAddress.setSelection(binding.etOfficeAddress.length())

            binding.etStartTime.setText(model!!.start_time)
            binding.etStartTime.setSelection(binding.etStartTime.length())

            binding.etEndTime.setText(model!!.end_time)
            binding.etEndTime.setSelection(binding.etEndTime.length())

            binding.etLunchTimeStart.setText(model!!.lunch_time_start)
            binding.etLunchTimeStart.setSelection(binding.etLunchTimeStart.length())

            binding.etLunchTimeEnd.setText(model!!.lunch_time_end)
            binding.etLunchTimeEnd.setSelection(binding.etLunchTimeEnd.length())

            /*binding.etPriceType.setText(model.price_type)
            binding.etPriceType.setSelection(binding.etPriceType.length())*/
            val spinnerPriceType = resources.getStringArray(R.array.spinner_price_type)
            spinnerPriceType.forEachIndexed { index, _ ->
                if (model!!.price_type == spinnerPriceType[index]) {
                    binding.spPriceType.setSelection(index)
                }
            }

            binding.etPrice.setText(model!!.price.toString())
            binding.etPrice.setSelection(binding.etPrice.length())
        }
    }


    private fun initView() {
        binding.etPracticeName.requestFocus()

        //initialize lunch time open or close
        opensTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.etLunchTimeStart.setText(opensTime)

        closesTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.etLunchTimeEnd.setText(closesTime)

        val intent = intent.extras
        val openTime = intent!!.getString("openTime")
        val closeTime = intent!!.getString("closeTime")
        binding.etStartTime.setText(openTime)
        binding.etEndTime.setText(closeTime)

    }

    private fun openDialogTimePickerOpens(time: EditText) {
        val dialog = Dialog(activity, R.style.MyDialogStyle)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.BOTTOM
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) //before
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val bindingDialogTimePicker = DialogTimePickerBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogTimePicker.root)
        dialog.setTitle("")


        bindingDialogTimePicker.timePicker.setIs24HourView(false)  // used to display AM/PM mode

        val opensTime24 = Utils.customDateTimeFormat(
            opensTime,
            Constants.DATE_FORMAT_COLUMN_HH_MM_AA,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24
        ).toString()

        val opensTimeHours = Utils.customDateTimeFormat(
            opensTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_HH
        ).toString()

        val opensTimeMinutes = Utils.customDateTimeFormat(
            opensTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_MM
        ).toString()

        // set the value for current hours
        bindingDialogTimePicker.timePicker.currentHour = opensTimeHours.toInt()
        bindingDialogTimePicker.timePicker.currentMinute = opensTimeMinutes.toInt()

        bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
            view: TimePicker,
            hourOfDay: Int,
            minute: Int
        ) {
            val selectedDate = "${hourOfDay}:${minute}"

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            binding.etLunchTimeStart.setText(opensTime)
        })


        bindingDialogTimePicker.btnDone.setOnClickListener {
            val hourOfDay = bindingDialogTimePicker.timePicker.hour
            val minute = bindingDialogTimePicker.timePicker.minute
            val selectedDate = "${hourOfDay}:${minute}"

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            time.setText(opensTime)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun processSpinnerProfessional() {
        binding.etProfessional.setOnClickListener {
            binding.spProfessional.performClick()
        }

        binding.llProfessional.setOnClickListener {
            binding.spProfessional.performClick()
        }

        val spinnerProfessional = resources.getStringArray(R.array.spinner_professional)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerProfessional
        )
        binding.spProfessional.adapter = adapter

        binding.spProfessional.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etProfessional.setText(spinnerProfessional[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun processSpinnerTypeOfPractice() {
        binding.etTypeOfPractice.setOnClickListener {
            binding.spTypeOfPractice.performClick()
        }

        binding.llTypeOfPractice.setOnClickListener {
            binding.spTypeOfPractice.performClick()
        }

        val spinnerTypeOfPractice = resources.getStringArray(R.array.spinner_type_of_practice)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerTypeOfPractice
        )
        binding.spTypeOfPractice.adapter = adapter

        binding.spTypeOfPractice.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etTypeOfPractice.setText(spinnerTypeOfPractice[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = "Practices Dental" // TODO: static data

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }
}