package com.dentalhelpinghands.activities.professional

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.LinkActivity
import com.dentalhelpinghands.activities.office.OfficeHomeActivity
import com.dentalhelpinghands.activities.office.OfficePostJobActivity
import com.dentalhelpinghands.activities.office.OfficeSignUpActivity
import com.dentalhelpinghands.adapters.professional.ProfessionalAvailabilityAdapter
import com.dentalhelpinghands.common.*
import com.dentalhelpinghands.databinding.ActivityProfessionalSignUpBinding
import com.dentalhelpinghands.databinding.ContentAvailabilityBinding
import com.dentalhelpinghands.databinding.DialogTimePickerBinding
import com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel
import com.dentalhelpinghands.models.professional.ProfessionalAvailabilityModel
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


class ProfessionalSignUpActivity : BaseActivity() {
    private lateinit var binding: ActivityProfessionalSignUpBinding
    private val lstAvailability: ArrayList<ProfessionalAvailabilityModel> = ArrayList()
    private lateinit var opensTime: String
    private lateinit var closesTime: String
    private var selectedImageFile: File? = null
    private var selectedImageUri: Uri? = null
    private val TAG = "Professional user signup"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.enter, R.anim.exit)
        binding = ActivityProfessionalSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerProfessional()
        processSpinnerRequestedPayRate()
        processSpinnerNotificationPreference()
        processSpinnerAvailability()
    }

    private fun processSpinnerProfessional() {
        binding.etProfessional.setOnClickListener {
            binding.spProfessional.performClick()
        }

        binding.llProfessional.setOnClickListener {
            binding.spProfessional.performClick()
        }

        val spinnerProfessional = resources.getStringArray(R.array.spinner_professional)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerProfessional
        )
        binding.spProfessional.adapter = adapter

        binding.spProfessional.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etProfessional.setText(spinnerProfessional[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun processSpinnerRequestedPayRate() {
        binding.etRequestedPayRate.setOnClickListener {
            binding.spRequestedPayRate.performClick()
        }


        binding.llRequestedPayRate.setOnClickListener {
            binding.spRequestedPayRate.performClick()
        }

        val spinnerRequestedPayRate = resources.getStringArray(R.array.spinner_requested_pay_rate)

        val adapter = ArrayAdapter(
            activity,
            R.layout.row_spinner_item, spinnerRequestedPayRate
        )
        binding.spRequestedPayRate.adapter = adapter

        binding.spRequestedPayRate.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.etRequestedPayRate.setText(spinnerRequestedPayRate[position])
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

    private fun popupWindowAvailability(view: View) {

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val bindingContentAvailability = ContentAvailabilityBinding.inflate(inflater)
        val popupView: View = bindingContentAvailability.root

        bindingContentAvailability.rvAvailability.layoutManager = LinearLayoutManager(
            activity
        )
        val mAdapter = ProfessionalAvailabilityAdapter(activity, lstAvailability)
        bindingContentAvailability.rvAvailability.adapter = mAdapter

        ProfessionalAvailabilityAdapter.onItemClickListener =
            object : ProfessionalAvailabilityAdapter.OnItemClickListener {
                override fun itemClick(position: Int) {
                    if (lstAvailability.size > 0 && lstAvailability.size > position) {
                        val model: ProfessionalAvailabilityModel = lstAvailability[position]
                        model.isSelected = !model.isSelected
                        lstAvailability[position] = model
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }


        // create the popup window
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAsDropDown(view)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }


    private fun processSpinnerAvailability() {
        binding.etAvailability.setOnClickListener {
            popupWindowAvailability(binding.etAvailability)
        }

        binding.llAvailability.setOnClickListener {
            popupWindowAvailability(binding.llAvailability)
        }

        val spinnerAvailability = resources.getStringArray(R.array.spinner_availability)

        lstAvailability.clear()


        for (name in spinnerAvailability) {
            lstAvailability.add(ProfessionalAvailabilityModel(name, false))
        }
    }

    private fun initClick() {

        binding.btnSignUp.setOnClickListener {
            hideKeyboard()
            validationProfessionalSignUp()
        }

        binding.tvSignIn.setOnClickListener {
            onBackPressed()
        }

        binding.rlProfile.setOnClickListener {
            hideKeyboard()
            permissionCheck()
        }

        binding.llTimeStart.setOnClickListener {
            openDialogTimePickerOpens()
        }

        binding.llTimeEnd.setOnClickListener {
            openDialogTimePickerCloses()
        }

    }

    private fun openDialogTimePickerOpens() {
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
        val bindingDialogTimePicker = DialogTimePickerBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogTimePicker.root)
        dialog.setTitle("")


        bindingDialogTimePicker.timePicker.setIs24HourView(false)  // used to display AM/PM mode

        val opensTime24 = Utils.customDateTimeFormat(
            opensTime,
            Constants.DATE_FORMAT_COLUMN_HH_MM_AA,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24
        ).toString()

        val opensTimeHours = Utils.customDateTimeFormat(
            opensTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_HH
        ).toString()

        val opensTimeMinutes = Utils.customDateTimeFormat(
            opensTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_MM
        ).toString()

        // set the value for current hours
        bindingDialogTimePicker.timePicker.currentHour = opensTimeHours.toInt()
        bindingDialogTimePicker.timePicker.currentMinute = opensTimeMinutes.toInt()

        bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
            view: TimePicker,
            hourOfDay: Int,
            minute: Int
        ) {
            val selectedDate = "${hourOfDay}:${minute}"

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

        })

        dialog.show()
    }

    private fun openDialogTimePickerCloses() {
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
        val bindingDialogTimePicker = DialogTimePickerBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogTimePicker.root)
        dialog.setTitle("")


        bindingDialogTimePicker.timePicker.setIs24HourView(false)  // used to display AM/PM mode

        val closesTime24 = Utils.customDateTimeFormat(
            closesTime,
            Constants.DATE_FORMAT_COLUMN_HH_MM_AA,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24
        ).toString()

        val closesTimeHours = Utils.customDateTimeFormat(
            closesTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_HH
        ).toString()

        val closesTimeMinutes = Utils.customDateTimeFormat(
            closesTime24,
            Constants.DATE_FORMAT_COLUMN_HH_MM_24,
            Constants.DATE_FORMAT_COLUMN_MM
        ).toString()

        // set the value for current hours
        bindingDialogTimePicker.timePicker.currentHour = closesTimeHours.toInt()
        bindingDialogTimePicker.timePicker.currentMinute = closesTimeMinutes.toInt()

        bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
            view: TimePicker,
            hourOfDay: Int,
            minute: Int
        ) {
            val selectedDate = "${hourOfDay}:${minute}"

            closesTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()
            //binding.etTimeStart.text = closesTime

        })

        dialog.show()

    }

    private fun validationProfessionalSignUp() {
        val name = binding.etName.text.toString().trim()
        val professional = binding.etProfessional.text.toString().trim()
        val scLicenceNumber = binding.etSCLicenseNumber.text.toString().trim()
        val gaLicenceNumber = binding.etGALicenseNumber.text.toString().trim()
        val flLicenceNumber = binding.etFLLicenseNumber.text.toString().trim()
        val ncLicenceNumber = binding.etNCLicenseNumber.text.toString().trim()
        val otherLicenceNumber = binding.etOtherLicenseNumber.text.toString().trim()
        val yearParticipating = binding.etYearsPracticing.text.toString().trim()
        //val availability = binding.etAvailability.text.toString().trim()
        val startTime = binding.etTimeStart.text.toString().trim()
        val endTime = binding.etTimeEnd.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val email = binding.etEmailAddress.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val referBy = binding.etReferredBy.text.toString().trim()
        val isAgreeTCAndPP = binding.cbAgreeTCAndPP.isChecked
        val notificationPreference = binding.etNotificationPreference.text.toString().trim()
        val requestPayRate = binding.etRequestedPayRate.text.toString().trim()


        var availabilityDayName = ""

        for (availabilityDay in lstAvailability) {
            if (availabilityDay.isSelected) {
                if (Utils.isValidationEmpty(availabilityDayName)) {
                    availabilityDayName = availabilityDay.name
                } else {
                    availabilityDayName = availabilityDayName + "," + availabilityDay.name
                }
            }
        }
        Logger.d(TAG, availabilityDayName)


        if (selectedImageUri == null || selectedImageFile == null || !selectedImageFile!!.exists() || Utils.isValidationEmpty(
                selectedImageFile!!.path
            )
        ) {
            Utils.showAlert(activity, getString(R.string.valid_empty_select_profile_picture))
        } else if (Utils.isValidationEmpty(name)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_name))
        } else if (Utils.isValidationEmpty(professional) || professional == getString(R.string.select_professional)) {
            Utils.showAlert(activity, getString(R.string.select_professional))
        } else if (binding.cbSC.isChecked && Utils.isValidationEmpty(scLicenceNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbGA.isChecked && Utils.isValidationEmpty(gaLicenceNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbFL.isChecked && Utils.isValidationEmpty(flLicenceNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbNC.isChecked && Utils.isValidationEmpty(ncLicenceNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbOther.isChecked && Utils.isValidationEmpty(otherLicenceNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (Utils.isValidationEmpty(yearParticipating)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_years_practicing))
        } else if (Utils.isValidationEmpty(availabilityDayName) || availabilityDayName == getString(
                R.string.select_availability
            )
        ) {
            Utils.showAlert(activity, getString(R.string.select_availability))
        } else if (Utils.isValidationEmpty(startTime) || startTime == getString(R.string.et_time_start_)) {
            Utils.showAlert(activity, getString(R.string.et_time_start_))
        } else if (Utils.isValidationEmpty(endTime) || endTime == getString(R.string.et_time_end_)) {
            Utils.showAlert(activity, getString(R.string.et_time_end_))
        } else if (Utils.isValidationEmpty(phoneNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_phone_number))
        } else if (Utils.isValidationEmpty(email)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_email_address))
        } else if (!Utils.isValidEmail(email)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if (Utils.isValidationEmpty(username)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_username))
        } else if (Utils.isValidationEmpty(password)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_password))
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
        } else if (Utils.isValidationEmpty(requestPayRate) || requestPayRate == getString(R.string.select_requested_pay_rate)
        ) {
            Utils.showAlert(activity, getString(R.string.select_requested_pay_rate))
        } else if (!isAgreeTCAndPP) {
            Utils.showAlert(activity, getString(R.string.valid_privacy_policy))
        } else {
            callProfessionalSignUpApi(
                RetrofitBuilder.HEADER_KEY_VALUE,
                selectedImageFile!!,
                name,
                professional,
                scLicenceNumber,
                gaLicenceNumber,
                flLicenceNumber,
                ncLicenceNumber,
                otherLicenceNumber,
                yearParticipating,
                availabilityDayName,
                startTime,
                endTime,
                phoneNumber,
                email,
                username,
                password,
                referBy,
                notificationPreference,
                requestPayRate,
                Constants.device_type,
                Constants.device_token
            )
        }
    }

    private fun callProfessionalSignUpApi(
        key: String,
        profile: File,
        name: String,
        professional: String,
        scLicenceNumber: String,
        gaLicenceNumber: String,
        flLicenceNumber: String,
        ncLicenceNumber: String,
        otherLicenceNumber: String,
        yearPractice: String,
        availability: String,
        startTime: String,
        endtime: String,
        mobile: String,
        email: String,
        username: String,
        password: String,
        referBy: String,
        preference: String,
        payRate: String,
        deviceType: String,
        deviceToken: String
    ) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity, getString(R.string.please_wait))

            var propertyImagePart: MultipartBody.Part? = null

            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse(contentResolver.getType(selectedImageUri!!)),
                selectedImageFile!!
            )

            if (selectedImageFile != null && selectedImageFile!!.exists()) {
                propertyImagePart = MultipartBody.Part.createFormData(
                    "profile",
                    selectedImageFile!!.name,
                    requestFile
                )

            }

            val dataPassword = Utils.getMD5(password)
            val call: Call<ResponseObjectModel> =
                Constants.retrofitBuilder.callProfessionalSignUpApi(
                    key, propertyImagePart,
                    Utils.convertValueToRequestBody(name)!!,
                    Utils.convertValueToRequestBody(professional)!!,
                    Utils.convertValueToRequestBody(scLicenceNumber)!!,
                    Utils.convertValueToRequestBody(gaLicenceNumber)!!,
                    Utils.convertValueToRequestBody(flLicenceNumber)!!,
                    Utils.convertValueToRequestBody(ncLicenceNumber)!!,
                    Utils.convertValueToRequestBody(otherLicenceNumber)!!,
                    Utils.convertValueToRequestBody(yearPractice)!!,
                    Utils.convertValueToRequestBody(availability)!!,
                    Utils.convertValueToRequestBody(startTime)!!,
                    Utils.convertValueToRequestBody(endtime)!!,
                    Utils.convertValueToRequestBody(mobile)!!,
                    Utils.convertValueToRequestBody(email)!!,
                    Utils.convertValueToRequestBody(Constants.USER_TYPE_PROFESSIONAL.toString())!!,
                    Utils.convertValueToRequestBody(username)!!,
                    Utils.convertValueToRequestBody(dataPassword)!!,
                    Utils.convertValueToRequestBody(referBy)!!,
                    Utils.convertValueToRequestBody(preference)!!,
                    Utils.convertValueToRequestBody(payRate)!!,
                    Utils.convertValueToRequestBody(deviceType)!!,
                    Utils.convertValueToRequestBody(deviceToken)!!
                )

            call.enqueue(object :
                Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<ResponseObjectModel>,
                    response: Response<ResponseObjectModel>
                ) {
                    hideProgressDialog()

                    Logger.d(TAG, response.body()!!)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        if (responseBody.ResponseCode == 1) {

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
                                    Intent(activity, ProfessionalHomeActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                activity.finish()
                            })
                        } else if (responseBody.ResponseCode == 2) {
                            Utils.showAlert(
                                activity,
                                responseBody.ResponseMsg,
                                DialogInterface.OnClickListener { _, _ ->
                                    val intent = Intent()
                                    intent.putExtra("email", email)
                                    setResult(RESULT_OK, intent)
                                    finish()
                                })
                        } else {
                            Utils.showAlert(activity, responseBody.ResponseMsg)
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
                    call: Call<ResponseObjectModel?>,
                    t: Throwable
                ) {
                    hideProgressDialog()
                    Utils.handleErrorCode(activity, t)
                }
            })
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
        binding.etName.requestFocus()

        if (Constants.IS_TESTING_MODE) {
            binding.etName.setText("Karthik")
            binding.etProfessional.setText("General")
            binding.etSCLicenseNumber.setText("2516461")
            binding.etGALicenseNumber.setText("2516461")
            binding.etGALicenseNumber.setText("2516461")
            binding.etYearsPracticing.setText("2020")
            binding.etPhoneNumber.setText("9621457896")
            binding.etEmailAddress.setText("karthik.kmphitech@gmail.com")
            binding.etUsername.setText("karthik01")
            binding.etPassword.setText("12345678")
            binding.etConfirmPassword.setText("12345678")
            binding.etConfirmPassword.setText("12345678")
        }

        //initialize lunch time open or close
        opensTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        //binding.etTimeStart.text = opensTime

        closesTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
        //binding.etTimeEnd.text = closesTime

        binding.cbSC.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llSC.visibility = View.VISIBLE
            } else {
                binding.llSC.visibility = View.GONE
            }
            processNextFocusForward()
        }

        binding.cbGA.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llGA.visibility = View.VISIBLE
            } else {
                binding.llGA.visibility = View.GONE
            }
            processNextFocusForward()
        }

        binding.cbFL.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llFL.visibility = View.VISIBLE
            } else {
                binding.llFL.visibility = View.GONE
            }
            processNextFocusForward()
        }

        binding.cbNC.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llNC.visibility = View.VISIBLE
            } else {
                binding.llNC.visibility = View.GONE
            }
            processNextFocusForward()
        }

        binding.cbOther.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.llOther.visibility = View.VISIBLE
            } else {
                binding.llOther.visibility = View.GONE
            }
            processNextFocusForward()
        }

        binding.cbSC.isChecked = true
        binding.llSC.visibility = View.VISIBLE

        binding.cbGA.isChecked = true
        binding.llGA.visibility = View.VISIBLE

        binding.cbFL.isChecked = false
        binding.llFL.visibility = View.GONE

        binding.cbNC.isChecked = false
        binding.llNC.visibility = View.GONE

        binding.cbOther.isChecked = false
        binding.llOther.visibility = View.GONE

        processNextFocusForward()

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
            activity.getText(R.string.title_professional_sign_up)

        binding.ilToolBar.ivHeaderBack.visibility = View.GONE

        binding.ilToolBar.ivHeaderOther.visibility = View.GONE
    }

    private fun processNextFocusForward() {
        if (binding.cbSC.isChecked) {
            binding.etName.nextFocusForwardId = R.id.etSCLicenseNumber
        } else if (binding.cbGA.isChecked) {
            binding.etName.nextFocusForwardId = R.id.etGALicenseNumber
        } else if (binding.cbFL.isChecked) {
            binding.etName.nextFocusForwardId = R.id.etFLLicenseNumber
        } else if (binding.cbNC.isChecked) {
            binding.etName.nextFocusForwardId = R.id.etNCLicenseNumber
        } else if (binding.cbOther.isChecked) {
            binding.etName.nextFocusForwardId = R.id.etOtherLicenseNumber
        } else {
            binding.etName.nextFocusForwardId = R.id.etYearsPracticing
        }

        //         etSCLicenseNumber.nextFocusForwardId
        if (binding.cbGA.isChecked) {
            binding.etSCLicenseNumber.nextFocusForwardId = R.id.etGALicenseNumber
        } else if (binding.cbFL.isChecked) {
            binding.etSCLicenseNumber.nextFocusForwardId = R.id.etFLLicenseNumber
        } else if (binding.cbNC.isChecked) {
            binding.etSCLicenseNumber.nextFocusForwardId = R.id.etNCLicenseNumber
        } else if (binding.cbOther.isChecked) {
            binding.etSCLicenseNumber.nextFocusForwardId = R.id.etOtherLicenseNumber
        } else {
            binding.etSCLicenseNumber.nextFocusForwardId = R.id.etYearsPracticing
        }

        //         etGALicenseNumber.nextFocusForwardId
        if (binding.cbFL.isChecked) {
            binding.etGALicenseNumber.nextFocusForwardId = R.id.etFLLicenseNumber
        } else if (binding.cbNC.isChecked) {
            binding.etGALicenseNumber.nextFocusForwardId = R.id.etNCLicenseNumber
        } else if (binding.cbOther.isChecked) {
            binding.etGALicenseNumber.nextFocusForwardId = R.id.etOtherLicenseNumber
        } else {
            binding.etGALicenseNumber.nextFocusForwardId = R.id.etYearsPracticing
        }

        //          etFLLicenseNumber.nextFocusForwardId
        if (binding.cbNC.isChecked) {
            binding.etFLLicenseNumber.nextFocusForwardId = R.id.etNCLicenseNumber
        } else if (binding.cbOther.isChecked) {
            binding.etFLLicenseNumber.nextFocusForwardId = R.id.etOtherLicenseNumber
        } else {
            binding.etFLLicenseNumber.nextFocusForwardId = R.id.etYearsPracticing
        }


        //          etNCLicenseNumber.nextFocusForwardId
        if (binding.cbOther.isChecked) {
            binding.etNCLicenseNumber.nextFocusForwardId = R.id.etOtherLicenseNumber
        } else {
            binding.etNCLicenseNumber.nextFocusForwardId = R.id.etYearsPracticing
        }
    }
}