package com.dentalhelpinghands.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.karumi.dexter.listener.PermissionDeniedResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeoutException
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection


class Utils {
    companion object {

        var progressDialog: ProgressDialog? = null

        fun isValidationEmpty(value: String?): Boolean {
            return value == null || value.isEmpty() || value.equals(
                "null",
                ignoreCase = true
            ) || value.equals("", ignoreCase = true) || value.isEmpty()
        }

        fun showAlert(activity: Activity, message: String?) {
            showAlert(activity, message, false)
        }


        fun showAlertsWithTitle(activity: Activity?, title: String?, message: String?) {
            showAlerts(activity, title, message)
        }


        fun showAlerts(activity: Activity?, title: String?, message: String?) {
            if (activity != null && !activity.isDestroyed && !activity.isFinishing) {
                activity.runOnUiThread(Runnable {
                    val builder = android.app.AlertDialog.Builder(
                        activity,
                        R.style.CustomAppCompatAlertDialogStyle
                    )
                    builder.setTitle(activity.getString(R.string.app_name))
                    builder.setMessage(message)
                    builder.setPositiveButton(activity.getString(R.string.ok), null)
                    builder.show()
                })
            }
        }

        fun showAlert(activity: Activity, message: String?, isFinish: Boolean) {
            val builder = AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
            builder.setTitle(activity.resources.getString(R.string.app_name))
            builder.setMessage(message)
            builder.setPositiveButton(activity.getString(R.string.btn_ok)) { _, _ ->
                if (isFinish) {
                    activity.finish()
                }
            }
            builder.setCancelable(!isFinish)
            builder.show()
        }

        fun showAlert(
            activity: Activity,
            message: String?,
            listener: DialogInterface.OnClickListener
        ) {
            val builder = AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
            builder.setTitle(activity.resources.getString(R.string.app_name))
            builder.setMessage(message)
            builder.setPositiveButton(activity.getString(R.string.btn_ok), listener)
            builder.setCancelable(false)
            builder.show()
        }


        fun setProfileImage(activity: Activity?, path: String?, imageView: ImageView?) {
            if (imageView != null) {
                Glide.with(activity!!).load(path).placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder).into(imageView)
            }
        }




        //for signup details
        fun convertValueToRequestBody(value: String?): RequestBody? {
            return RequestBody.create(MediaType.parse("text/plain"), value)
        }

        /*fun isUserName(username: String): Boolean {
            return if (TextUtils.isEmpty(username)) false else RegexOption.IGNORE_CASE.IsMatch()

        }*/


        fun isValidEmail(email: String): Boolean {
            return if (TextUtils.isEmpty(email)) false else Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        }

        fun isEmail(email: String): Boolean {
            val lstPattern: ArrayList<Pattern> = ArrayList()
            lstPattern.add(Pattern.compile("[0-9\\+\\.\\_\\%\\-\\+]{1,256}"))
            lstPattern.add(Pattern.compile("\\@"))
            lstPattern.add(Pattern.compile("[0-9][0-9\\-]{0,64}"))
            lstPattern.add(Pattern.compile("\\("))
            lstPattern.add(Pattern.compile("\\."))
            lstPattern.add(Pattern.compile("[0-9][0-9\\-]{0,25}"))
            lstPattern.add(Pattern.compile("\\)"))

            for (rx in lstPattern) if (rx.matcher(email).find()) return true
            return false
        }

        fun isPassword(password: String): Boolean {
            return if (TextUtils.isEmpty(password)) false else password.length < 6
        }

        fun isValidPassword(password: String, minPasswordLength: Int): Boolean {
            val pattern: Pattern

            val passwordPattern = "^.{$minPasswordLength,}\$"
            pattern = Pattern.compile(passwordPattern)
            val matcher: Matcher? = password.let { pattern.matcher(it) }
            if (matcher != null) {
                return matcher.matches()
            }
            return false
        }


        fun getMD5(data: String): String? {
            val result: String
            val md: MessageDigest
            try {
                md = MessageDigest.getInstance("MD5")
                md.update(data.toByteArray(StandardCharsets.UTF_8))
                result = String.format(Locale.ROOT, "%032x", BigInteger(1, md.digest()))
            } catch (e: NoSuchAlgorithmException) {
                throw IllegalStateException(e)
            }
            return result
        }

        fun isPassMatch(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }

        fun currentDateTime(outputDateFormat: String?): String? {
            val c = Calendar.getInstance()
            @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat(outputDateFormat)
            return df.format(c.time)
        }

