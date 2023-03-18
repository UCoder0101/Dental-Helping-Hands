package com.dentalhelpinghands.fragments.office

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.office.OfficeMyListingActivity
import com.dentalhelpinghands.activities.office.OfficeSelectDateActivity
import com.dentalhelpinghands.common.Preference
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.FragmentOfficeHomeBinding
import com.dentalhelpinghands.fragments.BaseFragment

class OfficeHomeFragment : BaseFragment() {
    private var _binding: FragmentOfficeHomeBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentOfficeHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
        initView()
        initClick()
    }

    private fun initClick() {
        binding.btnPostAJob.setOnClickListener {
            val intent = Intent(activity, OfficeSelectDateActivity::class.java)
            startActivity(intent)
        }
        binding.btnMyListing.setOnClickListener {
            val intent = Intent(activity, OfficeMyListingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val responseObjectModel = Preference.getUserObject()
        if(responseObjectModel != null) {
            val userProfile = responseObjectModel.profile

            Utils.setProfileImage(activity, userProfile, binding.ivProfile)
        }
    }

    private fun initView() {


    }

    private fun initToolBar() {

        binding.ilToolBar.ivHeaderMenu.setOnClickListener {
            onBackClickListener?.backClick()
        }

        binding.ilToolBar.tvHeaderTitle.text = activity.getText(R.string.title_dentist_location)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}