package com.dentalhelpinghands.common

import android.content.Context
import android.content.SharedPreferences
import com.dentalhelpinghands.MyDHHClass
import com.dentalhelpinghands.models.office.loginModel.DataX
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import com.google.gson.Gson

class Preference {

     companion object {

         const val USER_ID = "user_id"
         const val LOGIN_TOKEN = "token"
         const val USER_TYPE = "user_type"

//         const val JOB_ID = "job_id"
//         const val JOB_Apply_id = "job_apply_id"
//         const val EMAIL_ID = "email"
//         const val USER_Name = "username"

         const val FIREBASE_TOKEN = "FIREBASE_TOKEN"

         const val KEY_IS_CLEAR_DATA = "KEY_IS_CLEAR_DATA"
         const val VALUE_DEFAULT_IS_CLEAR_DATA = false

         const val User_Object = "User_Object"

         private fun get(): SharedPreferences {
             return MyDHHClass.getAppInstance().getSharedPreferences(
                 "DentalHelpingHands",
                 Context.MODE_PRIVATE
             )
         }

         fun getBooleanValue(Key: String): Boolean {
             return get().getBoolean(Key, false)
         }

         fun setBooleanValue(Key: String, TrueOrFalse: Boolean) {
             get().edit().putBoolean(Key, TrueOrFalse).apply()
         }

         fun getInteger(Key: String): Int {
             return get().getInt(Key, 0)
         }

         fun setInteger(Key: String, value: String?) {
             get().edit().putInt(Key, 0).apply()
         }

         //only use for single object
         fun setUserObject(data: DataX?) {
             val gson = Gson()
             val json = gson.toJson(data)
             SetStringName(
                User_Object,
                 json)
         }

         fun getUserObject(): DataX? {
             val gson = Gson()
             val json: String? =
                 getStringName(User_Object)
             return gson.fromJson(json, DataX::class.java)
         }

         fun getStringName(Key: String?): String? {
             return get().getString(Key, "")
         }

         fun SetStringName(Key: String?, Value: String?) {
             get().edit().putString(Key, Value).apply()
         }

     }
}