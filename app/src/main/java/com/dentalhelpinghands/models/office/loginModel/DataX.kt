package com.dentalhelpinghands.models.office.loginModel

import java.io.Serializable

data class DataX(
    val availability: String,
    val availability_end_time: String,
    val availability_start_time: String,
    val lunch_time_start: String,
    val lunch_time_end: String,
    val start_time: String,
    val end_time: String,
    val contact: String,
    val date: String,
    val office_address: String,
    val dental_software: String,
    val contact_name: String,
    val created_date: String,
    val device_token: String,
    val device_type: String,
    val email: String,
    val jobs_detail: DataX?,
    val time_per_patients: String,
    val fl_license_number: String,
    val ga_license_number: String,
    val is_confirmed: String,
    val name: String,
    val nc_license_number: String,
    val notification_preference: String,
    val other_license_number: String,
    val password: String,
    val pay_rate: String,
    val practice_address: String,
    val practice_name: String,
    val profession: String,
    val average_patients: String,
    val professional: String,
    val profile: String,
    val reffered_by: String,
    val sc_license_number: String,
    val states_licensed_in: String,
    val token: String,
    val type_of_practice: String,
    val ucode: String,
    val updated_date: String,
    val notification_discription: String,
    val user_id: String,
    val job_id: String,
    val job_apply_id: String,
    val user_type: String,
    val username: String,
    val is_approved: String,
    val web_address: String,
    val years_of_practicing: String,
    val applyedJobs: Int,
    val price_type: String,
    val price: Int,

    ): Serializable