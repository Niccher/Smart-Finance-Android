package com.niccher.smart_finance.models

data class SettingsResponseModel(
    val status: String,
    val settings: Map<String, Any>?
)

data class ProfileResponseModel(
    val status: String,
    val user: UserProfileModel?
)

data class UserProfileModel(
    val id: Int,
    val username: String,
    val email: String,
    val created_at: String
)

data class NoteResponseModel(
    val status: String,
    val note: String
)

data class GenericResponseModel(
    val status: String,
    val message: String
)
