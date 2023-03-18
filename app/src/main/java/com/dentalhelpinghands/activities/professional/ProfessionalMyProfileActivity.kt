package com.dentalhelpinghands.activities.professional

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentalhelpinghands.Api.RetrofitBuilder
import com.dentalhelpinghands.R
import com.dentalhelpinghands.activities.BaseActivity
import com.dentalhelpinghands.activities.ChangePasswordActivity
import com.dentalhelpinghands.adapters.professional.ProfessionalAvailabilityAdapter
import com.dentalhelpinghands.common.*
import com.dentalhelpinghands.databinding.ActivityProfessionalMyProfileBinding
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

class ProfessionalMyProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfessionalMyProfileBinding
    private val lstAvailability: ArrayList<ProfessionalAvailabilityModel> = ArrayList()
    private var selectImageUri: Uri? = null
    private var selectImageFile: File? = null
    private var opensTime: String? = null
    private var closesTime: String? = null
    private var mAdapter: ProfessionalAvailabilityAdapter? = null

    //    private var getSelectDate = ""
    private val TAG = "Professional My profile response"
    private var userProfile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        binding = ActivityProfessionalMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        initToolBar()
        initView()
        initClick()
        processSpinnerNotificationPreference()
        processSpinnerAvailability()
        getDataList()
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
        val binding = ContentAvailabilityBinding.inflate(inflater)
        val popupView: View = binding.root



        binding.rvAvailability.layoutManager = LinearLayoutManager(
            activity
        )
        mAdapter = ProfessionalAvailabilityAdapter(activity, lstAvailability)
        binding.rvAvailability.adapter = mAdapter

        ProfessionalAvailabilityAdapter.onItemClickListener =
            object : ProfessionalAvailabilityAdapter.OnItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun itemClick(position: Int) {
                    if (lstAvailability.size > 0 && lstAvailability.size > position) {
                        val model: ProfessionalAvailabilityModel = lstAvailability[position]
                        model.isSelected = !model.isSelected
                        lstAvailability[position] = model
                        mAdapter!!.notifyDataSetChanged()
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

        binding.btnSave.setOnClickListener {
            validationProfessionalMyProfile()
        }

        binding.rlProfile.setOnClickListener {
            hideKeyboard()
            permissionCheck()

        }
        binding.llChangePassword.setOnClickListener {
            hideKeyboard()
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)

        }

        binding.llTimeStart.setOnClickListener {
            if (opensTime != null) {
                openDialogTimePickerOpens(binding.etTimeStart)
            }
        }

        binding.etTimeStart.setOnClickListener {
            binding.llTimeStart.performClick()
        }

        binding.llTimeEnd.setOnClickListener {
            if (closesTime != null) {
                openDialogTimePickerOpens(binding.etTimeEnd)
            }
        }

        binding.etTimeEnd.setOnClickListener {
            binding.llTimeEnd.performClick()
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

    private fun validationProfessionalMyProfile() {
        val name = binding.etName.text.toString().trim()
        val scLicenseNumber = binding.etSCLicenseNumber.text.toString().trim()
        val gaLicenseNumber = binding.etGALicenseNumber.text.toString().trim()
        val flLicenseNumber = binding.etFLLicenseNumber.text.toString().trim()
        val ncLicenseNumber = binding.etNCLicenseNumber.text.toString().trim()
        val otherLicenseNumber = binding.etOtherLicenseNumber.text.toString().trim()
        val yearPracticing = binding.etYearsPracticing.text.toString().trim()
//        val availability = binding.etAvailability.text.toString().trim()
        val startTime = binding.etTimeStart.text.toString().trim()
        val endTime = binding.etTimeEnd.text.toString().trim()
        val contact = binding.etTimeEnd.text.toString().trim()
        val userName = binding.etUsername.text.toString().trim()
        val email = binding.etEmailAddress.text.toString().trim()
        val notificationPreference = binding.etNotificationPreference.text.toString().trim()

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

        if (Utils.isValidationEmpty(userProfile) && (selectImageUri == null || selectImageFile == null || !selectImageFile!!.exists() || Utils.isValidationEmpty(
                selectImageFile!!.path
            ))
        ) {
            Utils.showAlert(activity, getString(R.string.valid_empty_select_profile_picture))
        } else if (Utils.isValidationEmpty(name)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_name))
        } else if (binding.cbSC.isChecked && Utils.isValidationEmpty(scLicenseNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbGA.isChecked && Utils.isValidationEmpty(gaLicenseNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbFL.isChecked && Utils.isValidationEmpty(flLicenseNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbNC.isChecked && Utils.isValidationEmpty(ncLicenseNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (binding.cbOther.isChecked && Utils.isValidationEmpty(otherLicenseNumber)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_license_information))
        } else if (Utils.isValidationEmpty(yearPracticing)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_years_practicing))
        } else if (Utils.isValidationEmpty(availabilityDayName) || availabilityDayName == getString(
                R.string.select_availability
            )
        ) {
            Utils.showAlert(activity, getString(R.string.select_availability))
        } else if (Utils.isValidationEmpty(startTime)) {
            Utils.showAlert(activity, getString(R.string.et_time_start_))
        } else if (Utils.isValidationEmpty(endTime)) {
            Utils.showAlert(activity, getString(R.string.et_time_end_))
        } else if (Utils.isValidationEmpty(userName)) {
            Utils.showAlert(activity, getString(R.string.hint_enter_username))
        } else if (Utils.isValidationEmpty(email)) {
            Utils.showAlert(activity, getString(R.string.valid_empty_email))
        } else if (!Utils.isValidEmail(email)) {
            Utils.showAlert(activity, getString(R.string.valid_valid_email_address))
        } else if (Utils.isValidationEmpty(notificationPreference) || notificationPreference == getString(
                R.string.select_notification_preference
            )
        ) {
            Utils.showAlert(activity, getString(R.string.select_notification_preference))
        } else {
            callProfessionalMyProfileApi(
                name,
                scLicenseNumber,
                gaLicenseNumber,
                flLicenseNumber,
                ncLicenseNumber,
                otherLicenseNumber,
                yearPracticing,
                availabilityDayName,
                startTime,
                endTime,
                contact,
                email,
                userName,
                notificationPreference
            )
        }
    }

    private fun callProfessionalMyProfileApi(
        name: String,
        scLicenceNumber: String,
        gaLicenceNumber: String,
        flLicenceNumber: String,
        ncLicenceNumber: String,
        otherLicenceNumber: String,
        yearPracticing: String,
        availability: String,
        startTime: String,
        endTime: String,
        phoneNumber: String,
        email: String,
        userName: String,
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
                Constants.retrofitBuilder.callProfessionalUpdateProfileApi(
                    RetrofitBuilder.HEADER_KEY_VALUE,
                    Preference.getStringName(Preference.LOGIN_TOKEN)!!,
                    Utils.convertValueToRequestBody(Preference.getStringName(Preference.USER_ID))!!,
                    Utils.convertValueToRequestBody(Preference.getStringName(Preference.USER_TYPE))!!,
                    propertyImagePart,
                    Utils.convertValueToRequestBody(name)!!,
                    Utils.convertValueToRequestBody(scLicenceNumber)!!,
                    Utils.convertValueToRequestBody(gaLicenceNumber)!!,
                    Utils.convertValueToRequestBody(flLicenceNumber)!!,
                    Utils.convertValueToRequestBody(ncLicenceNumber)!!,
                    Utils.convertValueToRequestBody(otherLicenceNumber)!!,
                    Utils.convertValueToRequestBody(yearPracticing)!!,
                    Utils.convertValueToRequestBody(availability)!!,
                    Utils.convertValueToRequestBody(startTime)!!,
                    Utils.convertValueToRequestBody(endTime)!!,
                    Utils.convertValueToRequestBody(phoneNumber)!!,
                    Utils.convertValueToRequestBody(email)!!,
                    Utils.convertValueToRequestBody(userName)!!,
                    Utils.convertValueToRequestBody(notificationPreference)!!,
                    Utils.convertValueToRequestBody(Constants.device_type)!!,
                    Utils.convertValueToRequestBody(Constants.device_token)!!
                )

            call.enqueue(object :
                Callback<ResponseObjectModel> {
                override fun onResponse(
                    call: Call<com.dentalhelpinghands.models.office.loginModel.ResponseObjectModel>,
                    response: Response<ResponseObjectModel>
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

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataList() {
        val responseObjectModel = Preference.getUserObject()
        if (responseObjectModel != null) {
            userProfile = responseObjectModel.profile

            Utils.setProfileImage(activity, userProfile, binding.ivProfile)

            binding.etName.setText(responseObjectModel.name)
            binding.etName.setSelection(binding.etName.length())

            binding.etSCLicenseNumber.setText(responseObjectModel.sc_license_number)
            binding.etSCLicenseNumber.setSelection(binding.etSCLicenseNumber.length())

            binding.etGALicenseNumber.setText(responseObjectModel.ga_license_number)
            binding.etGALicenseNumber.setSelection(binding.etGALicenseNumber.length())

            binding.etFLLicenseNumber.setText(responseObjectModel.fl_license_number)
            binding.etFLLicenseNumber.setSelection(binding.etFLLicenseNumber.length())

            binding.etNCLicenseNumber.setText(responseObjectModel.nc_license_number)
            binding.etNCLicenseNumber.setSelection(binding.etNCLicenseNumber.length())

            binding.etOtherLicenseNumber.setText(responseObjectModel.other_license_number)
            binding.etOtherLicenseNumber.setSelection(binding.etOtherLicenseNumber.length())

            binding.etYearsPracticing.setText(responseObjectModel.years_of_practicing)
            binding.etYearsPracticing.setSelection(binding.etYearsPracticing.length())

            /*binding.etAvailability.setText(responseObjectModel.availability)
            binding.etAvailability.setSelection(binding.etAvailability.length())*/

//            "availability": "Monday,Wednesday,Friday",

            val availability: String = responseObjectModel.availability

            if (!Utils.isValidationEmpty(availability)) {

                val strAvailability = availability.split(",")

                if (strAvailability.isNotEmpty()) {
                    lstAvailability.forEachIndexed { index, _ ->
                        for (value in strAvailability) {
                            if (value == lstAvailability[index].name) {
                                lstAvailability[index].isSelected = true
                            }
                        }
                        if (mAdapter != null) {
                            mAdapter!!.notifyDataSetChanged()
                        }
                    }


                }
            }

            binding.etTimeStart.setText(responseObjectModel.availability_start_time)
            //Logger.d(TAG, responseObjectModel.start_time)
            binding.etTimeStart.setSelection(binding.etTimeStart.length())

            binding.etTimeEnd.setText(responseObjectModel.availability_end_time)
            //Logger.d(TAG, responseObjectModel.end_time)
            binding.etTimeEnd.setSelection(binding.etTimeEnd.length())

            binding.etEmailAddress.setText(responseObjectModel.email)
            binding.etEmailAddress.setSelection(binding.etEmailAddress.length())

            binding.etPhoneNumber.setText(responseObjectModel.contact)
            binding.etPhoneNumber.setSelection(binding.etPhoneNumber.length())

            binding.etUsername.setText(responseObjectModel.username)
            binding.etUsername.setSelection(binding.etUsername.length())

            val spinnerNotificationPreference =
                resources.getStringArray(R.array.spinner_notification_preference)
            spinnerNotificationPreference.forEachIndexed { index, _ ->
//                Logger.d("notification",  responseObjectModel.notification_preference)
//                Logger.d("notification",  index)
//                Logger.d("notification",  element)
                if (responseObjectModel.notification_preference == spinnerNotificationPreference[index]) {
                    binding.spNotificationPreference.setSelection(index)
                }
            }

            /*val spinnerNotificationPreference = resources.getStringArray(R.array.spinner_notification_preference)
            spinnerNotificationPreference.forEachIndexed { index, element ->
                Logger.d("notification",  responseObjectModel.notification_preference)
                Logger.d("notification",  index)
                Logger.d("notification",  element)
                if (responseObjectModel.notification_preference == spinnerNotificationPreference[index]) {
                    binding.spNotificationPreference.setSelection(index)
                }
            }*/

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


            //get data for selected date from DateActivity
//        binding.ivStartTimeArrow.visibility = View.VISIBLE
//        binding.ivEndTimeArrow.visibility = View.VISIBLE

            //initialize lunch time open or close
            opensTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
            binding.etTimeStart.setText(opensTime)

            closesTime = Utils.currentDateTime(Constants.DATE_FORMAT_COLUMN_HH_MM_AA).toString()
            binding.etTimeEnd.setText(closesTime)

        }

        /*//get data Professiona User Sign up
        val responseObjectModel = Preference.getUserObject()
        if(responseObjectModel != null) {
            val spinnerNotificationPreference = resources.getStringArray(R.array.spinner_notification_preference)
            spinnerNotificationPreference.forEachIndexed { index, element ->
                Logger.d("notification",  responseObjectModel.notification_preference)
                Logger.d("notification",  index)
                Logger.d("notification",  element)
                if (responseObjectModel.notification_preference == spinnerNotificationPreference[index]) {
                    binding.spNotificationPreference.setSelection(index)
                }
            }
        }*/
    }

    private fun initView() {
        binding.etName.requestFocus()

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

    private fun openDialogTimePickerOpens(time: EditText) {
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

        /*bindingDialogTimePicker.timePicker.setOnTimeChangedListener(fun(
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
            time.setText(opensTime)

        })*/

        bindingDialogTimePicker.btnDone.setOnClickListener {
            val hourOfDay = bindingDialogTimePicker.timePicker.hour
            val minute = bindingDialogTimePicker.timePicker.minute
            val selectedDate = "${hourOfDay}:${minute}"

            opensTime = Utils.customDateTimeFormat(
                selectedDate,
                Constants.DATE_FORMAT_COLUMN_HH_MM_24,
                Constants.DATE_FORMAT_COLUMN_HH_MM_AA
            ).toString()

            time.setText(opensTime)
            dialog.dismiss()
        }

        dialog.show()
    }


    /*private fun openDialogTimePickerCloses() {
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
            //binding.etTimeEnd.text = closesTime

        })

        dialog.show()
    }*/
}