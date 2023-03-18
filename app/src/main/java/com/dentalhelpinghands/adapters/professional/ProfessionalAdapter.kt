package com.dentalhelpinghands.adapters.professional

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dentalhelpinghands.activities.professional.ProfessionalDetailActivity
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.RowProfessionalItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import java.util.regex.Pattern


class ProfessionalAdapter(var activity: Activity, lstModel: MutableList<DataX>) :
    RecyclerView.Adapter<ProfessionalAdapter.ViewHolder>() {
    private lateinit var lstModel: MutableList<DataX>
    private val TAG = "professional adapter item size"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowProfessionalItemBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: DataX = lstModel[position]
        holder.binding.tvProfessional.text = model.professional
        holder.binding.tvOfficeAddress.text = model.office_address
        holder.binding.tvPhoneNumber.text = model.contact

        //for date
        //val date = Utils.printDifference()
        val date = model.date
        val delim = ","
        val arr = Pattern.compile(delim).split(date).asList()
        val dateSingle = arr[0]

        val formattedDate= Utils.customDateTimeFormat(dateSingle, Constants.DATE_FORMAT_DD_MM_YYYY_DASH, Constants.DATE_FORMAT_MMMM_DD_YYYY_COMMA)
        holder.binding.tvDate.text = formattedDate
        //Logger.d(TAG, formattedDate!!)

        holder.binding.tvPracticeName.text = model.practice_name
        holder.binding.tvRequestPayRate.text = model.price.toString() + "/per " + model.price_type


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
        com.dentalhelpinghands.common.Logger.d(TAG, lstModel.size)
        return lstModel.size
    }

    inner class ViewHolder(val binding: RowProfessionalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {
                val dataX: DataX? = lstModel[adapterPosition]
                if(dataX != null) {
                    val intent = Intent(activity, ProfessionalDetailActivity::class.java)
                    intent.putExtra("requestDataModel", dataX)
                    activity.startActivity(intent)
                }
            }

        }
    }

    init {
        this.lstModel = lstModel
    }
}
