package com.dentalhelpinghands.activities.office

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficePostJobBinding
import com.dentalhelpinghands.databinding.DialogTimePickerBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import kotlinx.android.synthetic.main.dialog_time_picker.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfficePostJobActivity : BaseActivity() {

    private lateinit var binding: ActivityOfficePostJobBinding
    private var opensTime: String? = null
    private var closesTime: String? = null
    private var getSelectDate = ""
    private val TAG = "Post a Job"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficePostJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerProfessional()
        processSpinnerTypeOfPractice()
        processSpinnerPriceType()
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

        binding.btnPost.setOnClickListener {
            validationPostJobs()
        }


        binding.llStartTime.setOnClickListener {
            if(opensTime != null) {
                openDialogTimePickerOpens(binding.etStartTime)
            }
        }

        binding.etStartTime.setOnClickListener {
            binding.llStartTime.performClick()
        }

        binding.llEndTime.setOnClickListener {
            if(closesTime != null) {
                openDialogTimePickerOpens(binding.etEndTime)
            }
        }

        binding.etEndTime.setOnClickListener {
            binding.llEndTime.performClick()
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

    private fun validationPostJobs() {
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


        if(Utils.isValidationEmpty(practiceName)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_practice_name))
        } else if(Utils.isValidationEmpty(professional) || professional == getString(R.string.select_professional)) {
            Utils.showAlert(activity, getString(R.string.select_type_of_professional))
        } else if(Utils.isValidationEmpty(phoneNumber)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_phone_number))
        } else if(Utils.isValidationEmpty(typePractice) || typePractice == getString(R.string.select_type_of_practice)) {
            Utils.showAlert(activity, getString(R.string.select_type_of_practice))
        } else if(Utils.isValidationEmpty(timePerPatient)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_time_per_patient))
        } else if(Utils.isValidationEmpty(officeAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_office_address))
        } else if(Utils.isValidationEmpty(lunchStartTime) || lunchStartTime == getString(R.string.select_start_lunch_time)) {
            Utils.showAlert(activity, getString(R.string.select_start_lunch_time))
        } else if(Utils.isValidationEmpty(lunchEndTime) || lunchEndTime == getString(R.string.select_end_lunch_time)) {
            Utils.showAlert(activity, getString(R.string.select_end_lunch_time))
        } else if(Utils.isValidationEmpty(priceType) ||  priceType == getString(R.string.price_type)) {
            Utils.showAlert(activity, getString(R.string.price_type))
        } else if(Utils.isValidationEmpty(price)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_price))
        } else {
            callPostJobsApi(RetrofitBuilder.HEADER_KEY_VALUE, Preference.getStringName(Preference.LOGIN_TOKEN)!!,Preference.getStringName(Preference.USER_ID)!!, getSelectDate, practiceName, professional, webAddress, phoneNumber, typePractice, dentalSoftware,timePerPatient, officeAddress, startTime, endTime, lunchStartTime, lunchEndTime, priceType, price)
        }
    }

    private fun callPostJobsApi(key: String, token: String,userId: String, date: String, practiceName: String, typeProfessional: String, webAddress: String, mobile: String, typePractice: String, dentalSoftware:String,timePerPatient: String, address: String, startTime: String, endTime: String, startLunchtime: String, endLunchtime: String, priceType: String, price: String) {
        if(Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            //val responseObjectModel = Data(practiceName = practiceName, profession = typeProfessional, contact = mobile, typeOfPractice = typePractice, timePerPatient = timePerPatient, practiceAddress = address, availabilityStartTime = startTime, availabilityEndTime = endTime, startLunchTime = startLunchtime, endLunchTime = endLunchtime, typePrice = priceType, payRate = price)
            Logger.d(TAG, Preference.getStringName(Preference.USER_ID)!!)
            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callPostJobsApi(key, token, userId, date, practiceName, typeProfessional, webAddress, mobile, typePractice, dentalSoftware, timePerPatient, address, startTime, endTime, startLunchtime, endLunchtime, priceType, price)

            call.enqueue(object : Callback<ResponseObjectModel> {
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
                            activity.startActivity(
                                Intent(activity, OfficeHomeActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            activity.finish()

                        } else {
                            Utils.showAlert(activity,responseBody.ResponseMsg)
                        }
                    } else {
                        Utils.showAlertsWithTitle(activity, getString(R.string.app_name),getString(R.string.something_went_wrong))
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



    private fun initView() {
        binding.etPracticeName.requestFocus()

        if(Constants.IS_TESTING_MODE) {
            binding.etPracticeName.setText("Anil")
            binding.etProfessional.setText("General")
            binding.etPhoneNumber.setText("9586667303")
            binding.etTimePerPatients.setText("5")
            binding.etOfficeAddress.setText("surat,surat,surat")
            binding.etLunchTimeStart.setText("01:00 pm")
            binding.etLunchTimeEnd.setText("01:00 pm")
            binding.etPriceType.setText("Fixed")
            binding.etPrice.setText("1000")
        }

        //initialize lunch time open or close
        opensTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.etLunchTimeStart.setText(opensTime)

        closesTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.etLunchTimeEnd.setText(closesTime)


        //get data for selected date from DateActivity
//        binding.ivStartTimeArrow.visibility = View.VISIBLE
//        binding.ivEndTimeArrow.visibility = View.VISIBLE

        getSelectDate = intent.getStringExtra("selectedDate") as String
        Logger.d(TAG, getSelectDate)

        val intent = intent.extras
        val openTime = intent!!.getString("openTime")
        val closeTime = intent.getString("closeTime")
        //previous screen get date
        binding.etStartTime.setText(openTime)
        binding.etEndTime.setText(closeTime)

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
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_post_a_job)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE

    }

    private fun openDialogTimePickerOpens(startTime: EditText) {
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

        /*bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
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

            binding.etLunchTimeStart.text = opensTime
        })*/


        bindingDialogTimePicker.btnDone.setOnClickListener {
            val hourOfDay = bindingDialogTimePicker.timePicker.hour
            val minute = bindingDialogTimePicker.timePicker.minute
            val selectedDate = "${hourOfDay}:${minute}"

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            startTime.setText(opensTime)
            dialog.dismiss()
        }

        dialog.show()
    }


    /*private fun openDialogTimePickerCloses(time: EditText) {
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

        val closesTime24 = Utils.customDateTimeFormat(
            closesTime,
            Constants.DATE_FORMAT_COLUMN_HH_MM_AA,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24
        ).toString()

        val closesTimeHours = Utils.customDateTimeFormat(
            closesTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_HH
        ).toString()

        val closesTimeMinutes = Utils.customDateTimeFormat(
            closesTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_MM
        ).toString()

        // set the value for current hours
        bindingDialogTimePicker.timePicker.currentHour = closesTimeHours.toInt()
        bindingDialogTimePicker.timePicker.currentMinute = closesTimeMinutes.toInt()

        bindingDialogTimePicker.btnDone.setOnClickListener {
            val hourOfDay = bindingDialogTimePicker.timePicker.hour
            val minute = bindingDialogTimePicker.timePicker.minute
            val selectedDate = "${hourOfDay}:${minute}"

            closesTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            time.setText(closesTime)
            dialog.dismiss()
        }

        *//*bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
            view: TimePicker,
            hourOfDay: Int,
            minute: Int
        ) {
            val selectedDate = "${hourOfDay}:${minute}"

            closesTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()
            binding.etLunchTimeEnd.text = closesTime

        })*//*

        dialog.show()
    }*/
}