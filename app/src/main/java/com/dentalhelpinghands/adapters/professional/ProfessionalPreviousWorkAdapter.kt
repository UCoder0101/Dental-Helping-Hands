package com.dentalhelpinghands.adapters.professional

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dentalhelpinghands.activities.professional.ProfessionalPreviousWorkDetailActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Logger
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.RowProfessionalPreviousWorkItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import java.util.regex.Pattern


class ProfessionalPreviousWorkAdapter(var activity: Activity, lstModel: MutableList<DataX>) :
    RecyclerView.Adapter<ProfessionalPreviousWorkAdapter.ViewHolder>() {
    private lateinit var lstModel: MutableList<DataX>
    //private var lstModel = mutableListOf<DataX>()
    private val TAG = "professional previous work adapter item size"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowProfessionalPreviousWorkItemBinding.inflate(
            LayoutInflater.from(activity),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: DataX = lstModel[position]
        val jobDetail: DataX = model.jobs_detail!!


        holder.binding.tvProfessional.text = jobDetail.professional
        holder.binding.tvOfficeAddress.text = jobDetail.office_address
        holder.binding.tvPhoneNumber.text = jobDetail.contact

        //for date
        //val date = Utils.printDifference()
        /*val date = model.date
        val delim = ","
        val arr = Pattern.compile(delim).split(date).asList()
        val dateSingle = arr[0]
        val formattedDate= Utils.customDateTimeFormat(dateSingle, Constants.DATE_FORMAT_DD_MM_YYYY_DASH, Constants.DATE_FORMAT_MMMM_DD_YYYY_COMMA)
        holder.binding.tvDate.text = formattedDate*/

        val date = jobDetail.date
        val delim = ","
        val arr = Pattern.compile(delim).split(date).asList()
        val dateSingle = arr[0]

        val formattedDate= Utils.customDateTimeFormat(dateSingle, Constants.DATE_FORMAT_DD_MM_YYYY_DASH, Constants.DATE_FORMAT_MMMM_DD_YYYY_COMMA)
        holder.binding.tvDate.text = formattedDate

        holder.binding.tvPrice.text = jobDetail.price.toString() + "/per " + jobDetail.price_type
        holder.binding.tvPracticeName.text = jobDetail.practice_name

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
        Logger.d(TAG, lstModel.size)
        return lstModel.size
    }

    inner class ViewHolder(val binding: RowProfessionalPreviousWorkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            itemView.setOnClickListener {
                val model: DataX? = lstModel[adapterPosition]
                if (model != null) {
                    val intent =
                        Intent(activity, ProfessionalPreviousWorkDetailActivity::class.java)
                    intent.putExtra("previousWorkDetail", model.jobs_detail)
                    activity.startActivity(intent)
                }
            }
        }
    }

    init {
        this.lstModel = lstModel
    }
}
