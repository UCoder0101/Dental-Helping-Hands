package com.dentalhelpinghands.activities.office

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.ChangePasswordActivity
import com.dentalhelpinghands.common.*
import com.dentalhelpinghands.databinding.ActivityOfficeMyProfileBinding
import com.dentalhelpinghands.models.office.loginModel.DataX
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

class OfficeMyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityOfficeMyProfileBinding
    private var selectImageUri: Uri? = null
    private var selectImageFile: File? = null
    private val TAG = "My Profile Response"
    private var userProfile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityOfficeMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerNotificationPreference()
        getData()
    }

    private fun getData() {
        val responseObjectModel = Preference.getUserObject()
        if (responseObjectModel != null) {
            userProfile = responseObjectModel.profile

            Utils.setProfileImage(activity, userProfile, binding.ivProfile)

            binding.etUsername.setText(responseObjectModel.username)
            Logger.d(TAG, responseObjectModel.username)
            binding.etUsername.setSelection(binding.etUsername.length())

            binding.etWebAddress.setText(responseObjectModel.web_address)
            binding.etWebAddress.setSelection(binding.etWebAddress.length())

            binding.etEmailAddress.setText(responseObjectModel.email)
            binding.etEmailAddress.setSelection(binding.etEmailAddress.length())

            binding.etPhoneNumber.setText(responseObjectModel.contact)
            binding.etPhoneNumber.setSelection(binding.etPhoneNumber.length())

            binding.etContactName.setText(responseObjectModel.contact_name)
            binding.etContactName.setSelection(binding.etContactName.length())

            //binding.etNotificationPreference.setText(responseObjectModel.notification_preference)
            val spinnerNotificationPreference = resources.getStringArray(R.array.spinner_notification_preference)
            Logger.d("notification",  responseObjectModel.notification_preference)
            spinnerNotificationPreference.forEachIndexed { index, _ ->
                //Logger.d("notification",  responseObjectModel.notification_preference)
                //Logger.d("notification",  element)
                if (responseObjectModel.notification_preference == spinnerNotificationPreference[index]) {
                    binding.spNotificationPreference.setSelection(index)
                }
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

        binding.btnSave.setOnClickListener {
            validationMyProfile()
        }

        binding.rlProfile.setOnClickListener {
            hideKeyboard()
            permissionCheck()
        }

        binding.llChangePassword.setOnClickListener {
            hideKeyboard()
            startActivity(Intent(activity, ChangePasswordActivity::class.java))

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
            isShowCamera = true,
        )

        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            val selectedImages: Image = images[0]
            selectImageUri = selectedImages.uri

            if (selectImageUri != null) {
                val filePath: String? = PathUtil().getPath(activity, selectImageUri!!)

                if (filePath != null) {
                    selectImageFile = File(filePath)
                }
                Logger.d(TAG, filePath!!)
                Logger.d(TAG, selectImageFile!!.exists())


                Utils.setProfileImage(activity, filePath, binding.ivProfile)
            }
        }
    }

    private fun validationMyProfile() {
        val username = binding.etUsername.text.toString().trim()
        val emailAddress = binding.etEmailAddress.text.toString().trim()
        val webAddress = binding.etWebAddress.text.toString().trim()
        val contactName = binding.etContactName.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val notificationPreference = binding.etNotificationPreference.text.toString().trim()

        if (Utils.isValidationEmpty(userProfile) && (selectImageUri == null || selectImageFile == null || !selectImageFile!!.exists() || Utils.isValidationEmpty(
                selectImageFile!!.path
            ))
        ) {
            Utils.showAlert(activity, getString(R.string.valid_empty_select_profile_picture))
        } else if (Utils.isValidationEmpty(username)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_username))
        } else if (Utils.isValidationEmpty(emailAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_email_address))
        } else if (!Utils.isValidEmail(emailAddress)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if (Utils.isValidationEmpty(contactName)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_contact_name))
        } else if (Utils.isValidationEmpty(phoneNumber)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_phone_number))
        } else if (Utils.isValidationEmpty(notificationPreference) || notificationPreference == getString(
                R.string.select_notification_preference
            )
        ) {
            Utils.showAlert(activity, getString(R.string.select_notification_preference))
        } else {
            callUpdateProfileApi(
                username,
                webAddress,
                emailAddress,
                contactName,
                phoneNumber,
                notificationPreference
            )
        }
    }

    private fun callUpdateProfileApi(
        userName: String,
        webAddress: String,
        email: String,
        contactName: String,
        phoneNumber: String,
        notificationPreference: String
    ) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            var propertyImagePart: MultipartBody.Part? = null

            if (selectImageFile != null && selectImageFile!!.exists()) {

                val requestFile: RequestBody = RequestBody.create(
                    MediaType.parse(contentResolver.getType(selectImageUri!!)),
                    selectImageFile!!
                )

                propertyImagePart =
                    MultipartBody.Part.createFormData(
                        "profile",
                        selectImageFile!!.name,
                        requestFile
                    )

            }

            val call: Call<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel> =
                Constants.retrofitBuilder.callDentalUpdateProfileApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Utils.convertValueToRequestBody(Preference.getStringName(Preference.USER_ID))!!,
                    propertyImagePart,
                    Utils.convertValueToRequestBody(webAddress)!!,
                    Utils.convertValueToRequestBody(contactName)!!,
                    Utils.convertValueToRequestBody(phoneNumber)!!,
                    Utils.convertValueToRequestBody(email)!!,
                    Utils.convertValueToRequestBody(notificationPreference)!!,
                    Utils.convertValueToRequestBody(userName)!!,
                    Utils.convertValueToRequestBody(Constants.device_type)!!,
                    Utils.convertValueToRequestBody(Constants.device_token)!!
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

                                Utils.showToast(activity, responseBody.ResponseMsg)
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

    /*private fun getData() {
        val responseObjectModel = Preference.getUserObject()
        if (responseObjectModel != null) {
            *//*userProfile = responseObjectModel.profile

            Utils.setProfileImage(activity, userProfile, binding.ivProfile)

            binding.etUsername.setText(responseObjectModel.username)
            binding.etUsername.setSelection(binding.etUsername.length())

            binding.etWebAddress.setText(responseObjectModel.web_address)
            binding.etWebAddress.setSelection(binding.etWebAddress.length())

            binding.etEmailAddress.setText(responseObjectModel.email)
            binding.etEmailAddress.setSelection(binding.etEmailAddress.length())

            binding.etPhoneNumber.setText(responseObjectModel.contact)
            binding.etPhoneNumber.setSelection(binding.etPhoneNumber.length())

            binding.etContactName.setText(responseObjectModel.contact_name)
            binding.etContactName.setSelection(binding.etContactName.length())*//*

            //binding.etNotificationPreference.setText(responseObjectModel.notification_preference)
            val spinnerNotificationPreference = resources.getStringArray(R.array.spinner_notification_preference)
            spinnerNotificationPreference.forEachIndexed { index, element ->
                Logger.d("notification",  responseObjectModel.notification_preference)
                Logger.d("notification",  index)
                Logger.d("notification",  element)
                if (responseObjectModel.notification_preference == spinnerNotificationPreference[index]) {
                    binding.spNotificationPreference.setSelection(index)
                }
            }
        }
    }*/

    private fun initView() {
        binding.etUsername.requestFocus()



    }


    private fun initToolBar() {
        binding.ilToolBar.tvHeaderTitle.text =
            activity.getText(R.string.btn_my_profile)

        binding.ilToolBar.ivHeaderBack.visibility = View.VISIBLE
        binding.ilToolBar.ivHeaderBack.setOnClickListener {
            onBackPressed()
        }

        binding.ilToolBar.ivHeaderOther.visibility = View.INVISIBLE
    }
}