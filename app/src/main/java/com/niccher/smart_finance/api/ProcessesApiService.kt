package com.niccher.smart_finance.api

import com.niccher.smart_finance.models.SummaryResponse
import com.niccher.smart_finance.models.LootSummaryModel
import com.niccher.smart_finance.models.MyLootCountModel
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProcessesApiService {
    @FormUrlEncoded
    @POST("api/v1/financial/uploads")
    fun getSummary(@FieldMap fields: Map<String, String>): Call<SummaryResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/uploads-summary")
    fun getSummaryCalc(@FieldMap fields: Map<String, String>): Call<LootSummaryModel>

    @FormUrlEncoded
    @POST("api/v1/financial/uploads-count")
    fun getLootCount(@FieldMap fields: Map<String, String>): Call<MyLootCountModel>
}