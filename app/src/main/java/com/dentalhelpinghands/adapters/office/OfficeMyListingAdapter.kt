package com.dentalhelpinghands.adapters.office

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.view.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.office.OfficePostDetailActivity
import com.dentalhelpinghands.activities.office.OfficePostEditActivity
import com.dentalhelpinghands.activities.office.OfficeSelectDateActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.DialogDeleteBinding
import com.dentalhelpinghands.databinding.RowOfficeMyListingItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


class OfficeMyListingAdapter(var activity: Activity, lstModel: MutableList<DataX>, deleteJobPost: DeleteJobPostClick) : RecyclerView.Adapter<OfficeMyListingAdapter.ViewHolder>() {
    private lateinit var lstModel: MutableList<DataX>
    private var clickDeleteBtn: DeleteJobPostClick? = null

    interface DeleteJobPostClick {
        fun jobPostDelete(adapterPosition: Int)

        fun itemClickUpdate(adapterPosition: Int)
    }


    companion object {
        private val TAG = "Adapter List Size"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowOfficeMyListingItemBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: DataX = lstModel[position]
        holder.binding.tvProfessional.text = model.professional
        holder.binding.tvOfficeAddress.text = model.office_address
        holder.binding.tvContact.text = model.contact

        //for date
        val date = model.date
        Logger.d(TAG, date)
        val delim = ","
        val arr = Pattern.compile(delim).split(date).asList()
        val dateSingle = arr[0]
        Logger.d(TAG, dateSingle)
        val formattedDate=Utils.customDateTimeFormat(dateSingle,Constants.DATE_FORMAT_DD_MM_YYYY_DASH,Constants.DATE_FORMAT_MMMM_DD_YYYY_COMMA)
        holder.binding.tvDate.text = formattedDate

        holder.binding.tvPracticeName.text = model.practice_name
        holder.binding.tvPrice.text = model.price.toString() + "/per " + model.price_type
        if(model.applyedJobs == 0 || model.applyedJobs == 1) {
            holder.binding.tvCount.visibility = View.GONE
            holder.binding.tvCount.text = model.applyedJobs.toString()
        } else {
            holder.binding.tvCount.visibility = View.VISIBLE
            holder.binding.tvCount.text = model.applyedJobs.toString()
        }

        when (position) {
            0 -> {
                holder.binding.viewDummyTop.visibility = View.VISIBLE
                holder.binding.viewDummyBottom.visibility = View.GONE
            }
            lstModel.size - 1 -> {
                holder.binding.viewDummyTop.visibility = View.GONE
                holder.binding.viewDummyBottom.visibility = View.VISIBLE
            }
            else -> {
                holder.binding.viewDummyTop.visibility = View.GONE
                holder.binding.viewDummyBottom.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return lstModel.size
    }

    inner class ViewHolder(val binding: RowOfficeMyListingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val dataX: DataX? = lstModel[adapterPosition]
                if(dataX != null) {
                    val intent = Intent(activity, OfficePostDetailActivity::class.java)
                    intent.putExtra("requestDataModel", dataX)
                    activity.startActivity(intent)
                }
            }

            binding.ivEdit.setOnClickListener {
                clickDeleteBtn!!.itemClickUpdate(adapterPosition)

            }

            binding.ivDelete.setOnClickListener {
                openDialogDelete(adapterPosition)
            }
        }
    }

    init {
        this.lstModel = lstModel
        this.clickDeleteBtn = deleteJobPost
    }

    private fun openDialogDelete(adapterPosition: Int) {
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
        val bindingDialogDelete = DialogDeleteBinding.inflate(activity.layoutInflater)
        dialog.setContentView(bindingDialogDelete.root)
        dialog.setTitle("")

        bindingDialogDelete.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogDelete.btnYes.setOnClickListener {
            dialog.dismiss()
            clickDeleteBtn!!.jobPostDelete(adapterPosition)
        }

        dialog.show()
    }
}
