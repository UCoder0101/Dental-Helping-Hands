package com.dentalhelpinghands.Api

import com.dentalhelpinghands.models.office.loginModel.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DentalHelpingHandsApi {
    //Dental Office Module Api
    //@Headers("key: ylvkgexwkqeuduutirtpumgvshofclch")
    @Multipart
    @POST("DentalApiController/signUp")
    fun callSignUpApi(
        @Header("key") key: String,
        @Part profile: MultipartBody.Part?,
        @Part("user_type") userType: RequestBody,
        @Part("practice_name") practiceName: RequestBody,
        @Part("type_of_practice") typePractice: RequestBody,
        @Part("web_address") webAddress: RequestBody,
        @Part("contact_name") contactName: RequestBody,
        @Part("contact") phoneNumber: RequestBody,
        @Part("practice_address") practiceAddress: RequestBody,
        @Part("email") email: RequestBody,
        @Part("username") userName: RequestBody,
        @Part("password") password: RequestBody,
        @Part("reffered_by") referBy: RequestBody,
        @Part("notification_preference") notificationPreference: RequestBody,
        @Part("device_type") deviceType: RequestBody,
        @Part("device_token") deviceToken: RequestBody
    ): Call<ResponseObjectModel>

    @Multipart
    @POST("DentalApiController/signUp")
    fun callProfessionalSignUpApi(
        @Header("key") key: String,
        @Part profile: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("profession") professional: RequestBody,
        @Part("sc_license_number") scLicenseNumber: RequestBody,
        @Part("ga_license_number") gaLicenseNumber: RequestBody,
        @Part("fl_license_number") flLicenseNumber: RequestBody,
        @Part("nc_license_number") ncLicenseNumber: RequestBody,
        @Part("other_license_number") OtherLicenseNumber: RequestBody,
        @Part("years_of_practicing") yearsOfPracticing: RequestBody,
        @Part("availability") availability: RequestBody,
        @Part("availability_start_time") availabilityStartTime: RequestBody,
        @Part("availability_end_time") availabilityEndTime: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("email") email: RequestBody,
        @Part("user_type") userType: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("reffered_by") referBy: RequestBody,
        @Part("notification_preference") notificationPreference: RequestBody,
        @Part("pay_rate") payRate: RequestBody,
        @Part("device_type") deviceType: RequestBody,
        @Part("device_token") deviceToken: RequestBody
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/login")
    fun callLoginApi(
        @Header("key") key: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("token") device_token: String
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/jobList")
    fun callOfficeJobNotificationApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String
    ): Call<ResponseArrayModel>


    @FormUrlEncoded
    @POST("DentalApiController/forgotPassword")
    fun callForgotPasswordApi(
        @Header("key") key: String,
        @Field("email") email: String
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/resendConfirm")
    fun callResendConfirmApi(
        @Header("key") key: String,
        @Field("email") email: String
    ): Call<ResponseObjectModel>


    @FormUrlEncoded
    @POST("DentalApiController/logOut")
    fun callLogOutApi(
        @Header("key") key: String, @Header("token") token: String,
        @Field("user_id") user_id: String
    ): Call<ResponseObjectModel>


    @FormUrlEncoded
    @POST("DentalApiController/postJobs")
    fun callPostJobsApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String,
        @Field("date") date: String,
        @Field("practice_name") practiceName: String,
        @Field("professional") typeProfessional: String,
        @Field("web_address") webAddress: String,
        @Field("contact") mobile: String,
        @Field("type_of_practice") typePractice: String,
        @Field("dental_software") dentalSoftware: String,
        @Field("time_per_patients") timePerPatient: String,
        @Field("office_address") address: String,
        @Field("start_time") startTime: String,
        @Field("end_time") endTime: String,
        @Field("lunch_time_start") startLunchtime: String,
        @Field("lunch_time_end") endLunchtime: String,
        @Field("price_type") priceType: String,
        @Field("price") price: String
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/jobList")
    fun callOfficeJobListApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") user_id: String
    ): Call<ResponseArrayModel>

    @FormUrlEncoded
    @POST("DentalApiController/contactUs")
    fun callContactUsApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("subject") subject: String,
        @Field("message") message: String
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/changePassword")
    fun callChangePasswordApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): Call<ResponseObjectModel>

    @Multipart
    @POST("DentalApiController/updateProfile")
    fun callDentalUpdateProfileApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Part("user_id") userId: RequestBody,
        @Part profile: MultipartBody.Part?,
        @Part("web_address") webAddress: RequestBody,
        @Part("contact_name") contactName: RequestBody,
        @Part("contact") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("notification_preference") notificationPreference: RequestBody,
        @Part("username") userName: RequestBody,
        @Part("device_type") deviceType: RequestBody,
        @Part("device_token") deviceToken: RequestBody
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/deleteJobsPost")
    fun callDeleteJobPostApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("job_id") jobId: String,
        @Field("user_id") userId: String
    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/updatePractice")
    fun callUpdatePracticeApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("job_id") jobId: String,
        @Field("user_id") userId: String,
//        @Field("date") date: String,
        @Field("practice_name") practiceName: String,
        @Field("professional") typeProfessional: String,
        @Field("web_address") webAddress: String,
        @Field("contact") mobile: String,
        @Field("type_of_practice") typePractice: String,
        @Field("dental_software") dentalSoftware: String,
        @Field("time_per_patients") timePerPatient: String,
        @Field("office_address") address: String,
        @Field("start_time") startTime: String,
        @Field("end_time") endTime: String,
        @Field("lunch_time_start") startLunchtime: String,
        @Field("lunch_time_end") endLunchtime: String,
        @Field("price_type") priceType: String,
        @Field("price") price: String

    ): Call<ResponseObjectModel>

    @FormUrlEncoded
    @POST("DentalApiController/JobApplicationApprove")
    fun callJobApplicationApproveApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String,
        @Field("action_type") actionType: String,
        @Field("job_apply_id") jobApplyId: String
    ): Call<ResponseObjectModel>


    //professional Module Api
    @Multipart
    @POST("ProfessionalApiController/updateProfile")
    fun callProfessionalUpdateProfileApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Part("user_id") userId: RequestBody,
        @Part("user_type") userType: RequestBody,
        @Part profile: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("sc_license_number") scLicenseNumber: RequestBody,
        @Part("ga_license_number") gaLicenseNumber: RequestBody,
        @Part("fl_license_number") flLicenseNumber: RequestBody,
        @Part("nc_license_number") ncLicenseNumber: RequestBody,
        @Part("other_license_number") otherLicenseNumber: RequestBody,
        @Part("years_of_practicing") yearsOfPracticing: RequestBody,
        @Part("availability") availability: RequestBody,
        @Part("start_time") startTime: RequestBody,
        @Part("end_time") endTime: RequestBody,
        @Part("contact") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part("username") userName: RequestBody,
        @Part("notification_preference") notificationPreference: RequestBody,
        @Part("device_type") deviceType: RequestBody,
        @Part("device_token") deviceToken: RequestBody
    ): Call<ResponseObjectModel>


    @FormUrlEncoded
    @POST("ProfessionalApiController/jobList")
    fun callProfessionalJobListApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String
    ): Call<ResponseArrayModel>


    @FormUrlEncoded
    @POST("ProfessionalApiController/applyNow")
    fun callJobApplyNowApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String,
        @Field("job_id") jobId: String
    ): Call<ResponseObjectModel>


    @FormUrlEncoded
    @POST("ProfessionalApiController/applyedJobList")
    fun callApplyJobListApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String
    ): Call<ResponseArrayModel>


    @FormUrlEncoded
    @POST("ProfessionalApiController/approvedJobsList")
    fun callApprovedJobsListApi(
        @Header("key") key: String,
        @Header("token") token: String,
        @Field("user_id") userId: String
    ): Call<ResponseArrayModel>

}