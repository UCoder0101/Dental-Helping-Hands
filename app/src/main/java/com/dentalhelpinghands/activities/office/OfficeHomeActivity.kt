package com.dentalhelpinghands.activities.office

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.ContactUsActivity
import com.dentalhelpinghands.activities.FlowTypeActivity
import com.dentalhelpinghands.activities.LinkActivity
import com.dentalhelpinghands.common.*
import com.dentalhelpinghands.databinding.ActivityOfficeHomeBinding
import com.dentalhelpinghands.databinding.DialogLogoutBinding
import com.dentalhelpinghands.fragments.office.OfficeHomeFragment
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfficeHomeActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficeHomeBinding
    private lateinit var fragment: Fragment
    private lateinit var fragmentClass: Class<*>

    private var isOpenHome: Boolean = true
    private var TAG = "USER_LOGOUT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficeHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initView()
        initClick()
    }

    private fun initClick() {

    }

    private fun initView() {

        fragmentClass = OfficeHomeFragment::class.java
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.officeFrameLayout, fragment).commit()

        OfficeHomeFragment.onBackClickListener = object : OfficeHomeFragment.OnBackClickListener {
            override fun backClick() {
                isOpenHome = true
                processDrawer()
            }
        }


        //Listener to handle the menu item click. It returns the position of the menu item clicked. Based on that you can switch between the fragments.


        //Listener to handle the menu item click. It returns the position of the menu item clicked. Based on that you can switch between the fragments.
        binding.officeNavigationDrawer.onMenuItemClickListener =
            OfficeNavigationDrawer.OnMenuItemClickListener { position ->
                println("Position $position")
                isOpenHome = false


                //Listener for drawer events such as opening and closing.
                binding.officeNavigationDrawer.drawerListener =
                    object : OfficeNavigationDrawer.DrawerListener {
                        override fun onDrawerOpened() {}
                        override fun onDrawerOpening() {}
                        override fun onDrawerClosing() {
                            println("Drawer closed")

//                        try {
//                            fragment = fragmentClass.newInstance() as Fragment
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                        val fragmentManager: FragmentManager = supportFragmentManager
//                        fragmentManager.beginTransaction().setCustomAnimations(
//                            android.R.animator.fade_in,
//                            android.R.animator.fade_out
//                        ).replace(R.id.frameLayout, fragment).commit()
                            if (isOpenHome) {
                                isOpenHome = false
                            } else {
                                when (position) {
                                    0 -> {
                                        val intent =
                                            Intent(activity, OfficeNotificationActivity::class.java)
                                        startActivity(intent)

//                                OfficeNotificationActivity.onBackClickListener =
//                                    object : OfficeNotificationActivity.OnBackClickListener {
//                                        override fun backClick() {
//                                            processDrawer()
//                                        }
//                                    }
                                    }
                                    1 -> {

                                        val intent =
                                            Intent(activity, OfficeMyProfileActivity::class.java)
                                        startActivity(intent)
                                    }
                                    2 -> {
                                        startActivity(
                                            Intent(
                                                activity,
                                                LinkActivity::class.java
                                            ).putExtra(
                                                Constants.INTENT_IS_FROM,
                                                Constants.IS_FROM_PRIVACY_POLICY
                                            )
                                        )
                                    }
                                    3 -> {
                                        startActivity(
                                            Intent(
                                                activity,
                                                LinkActivity::class.java
                                            ).putExtra(
                                                Constants.INTENT_IS_FROM,
                                                Constants.IS_FROM_TERM_AND_CONDITIONS
                                            )
                                        )
                                    }
                                    4 -> {
                                        startActivity(
                                            Intent(
                                                activity,
                                                LinkActivity::class.java
                                            ).putExtra(
                                                Constants.INTENT_IS_FROM,
                                                Constants.IS_FROM_ABOUT_US
                                            )
                                        )
                                    }
                                    5 -> {
                                        val intent = Intent(activity, ContactUsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    6 -> {
                                        openDialogLogout()
                                    }
                                }
                            }


                        }

                        override fun onDrawerClosed() {}
                        override fun onDrawerStateChanged(newState: Int) {
                            println("State $newState")
                        }
                    }
            }
    }

    fun processDrawer() {
        if (binding.officeNavigationDrawer.isDrawerOpen) {
            binding.officeNavigationDrawer.closeDrawer()
        } else {
            binding.officeNavigationDrawer.openDrawer()
        }
    }

    private fun openDialogLogout() {
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
//        dialog.setContentView(R.layout.dialog_logout)
        val bindingDialogLogout: DialogLogoutBinding = DialogLogoutBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogLogout.root)
        dialog.setTitle("")

        bindingDialogLogout.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogLogout.btnYes.setOnClickListener {
            dialog.dismiss()

            hideKeyboard()
            callLogOutApi()
        }

        dialog.show()
    }

        private fun callLogOutApi() {

        if(Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callLogOutApi(RetrofitBuilder.HEADER_KEY_VALUE, Preference.getStringName(Preference.LOGIN_TOKEN)!!, Preference.getStringName(Preference.USER_ID)!!)

            call.enqueue(object : Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<ResponseObjectModel>,
                    response: Response<ResponseObjectModel>) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        if (responseBody.ResponseCode == 1) {

                            Utils.showToast(activity, responseBody.ResponseMsg)
                            Utils.clearPreferenceLogout()
                            startActivity(
                                Intent(
                                    activity,
                                    FlowTypeActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                            //finishAffinity()
                        }  else {
                            Utils.showAlert(activity,responseBody.ResponseMsg)
                        }
                    }else {
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
}