        fun currentDateTimeCalendar(): Calendar {
            val c = Calendar.getInstance()
//            @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat(outputDateFormat)
            return c
        }

        fun customDateTimeFormat(
            dateTime: String?,
            inputDateFormat: String?,
            outputDateFormat: String?
        ): String? {
            return if (!isValidationEmpty(dateTime)) {
                @SuppressLint("SimpleDateFormat") val input = SimpleDateFormat(inputDateFormat)
                @SuppressLint("SimpleDateFormat") val output = SimpleDateFormat(outputDateFormat)
                try {
                    val date = dateTime?.let { input.parse(it) } // parse input
                    output.format(Objects.requireNonNull(date)) // format output
                } catch (e: ParseException) {
                    e.printStackTrace()
                    dateTime
                }
            } else {
                ""
            }
        }

        fun hideProgressDialog(activity: Activity?) {
//        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                activity.runOnUiThread(() -> progressDialog.dismiss());
//            }
//        }
            if (progressDialog != null && progressDialog!!.isShowing()) {
                try {
                    if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
                        activity.runOnUiThread(Runnable { progressDialog!!.dismiss() })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        fun printDifference(startDate: Date, endDate: Date): String {

            val convTime: String
            var different = startDate.time - endDate.time

            val secondsInMilli: Long = 1000
            val minutesInMilli: Long = secondsInMilli * 60
            val hoursInMilli: Long = minutesInMilli * 60
            val daysInMilli: Long = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            different %= daysInMilli


            val elapsedHours = different / hoursInMilli
            different %= hoursInMilli


            val elapsedMinutes = different / minutesInMilli
            different %= minutesInMilli

            val elapsedSeconds = different / secondsInMilli

            if (elapsedDays > 0 && elapsedDays < 7) {
                convTime = "$elapsedDays + day ago"
            } else if (elapsedDays >= 7) {
                if (elapsedDays > 360) {
                    convTime = ((elapsedDays / 360).toString()) + " year ago"
                } else if (elapsedDays > 30) {
                    convTime = ((elapsedDays / 30)).toString() + " month ago"
                } else {
                    convTime = ((elapsedDays / 7)).toString() + " week ago"
                }
            } else if (elapsedHours > 0) {
                convTime = "$elapsedHours + hour ago"
            } else if (elapsedMinutes > 0) {
                convTime = "$elapsedMinutes +  min ago"
            } else if (elapsedSeconds > 0) {
                convTime = "$elapsedSeconds +  sec ago"
            } else {
                convTime = "now"
            }
            return convTime
        }


        /*fun saveImage(myBitmap: Bitmap):String {
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
            // have the object build the directory structure, if needed.
            //Log.d("fee",wallpaperDirectory.toString())
            if (!wallpaperDirectory.exists())
            {

                wallpaperDirectory.mkdirs()
            }

            try
            {
                //Log.d("heel",wallpaperDirectory.toString())
                val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
                fo.close()
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

                return f.getAbsolutePath()
            }
            catch (e1: IOException) {
                e1.printStackTrace()
            }

            return ""
        }*/

        //    public static String setUserType(Activity activity, String user) {
        //        String userType = "";
        //        if (user.equalsIgnoreCase("1")) {
        //            userType = activity.getString(R.string.individual);
        //        } else {
        //            userType = activity.getString(R.string.community);
        //        }
        //        return userType;1
        //    }

        fun clearPreferenceLogout() {
            Preference.setUserObject(null)
            Preference.SetStringName(Preference.USER_ID, "")
            Preference.SetStringName(Preference.LOGIN_TOKEN, "")
            Preference.SetStringName(Preference.USER_TYPE, "")
            //Preference.SetStringName(Preference.ACTION_TYPE, "")
            Preference.SetStringName(Preference.USER_ID, "")
            /*Preference.SetStringName(Preference.JOB_ID, "")
            Preference.SetStringName(Preference.JOB_Apply_id, "")
            Preference.SetStringName(Preference.EMAIL_ID, "")*/

        }

        fun handleErrorCode(activity: Activity?, t: Throwable) {
            Logger.d("test_handleErrorCode:", Gson().toJson(t))
            if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
                hideProgressDialog(activity)

                if (isNetworkAvailable(activity, false, false)) {
                    if (t is HttpException) {
                        val statusCode = t.code()
                        Logger.d("test_handleErrorCode_1", statusCode)
                        when (statusCode) {
                            HttpsURLConnection.HTTP_UNAUTHORIZED -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_unauthorised_user)
                            )
                            HttpsURLConnection.HTTP_INTERNAL_ERROR -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_internal_server_error)
                            )
                            HttpsURLConnection.HTTP_BAD_REQUEST -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_low_internet_speed)
                            )
                            HttpsURLConnection.HTTP_PRECON_FAILED -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_request_failed)
                            )
                            HttpsURLConnection.HTTP_GATEWAY_TIMEOUT -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_request_timed_out)
                            )
                            0 -> showAlertWithoutTitle(
                                activity,
                                activity.getString(R.string.error_code_internet_connection)
                            )
                            else -> showAlertWithoutTitle(
                                activity,
                                t.getLocalizedMessage()
                            )
                        }
                    } else if (t is JsonSyntaxException) {
                        showAlertWithoutTitle(
                            activity,
                            activity.getString(R.string.error_code_internal_server_error)
                        )
                    } else if (t is IOException) {
                        showAlertWithoutTitle(
                            activity,
                            activity.getString(R.string.error_code_io_exception)
                        )
                    } else if (t is TimeoutException) {
                        showAlertWithoutTitle(
                            activity,
                            activity.getString(R.string.error_code_request_timed_out)
                        )
                    } else {
                        showAlertWithoutTitle(
                            activity,
                            t.message
                        )
                    }
                } else {
                    showAlertWithoutTitle(
                        activity,
                        activity.getString(R.string.error_code_internet_connection)
                    )
                }

            }
        }

        fun showAlertWithoutTitle(activity: Activity?, message: String?) {
            if (activity != null && !activity.isDestroyed && !activity.isFinishing) {
                activity.runOnUiThread(Runnable {
                    val builder = android.app.AlertDialog.Builder(
                        activity,
                        R.style.CustomAppCompatAlertDialogStyle
                    )
                    builder.setTitle(activity.getString(R.string.app_name))
                    builder.setMessage(message)
                    builder.setPositiveButton(activity.getString(R.string.ok), null)
                    builder.show()
                })
            }
        }

        fun showToast(activity: Activity?, msg: String?) {
            if (!isValidationEmpty(msg)) {
                if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
                }
            }
        }

        fun showAlertPermission(
            activity: Activity,
            deniedPermissionResponses: MutableList<PermissionDeniedResponse>
        ) {

            var permission = ""

            if (deniedPermissionResponses.isNotEmpty()) {
                for (i in deniedPermissionResponses) {
                    permission = if (isValidationEmpty(permission)) {
                        i.permissionName
                    } else {
                        permission + ", " + i.permissionName
                    }
                }
            }


            val builder = AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
            builder.setTitle(activity.getString(R.string.title_need_permission))
            builder.setMessage(
                activity.getString(
                    R.string.alert_msg_permission_from_settings,
                    permission
                )
            )
            builder.setPositiveButton(activity.getString(R.string.btn_setting)) { dialogInterface, i ->
                dialogInterface.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }

            builder.setNegativeButton(activity.getString(R.string.btn_cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
        }

        fun isNetworkAvailable(
            activity: Activity?,
            canShowErrorDialogOnFail: Boolean,
            isFinish: Boolean
        ): Boolean {
            var isNetAvailable = false
            if (activity != null) {
                val mConnectivityManager =
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var mobileNetwork = false
                var wifiNetwork = false
                var mobileNetworkConnected = false
                var wifiNetworkConnected = false
                val mobileInfo =
                    mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                val wifiInfo =
                    mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (mobileInfo != null) {
                    mobileNetwork = mobileInfo.isAvailable
                }
                if (wifiInfo != null) {
                    wifiNetwork = wifiInfo.isAvailable
                }
                if (wifiNetwork || mobileNetwork) {
                    if (mobileInfo != null) mobileNetworkConnected = mobileInfo
                        .isConnectedOrConnecting
                    assert(wifiInfo != null)
                    wifiNetworkConnected = wifiInfo!!.isConnectedOrConnecting
                }
                isNetAvailable = mobileNetworkConnected || wifiNetworkConnected
//                activity.setTheme(R.style.AppTheme)
                if (!isNetAvailable && canShowErrorDialogOnFail) {
                    activity.runOnUiThread(Runnable {
                        BaseActivity.hideProgressDialog()
                        showAlert(
                            activity,
                            activity.getString(R.string.error_code_internet_connection),
                            isFinish
                        )
                    })
                }
            }
            return isNetAvailable
        }
    }

}