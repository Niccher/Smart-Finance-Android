package com.niccher.smart_finance.models

import com.google.gson.annotations.SerializedName

data class SystemVersionResponseModel(
    @SerializedName("version") val version: String,
    @SerializedName("github_url") val githubUrl: String,
    @SerializedName("changelog") val changelog: List<String>
)
