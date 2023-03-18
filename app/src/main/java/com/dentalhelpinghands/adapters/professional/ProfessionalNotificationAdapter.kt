package com.dentalhelpinghands.adapters.professional

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.professional.ProfessionalPreviousWorkDetailActivity
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.DialogApproveBinding
import com.dentalhelpinghands.databinding.DialogDeleteBinding
import com.dentalhelpinghands.databinding.RowProfessionalNotificationItemBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
import okhttp3.internal.Util


class ProfessionalNotificationAdapter(
    var activity: Activity,
    lstModel: MutableList<DataX>,
    dialog: ProfessionalNotificationAdapter.OpenCloseNotificationDialog
) :
    RecyclerView.Adapter<ProfessionalNotificationAdapter.ViewHolder>() {
    //private var lstModel: List<DataX>
    private lateinit var lstModel: MutableList<DataX>
    private var openCloseNotificationDialog: OpenCloseNotificationDialog? = null

    interface OpenCloseNotificationDialog {
        fun confirmNotificationDialog(position: Int)

        fun deleteNotificationDialog(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowProfessionalNotificationItemBinding.inflate(
            LayoutInflater.from(activity),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: DataX? = lstModel[position]
        if (model != null) {
            Utils.setProfileImage(activity, model.profile, holder.binding.ivProfile)
            holder.binding.tvProfessional.text = model.practice_name
            holder.binding.tvNotificationMsg.text = model.notification_discription

            if (model.is_approved.equals("0")) {
                holder.binding.llBtn.visibility = View.VISIBLE
            } else {
                holder.binding.llBtn.visibility = View.GONE
            }
        }

        when (position) {
            0 -> {
                holder.binding.viewDummyTop.visibility = View.VISIBLE
                holder.binding.viewDummyBottom.visibility = View.VISIBLE
            }
            lstModel.size - 1 -> {
                holder.binding.viewDummyTop.visibility = View.GONE
                holder.binding.viewDummyBottom.visibility = View.GONE
            }
            else -> {
                holder.binding.viewDummyTop.visibility = View.GONE
                holder.binding.viewDummyBottom.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return lstModel.size
    }

    inner class ViewHolder(val binding: RowProfessionalNotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {
                val dataX: DataX? = lstModel[adapterPosition]
                if (dataX != null) {
                    val intent =
                        Intent(activity, ProfessionalPreviousWorkDetailActivity::class.java)
                    intent.putExtra("notificationDetail", dataX)
                    activity.startActivity(intent)
                }
            }

            binding.btnConfirm.setOnClickListener {
                openDialogApprove(adapterPosition)
            }
            binding.btnDelete.setOnClickListener {
                openDialogDelete(adapterPosition)
            }
        }
    }

    init {
        this.lstModel = lstModel
        this.openCloseNotificationDialog = dialog
    }

    private fun openDialogApprove(adapterPosition: Int) {
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
        val bindingDialogApprove = DialogApproveBinding.inflate(activity.layoutInflater)
        dialog.setContentView(bindingDialogApprove.root)
        dialog.setTitle("")

        bindingDialogApprove.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogApprove.btnYes.setOnClickListener {
            dialog.dismiss()
            openCloseNotificationDialog!!.confirmNotificationDialog(adapterPosition)
        }

        dialog.show()
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
            openCloseNotificationDialog!!.deleteNotificationDialog(adapterPosition)
        }

        dialog.show()
    }
}
