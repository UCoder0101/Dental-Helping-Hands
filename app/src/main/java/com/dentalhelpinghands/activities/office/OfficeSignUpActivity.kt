package com.dentalhelpinghands.activities.office

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.LinkActivity
import com.dentalhelpinghands.common.*
import com.dentalhelpinghands.databinding.ActivityOfficeSignUpBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class OfficeSignUpActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficeSignUpBinding
//    private var selectedImages: Image? = null
    private var selectedImageUri: Uri? = null
    private var selectedImageFile: File? = null
    private var TAG = "signup user response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityOfficeSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerTypeOfPractice()
        processSpinnerNotificationPreference()
    }

    private fun processSpinnerTypeOfPractice() {
        binding.etTypeOfPractice.setOnClickListener {
            binding.spTypeOfPractice.performClick()
        }


        binding.llTypeOfPractice.setOnClickListener {
            binding.spTypeOfPractice.performClick()
        }

        val spinnerTypeOfPractice = resources.getStringArray(R.array.spinner_type_of_practice)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerTypeOfPractice
        )
        binding.spTypeOfPractice.adapter = adapter

        binding.spTypeOfPractice.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etTypeOfPractice.setText(spinnerTypeOfPractice[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun processSpinnerNotificationPreference() {
        binding.etNotificationPreference.setOnClickListener {
            binding.spNotificationPreference.performClick()
        }


        binding.llNotificationPreference.setOnClickListener {
            binding.spNotificationPreference.performClick()
        }

        val spinnerNotificationPreference =
            resources.getStringArray(R.array.spinner_notification_preference)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerNotificationPreference
        )
        binding.spNotificationPreference.adapter = adapter

        binding.spNotificationPreference.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etNotificationPreference.setText(spinnerNotificationPreference[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initClick() {

        binding.btnSignUp.setOnClickListener {
            hideKeyboard()
            validationSignUp()
        }

        binding.tvSignIn.setOnClickListener {
            onBackPressed()
        }

        binding.rlProfile.setOnClickListener {
            hideKeyboard()
            permissionCheck()
        }

    }

    private fun permissionCheck() {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        openImagePicker()
                    } else if (report.isAnyPermissionPermanentlyDenied) {   // check for permanent denial of any permission
                        // permission is denied permanently, navigate user to app settings
                        Utils.showAlertPermission(activity, report.deniedPermissionResponses)
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    private fun openImagePicker() {
        val config = ImagePickerConfig(
            statusBarColor = "#000000",
            isLightStatusBar = false,
            isFolderMode = true,
            isMultipleMode = false,
            subDirectory = getString(R.string.app_name),
            folderGridCount = GridCount(2, 4),
            imageGridCount = GridCount(3, 5),
            isShowCamera = true
        )

        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            val selectedImages:Image = images[0]
            selectedImageUri = selectedImages.uri

            if (selectedImageUri != null) {
                val filePath: String? = PathUtil().getPath(activity, selectedImageUri!!)

                if (filePath != null) {
                    selectedImageFile = File(filePath)
                }
                Logger.d(TAG, filePath!!)
                Logger.d(TAG, selectedImageFile!!.exists())


                Glide.with(activity)
                    .load(selectedImageUri)
                    .into(binding.ivProfile)
            }
        }
    }

    private fun initView() {
        binding.etPracticeName.requestFocus()

        if (Constants.IS_TESTING_MODE) {
            binding.etPracticeName.setText("Dhruv Kikani")
            binding.etContactName.setText("Dhruv")
            binding.etPhoneNumber.setText("9586667303")
            binding.etPracticeAddress.setText("Mota varachha, surat , ")
            binding.etEmailAddress.setText("dhruv.kmphitech@gmail.com")
            binding.etUsername.setText("Dhruv01")
            binding.etPassword.setText("123456")
            binding.etConfirmPassword.setText("123456")
            binding.cbAgreeTCAndPP.isChecked = true
        }

        val agreement = """
            ${
            activity.resources.getString(R.string.paragraph_1)
        }
            <font color=${activity.resources.getColor(R.color.colorTextCyanBlue)}><a href="${
            activity.resources.getString(
                R.string.link_1
            )
        }:"><b>${activity.resources.getString(R.string.hint_link_term_service)}</b></a></font>
            <font>${
            activity.resources.getString(
                R.string.paragraph_2
            )
        }</font>
            <font color=${activity.resources.getColor(R.color.colorTextCyanBlue)}><a href="${
            activity.resources.getString(
                R.string.link_2
            )
        }:"><b>${activity.resources.getString(R.string.hint_link_privacy_policy)}</b></a></font>
            <font>${
            activity.resources.getString(
                R.string.paragraph_3
            )
        }
            """.trimIndent()

        binding.tvAgreeTCAndPP.text = Html.fromHtml(agreement)
        binding.tvAgreeTCAndPP.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun startActivity(intent: Intent?) {
        if (intent != null && intent.scheme != null) {
            var isFrom = ""
            if (intent.scheme == activity.resources.getString(R.string.link_1)) {
                isFrom = Constants.IS_FROM_TERM_AND_CONDITIONS
            } else if (intent.scheme == activity.resources.getString(R.string.link_2)) {
                isFrom = Constants.IS_FROM_PRIVACY_POLICY
            }
            if (!Utils.isValidationEmpty(isFrom) && Utils.isNetworkAvailable(
                    activity,
                    true,
                    false
                )
            ) {
                hideKeyboard()
                startActivity(
                    Intent(
                        activity,
                        LinkActivity::class.java
                    ).putExtra(Constants.INTENT_IS_FROM, isFrom)
                )
            } else {
                super.startActivity(intent)
            }
        } else {
            super.startActivity(intent)
        }
    }

    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.title_dental_office_sign_up)

        binding.ilToolBar.ivHeaderBack.visibility = View.GONE

        binding.ilToolBar.ivHeaderOther.visibility = View.GONE
    }

    private fun validationSignUp() {
        val practiceName = binding.etPracticeName.text.toString().trim()
        val typeOfPractice = binding.etTypeOfPractice.text.toString().trim()
        val contactName = binding.etContactName.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val practiceAddress = binding.etPracticeAddress.text.toString().trim()
        val emailAddress = binding.etEmailAddress.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val notificationPreference = binding.etNotificationPreference.text.toString().trim()
        val isAgreeTCAndPP = binding.cbAgreeTCAndPP.isChecked
        val webAddress = binding.etWebAddress.text.toString().trim()
        val referBy = binding.etReferredBy.text.toString().trim()

        if ( selectedImageUri == null || selectedImageFile == null || !selectedImageFile!!.exists() || Utils.isValidationEmpty(
                selectedImageFile!!.path
            )
        ) {
            Utils.showAlert(activity, getString(R.string.valid_empty_select_profile_picture))
        } else if (Utils.isValidationEmpty(practiceName)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_practice_name))
        } else if (Utils.isValidationEmpty(typeOfPractice) || typeOfPractice == getString(R.string.select_type_of_practice)) {
            Utils.showAlert(activity, getString(R.string.select_type_of_practice))
        } else if (Utils.isValidationEmpty(contactName)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_contact_name))
        } else if (Utils.isValidationEmpty(phoneNumber)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_phone_number))
        } else if (Utils.isValidationEmpty(practiceAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_practice_address))
        } else if (Utils.isValidationEmpty(emailAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_email_address))
        } else if (!Utils.isValidEmail(emailAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if (Utils.isValidationEmpty(username)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_username))
        } else if (Utils.isValidationEmpty(password)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_password))
        } else if (!Utils.isValidPassword(
                password,
                resources.getInteger(R.integer.min_length_password)
            )
        ) {
            Utils.showAlert(
                activity, getString(
                    R.string.valid_strong_password,
                    resources.getInteger(R.integer.min_length_password)
                )
            )
        } else if (Utils.isValidationEmpty(confirmPassword)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_confirm_password))
        } else if (!Utils.isPassMatch(password, confirmPassword)) {
            Utils.showAlert(
                activity,
                getString(R.string.valid_password_and_confirm_password_not_match)
            )
        } else if (Utils.isValidationEmpty(notificationPreference) || notificationPreference == getString(
                R.string.select_notification_preference
            )
        ) {
            Utils.showAlert(activity, getString(R.string.select_notification_preference))
        } else if (!isAgreeTCAndPP) {
            Utils.showAlert(activity, getString(R.string.valid_privacy_policy))
        } else {
            /*Logger.d(TAG, "practiceName : $practiceName")
            Logger.d(TAG, "typeOfPractice : $typeOfPractice")
            Logger.d(TAG, "webAddress : $webAddress")
            Logger.d(TAG, "contactName : $contactName")
            Logger.d(TAG, "phoneNumber : $phoneNumber")
            Logger.d(TAG, "practiceAddress : $practiceAddress")
            Logger.d(TAG, "emailAddress : $emailAddress")
            Logger.d(TAG, "username : $username")
            Logger.d(TAG, "password : $password")
            Logger.d(TAG, "referBy : $referBy")
            Logger.d(TAG, "notificationPreference : $notificationPreference")
            Logger.d(TAG, "device_token : $Constants.device_token")
            Logger.d(TAG, "device_type : $Constants.device_type")*/
            /*Logger.d(TAG, typeOfPractice)
            Logger.d(TAG, webAddress)
            Logger.d(TAG, contactName)
            Logger.d(TAG, phoneNumber)
            Logger.d(TAG, practiceAddress)
            Logger.d(TAG, emailAddress)
            Logger.d(TAG, username)
            Logger.d(TAG, password)
            Logger.d(TAG, referBy)
            Logger.d(TAG, notificationPreference)
            Logger.d(TAG, Constants.device_token)
            Logger.d(TAG, Constants.device_type)*/

            callSignUpApi(
                practiceName,
                typeOfPractice,
                webAddress,
                contactName,
                phoneNumber,
                practiceAddress,
                emailAddress,
                username,
                password,
                referBy,
                notificationPreference, Constants.device_token, Constants.device_type
            )
        }
    }

    private fun callSignUpApi(
        practiceName: String,
        practiceType: String,
        webAddress: String,
        contactName: String,
        phoneNumber: String,
        practiceAddress: String,
        email: String,
        userName: String,
        password: String,
        referBy: String,
        notificationPreference: String,
        deviceToken: String,
        deviceType: String
    ) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            var propertyImagePart: MultipartBody.Part? = null

