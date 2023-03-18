package com.dentalhelpinghands.common

import com.dentalhelpinghands.Api.DentalHelpingHandsApi
import com.dentalhelpinghands.Api.RetrofitBuilder

class Constants {
    companion object {

        //    security add end.
        var device_type = "android"
        var device_token = "abcd"


        /*external fun getEncryptionKey(): String?

        external fun getBaseUrl(): String?

        external fun getHeaderKey(): String?*/


        // TODO: 1/6/22 Release: Make the build to first check DEBUGGING_BUILD = false value set.
//        val IS_SHOW_LOG: Boolean = BuildConfig.DEBUG
        const val IS_SHOW_LOG: Boolean = true

        // TODO: 1/6/22 Make the build to first check DEBUGGING_BUILD_TEST = false value set.
//        val IS_TESTING_MODE: Boolean = BuildConfig.DEBUG
        const val IS_TESTING_MODE: Boolean = false

        // TODO: 1/6/22 SPLASH_TIME_OUT = 3000
        const val SPLASH_TIME_OUT: Long = 3000
//        const val SPLASH_TIME_OUT: Long = 300

        //val TAG: String = java.lang.String.valueOf(R.string.app_name)

        val TAG: String = "Dental SignUp user response : "


        //    class name
        var IS_FROM_TERM_AND_CONDITIONS = "IS_FROM_TERM_AND_CONDITIONS"
        var IS_FROM_PRIVACY_POLICY = "IS_FROM_PRIVACY_POLICY"
        var IS_FROM_ABOUT_US = "IS_FROM_ABOUT_US"

        //    intent key
        var INTENT_KEY_IS_UPDATE = "INTENT_KEY_IS_UPDATE"
        var INTENT_IS_FROM = "INTENT_IS_FROM"
        var INTENT_KEY_EMAIL = "INTENT_KEY_EMAIL"

        const val LINK_TERM_AND_CONDITIONS =
            "https://client.appmania.co.in/DentalHelpingHands/termsandcondition.html"
        const val LINK_PRIVACY_POLICY =
            "https://client.appmania.co.in/DentalHelpingHands/privacypolicy.html"
        const val LINK_ABOUT_US = "https://client.appmania.co.in/DentalHelpingHands/aboutus.html"

        //        date format
        const val DATE_FORMAT_DD_MM_YYYY_DASH = "dd-MM-yyyy"
        const val DATE_FORMAT_MMMM_DD_YYYY_COMMA = "MMMM, dd yyyy"
        const val DATE_FORMAT_DD = "dd"
        const val DATE_FORMAT_MM = "MM"
        const val DATE_FORMAT_YYYY = "yyyy"
        var DATE_FORMAT_COLUMN_HH_MM_AA = "hh:mm aa"
        var DATE_FORMAT_COLUMN_HH_MM_24 = "HH:mm"
        var DATE_FORMAT_COLUMN_HH = "HH"
        var DATE_FORMAT_COLUMN_MM = "mm"

        // BASE_URL
        //const val BASE_URL = "https://client.appmania.co.in/DentalHelpingHands/Api/"

        //create retrofit Builder
        val retrofitBuilder =
            RetrofitBuilder.getClient(Preference.getStringName(Preference.LOGIN_TOKEN)!!)!!
                .create(DentalHelpingHandsApi::class.java)

        // Api Calling Response
        const val ADD_DATA_USER = "user_add_data_response"

        //        1=Dental user Type,2=Professional User Type
        const val USER_TYPE_DENTAL = 1
        const val USER_TYPE_PROFESSIONAL = 2

        //approve or reject action_type
        const val ACTION_TYPE_APPROVE = "Approve"
        const val ACTION_TYPE_REJECTED = "Rejected"

        //approve job or not
        const val IS_APPROVE = 1
        const val IS_NOT_APPROVE = 0

        //intent key
        const val KEY_IS_UPDATE = "isUpdate"

        //request code
        const val REQUEST_CODE_1 = 1
        const val REQUEST_CODE_2 = 2
    }

}