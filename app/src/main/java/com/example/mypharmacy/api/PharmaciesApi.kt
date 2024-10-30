package com.example.mypharmacy.api

import com.example.mypharmacy.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PharmaciesApi {
    //  약국 한 건 조회
    @GET("getParmacyBassInfoInqire")
    fun getPharmacy(
        @Query("serviceKey")
        serviceKey: String,
        @Query("HPID") HPID: String
    ): Call<Response>


    // 대구 약국 리스트(반경 1.5km내)
    @GET("getParmacyListInfoInqire?")
    fun getPharmacies(
        @Query("serviceKey")
        serviceKey: String,
        @Query("numOfRows")
        numOfRows: Int = 1000,
        @Query("Q0")
        Q0: String = "대구광역시",
        @Query("pageNo")
        pageNo: Int = 1
    ): Call<Response>
}