//            val file = File(profile.path)
//            Logger.d(TAG, file.path)
//            Logger.d(TAG, file.exists())

            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse(contentResolver.getType(selectedImageUri!!)),
                selectedImageFile!!
            )

            if (selectedImageFile != null && selectedImageFile!!.exists()) {
                propertyImagePart =
                    MultipartBody.Part.createFormData(
                        "profile",
                        selectedImageFile!!.name,
                        requestFile
                    )

            }
            val dataPassword = Utils.getMD5(password)
            val call: Call<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel> =
                Constants.retrofitBuilder.callSignUpApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    propertyImagePart,
                    Utils.convertValueToRequestBody(Constants.USER_TYPE_DENTAL.toString())!!,
                    Utils.convertValueToRequestBody(practiceName)!!,
                    Utils.convertValueToRequestBody(practiceType)!!,
                    Utils.convertValueToRequestBody(webAddress)!!,
                    Utils.convertValueToRequestBody(contactName)!!,
                    Utils.convertValueToRequestBody(phoneNumber)!!,
                    Utils.convertValueToRequestBody(practiceAddress)!!,
                    Utils.convertValueToRequestBody(email)!!,
                    Utils.convertValueToRequestBody(userName)!!,
                    Utils.convertValueToRequestBody(dataPassword)!!,
                    Utils.convertValueToRequestBody(referBy)!!,
                    Utils.convertValueToRequestBody(notificationPreference)!!,
                    Utils.convertValueToRequestBody(deviceToken)!!,
                    Utils.convertValueToRequestBody(deviceType)!!
                )

            call.enqueue(object :
                Callback<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel> {
                override fun onResponse(
                    call: Call<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel>,
                    response: Response<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        when (responseBody.ResponseCode) {
                            1 -> {

                                val data = responseBody.data
                                //Logger.d(TAG,data)
                                Preference.setUserObject(data)


                                Preference.SetStringName(
                                    Preference.USER_ID,
                                    data.user_id
                                )

                                Preference.SetStringName(
                                    Preference.LOGIN_TOKEN,
                                    data.token
                                )

                                Preference.SetStringName(
                                    Preference.USER_TYPE,
                                    data.user_type
                                )

                                Utils.showAlert(activity, responseBody.ResponseMsg, DialogInterface.OnClickListener { _, _ ->
                                    activity.startActivity(
                                        Intent(activity, OfficeHomeActivity::class.java)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    )
                                    activity.finish()
                                })


                            }
                            2 -> {
                                Utils.showAlert(activity, responseBody.ResponseMsg, DialogInterface.OnClickListener { _, _ ->

                                    // TODO: https://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android
                                    val intent = Intent()
                                    intent.putExtra("email", email)
                                    setResult(RESULT_OK, intent)
                                    finish()
                                })

                            }
                            else -> {
                                Utils.showAlert(activity, responseBody.ResponseMsg)
                            }
                        }
                    } else {
                        Utils.showAlertsWithTitle(
                            activity,
                            getString(R.string.app_name),
                            getString(R.string.something_went_wrong)
                        )
                    }
                }

                override fun onFailure(
                    call: Call<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel?>,
                    t: Throwable
                ) {
                    hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
        }
    }
}