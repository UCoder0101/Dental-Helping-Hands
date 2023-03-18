package com.dentalhelpinghands.activities.office

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.TimePicker
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Constants.Companion.DATE_FORMAT_DD_MM_YYYY_DASH
import com.dentalhelpinghands.common.Constants.Companion.DATE_FORMAT_MMMM_DD_YYYY_COMMA
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityOfficeSelectDateBinding
import com.dentalhelpinghands.databinding.DialogTimePickerBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OfficeSelectDateActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficeSelectDateBinding
    private var strRequestDate = ""
    var selectionDate = ""
    //private var lstModel = ArrayList<DataX>()
    private var lstModel: DataX? = null
    private val TAG = "select date :- "

    private lateinit var opensTime: String
    private lateinit var closesTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            R.anim.enter,
            R.anim.exit
        )
        binding = ActivityOfficeSelectDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
    }


    private fun initClick() {
        binding.btnNext.setOnClickListener {
            val selectedDates: List<Calendar> = binding.calendarView.selectedDates
            Logger.d(TAG, selectedDates.size)

            for(dates in selectedDates) {
                val inputFormat = dates.time
                Logger.d(TAG, inputFormat)
                val outputFormat = SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY_DASH)

                val formatedDate = outputFormat.format(inputFormat)
                Logger.d(TAG, formatedDate)

                if(selectionDate.isEmpty()) {
                    selectionDate = formatedDate
                    Logger.d("Empty Date", selectionDate)
                } else {
                    selectionDate = "$selectionDate,$formatedDate"
                    Logger.d("Response Date", selectionDate)
                }
            }

            Logger.d(TAG, selectionDate)
            val intent = Intent(activity, OfficePostJobActivity::class.java)
            intent.putExtra("selectedDate", selectionDate)
            intent.putExtra("openTime", binding.tvOpens.text)
            intent.putExtra("closeTime", binding.tvCloses.text)
            startActivity(intent)
        }

        binding.clOpens.setOnClickListener {
            openDialogTimePickerCloses(binding.tvOpens)
        }

        binding.clCloses.setOnClickListener {
            openDialogTimePickerCloses(binding.tvCloses)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {
        /*if(lstModel?.get(position)?.date != null) {
            val getSelectDate = lstModel!![position].date
            Logger.d(TAG, getSelectDate)
            if(getSelectDate.length > 1) {
                val getSelectDate = lstModel!![position].date
                Logger.d(TAG, getSelectDate)
                //binding.calendarView.selectedDates =
            } else {
                binding.calendarView.selectedDates = listOf(Utils.currentDateTimeCalendar())
            }
        }*/

        binding.calendarView.selectedDates = listOf(Utils.currentDateTimeCalendar())

        strRequestDate = Utils.currentDateTime(Constants.DATE_FORMAT_DD_MM_YYYY_DASH)!!
        val date: String = Utils.customDateTimeFormat(
            strRequestDate,
            Constants.DATE_FORMAT_DD_MM_YYYY_DASH,
            Constants.DATE_FORMAT_MMMM_DD_YYYY_COMMA
        ).toString()
        Logger.d(TAG, date)

        val customDay: String = Utils.customDateTimeFormat(
            strRequestDate,
            Constants.DATE_FORMAT_DD_MM_YYYY_DASH,
            Constants.DATE_FORMAT_DD
        ).toString()
        Logger.d(TAG, customDay)

        val customMonth: String = Utils.customDateTimeFormat(
            strRequestDate,
            Constants.DATE_FORMAT_DD_MM_YYYY_DASH,
            Constants.DATE_FORMAT_MM
        ).toString()
        Logger.d(TAG, customMonth)

        val customYear: String = Utils.customDateTimeFormat(
            strRequestDate,
            Constants.DATE_FORMAT_DD_MM_YYYY_DASH,
            Constants.DATE_FORMAT_YYYY
        ).toString()
        Logger.d(TAG, customYear)
//        Logger.debugOnLog("current_date", "$customDay-$customMonth-$customYear")

        if (!Utils.isValidationEmpty(customDay) && !Utils.isValidationEmpty(customMonth) && !Utils.isValidationEmpty(
                customYear)) {

        }

        opensTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.tvOpens.text = opensTime

        closesTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        binding.tvCloses.text = closesTime
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_select_a_date)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

    /*private fun openDialogTimePickerOpens() {
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
            binding.tvOpens.text = opensTime

        })

        dialog.show()
    }*/

    private fun openDialogTimePickerCloses(time: TextView) {
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

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            time.text = opensTime
            dialog.dismiss()
        }

        /*bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
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
            binding.tvCloses.text = closesTime

        })*/

        dialog.show()
    }
}