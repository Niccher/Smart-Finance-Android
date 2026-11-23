package com.niccher.smart_finance.api

import com.niccher.smart_finance.models.*
import com.niccher.smart_finance.models.FinancialAnalystModel.FinancialHealthResponse
import com.niccher.smart_finance.models.FinancialAnalystModel.SmartAlertsResponse
import com.niccher.smart_finance.models.FinancialAnalystModel.RecurringPaymentsResponse
import com.niccher.smart_finance.models.FinancialAnalystModel.SpendingTrendsResponse
import com.niccher.smart_finance.models.FinancialAnalystModel.AIObservationsResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FinancialApiService {
    @FormUrlEncoded
    @POST("api/v1/financial/overview")
    fun getFinancialOverview(@FieldMap fields: Map<String, String>): Call<FinancialOverviewResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/categories")
    fun getTransactionsByCategory(@FieldMap fields: Map<String, String>): Call<TransactionsByCategoryResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/senders")
    fun getSenderProfiles(@FieldMap fields: Map<String, String>): Call<SenderProfilesResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/health")
    fun getFinancialHealth(@FieldMap fields: Map<String, String>): Call<FinancialHealthResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/alerts")
    fun getSmartAlerts(@FieldMap fields: Map<String, String>): Call<SmartAlertsResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/recurring")
    fun getRecurringPayments(@FieldMap fields: Map<String, String>): Call<RecurringPaymentsResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/trends")
    fun getSpendingTrends(@FieldMap fields: Map<String, String>): Call<SpendingTrendsResponse>

    @FormUrlEncoded
    @POST("api/v1/financial/insights")
    fun getAIObservations(@FieldMap fields: Map<String, String>): Call<AIObservationsResponse>
}

