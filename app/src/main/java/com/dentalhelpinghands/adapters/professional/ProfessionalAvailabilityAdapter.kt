package com.dentalhelpinghands.adapters.professional

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dentalhelpinghands.R
import com.dentalhelpinghands.databinding.RowProfessionalAvailabilityItemBinding
import com.dentalhelpinghands.models.professional.ProfessionalAvailabilityModel


class ProfessionalAvailabilityAdapter(
    var activity: Activity,
    lstModel: List<ProfessionalAvailabilityModel>
) :
    RecyclerView.Adapter<ProfessionalAvailabilityAdapter.ViewHolder>() {
    private var lstModel: List<ProfessionalAvailabilityModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowProfessionalAvailabilityItemBinding.inflate(
                LayoutInflater.from(activity),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model: ProfessionalAvailabilityModel = lstModel[position]
        holder.binding.text1.text = model.name

        if (model.isSelected) {
            holder.binding.text1.setBackgroundResource(R.drawable.shape_rect_bg_spinner_item_selected)
        } else {
            holder.binding.text1.setBackgroundResource(R.drawable.shape_rect_bg_spinner_item)
        }

    }

    override fun getItemCount(): Int {
        return lstModel.size
    }

    inner class ViewHolder(val binding: RowProfessionalAvailabilityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener?.itemClick(adapterPosition)
            }

        }
    }

    init {
        this.lstModel = lstModel
    }

    interface OnItemClickListener {
        fun itemClick(position: Int)
    }

    companion object {
        var onItemClickListener: OnItemClickListener? = null
    }

}
