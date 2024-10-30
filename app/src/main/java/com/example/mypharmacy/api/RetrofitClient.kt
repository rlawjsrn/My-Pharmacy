package com.example.mypharmacy.api

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

class RetrofitClient {
    companion object {
        /*Open API End Point Url*/
        private const val BASE_URL =
            "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/"

        private val client = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
            .build()

        // Retrofit 클라이언트를 사용해 PharmaciesApi 인터페이스 구현체 생성
        val pharmacyApi: PharmaciesApi = client.create(PharmaciesApi::class.java)

    }
}
