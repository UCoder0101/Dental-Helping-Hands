package com.dentalhelpinghands.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.dentalhelpinghands.R

open class BaseActivity : AppCompatActivity() {
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        hideKeyboard()

        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)

    }

    companion object {
        var progressDialog: ProgressDialog? = null

        fun showProgressDialog(activity: Activity, message: String?) {
            try {
                if (progressDialog == null || !progressDialog!!.isShowing) {
                    if (!activity.isDestroyed) {
                        progressDialog = ProgressDialog(activity)
                        progressDialog!!.setTitle(activity.resources.getString(R.string.app_name))
                        progressDialog!!.setMessage(message)
                        progressDialog!!.setCancelable(false)
                        progressDialog!!.show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                showProgressDialog(activity, message)
            }
        }

        fun hideProgressDialog() {
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
        }
    }


}