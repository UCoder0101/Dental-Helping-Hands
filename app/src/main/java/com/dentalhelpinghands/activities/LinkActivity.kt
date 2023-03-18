package com.dentalhelpinghands.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dentalhelpinghands.R
import com.dentalhelpinghands.common.Constants
import com.dentalhelpinghands.common.Utils
import com.dentalhelpinghands.databinding.ActivityLinkBinding


class LinkActivity : BaseActivity() {
    private lateinit var binding: ActivityLinkBinding
    private var isFrom: String? = null
    private var title: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        getPreviousData()
        initToolBar()
        initView()
        initClick()
    }

    private fun getPreviousData() {
        val mBundle = intent.extras
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_IS_FROM)) {
                isFrom = mBundle.getString(Constants.INTENT_IS_FROM)

                if (!Utils.isValidationEmpty(isFrom)) {
                    if (isFrom.equals(Constants.IS_FROM_TERM_AND_CONDITIONS)) {
                        title = getString(R.string.title_terms_and_conditions)
                    } else if (isFrom.equals(Constants.IS_FROM_PRIVACY_POLICY)) {
                        title = getString(R.string.title_privacy)
                    } else if (isFrom.equals(Constants.IS_FROM_ABOUT_US)) {
                        title = getString(R.string.title_about_us)
                    }
                }
            }
        }
    }

    private fun initClick() {

    }

    private fun initView() {
        binding.webView.isVerticalScrollBarEnabled = false


        var loadUrl = ""
        if (isFrom.equals(Constants.IS_FROM_TERM_AND_CONDITIONS)) {
            loadUrl = Constants.LINK_TERM_AND_CONDITIONS
        } else if (isFrom.equals(Constants.IS_FROM_PRIVACY_POLICY)) {
            loadUrl = Constants.LINK_PRIVACY_POLICY
        } else if (isFrom.equals(Constants.IS_FROM_ABOUT_US)) {
            loadUrl = Constants.LINK_ABOUT_US
        }

        binding.webView.settings.setJavaScriptEnabled(true)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgressDialog(activity, activity.resources.getString(R.string.msg_loading))
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                hideProgressDialog()
                super.onPageFinished(view, url)
            }

        }
        binding.webView.loadUrl(loadUrl)
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text = title

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }

}