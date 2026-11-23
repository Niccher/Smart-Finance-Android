package com.niccher.smart_finance.api

import com.niccher.smart_finance.models.FoneIdModel
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FonePrintApiService {

    @FormUrlEncoded
    @POST("api/v1/device")
    fun createPrint(@FieldMap fields: Map<String, String>): Call<FoneIdModel>
}