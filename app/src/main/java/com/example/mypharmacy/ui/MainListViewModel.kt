package com.example.mypharmacy.ui

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypharmacy.api.RetrofitClient
import com.example.mypharmacy.model.Item
import com.example.mypharmacy.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class MainListViewModel : ViewModel() {
    val pharmacies = MutableLiveData<List<Item>>()

    fun getPharmacies(userLat: Double, userLon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            RetrofitClient.pharmacyApi.getPharmacies(
                "uFfX1uIn4VJvKAYCqPen0T41BiE0aQKe+wQbD1v1RATxmvbSB7ohrYKZVCQ9uHXM1SC4RvgbMc6/N1mDRpb2xg=="
            )
                .enqueue(object : Callback<Response> {

                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        val body = response.body()
                        Log.d("body: ", body.toString())
                        if (body != null) {
                            val items = body.body?.items?.item ?: emptyList()

                            // 필터링된 약국 목록을 담을 리스트
                            val nearbyPharmacies = mutableListOf<Item>()

                            // 사용자의 위치
                            val userLocation = Location("")
                            userLocation.latitude = userLat
                            userLocation.longitude = userLon
                            Log.d("MainListViewModel", userLocation.toString())

                            // 거리를 기준으로 약국 리스트를 정렬
                            val sortedPharmacies = items.sortedBy { pharmacy ->
                                val pharmacyLocation = Location("")
                                pharmacyLocation.latitude = pharmacy.wgs84Lat?.toDouble() ?: 0.0
                                pharmacyLocation.longitude = pharmacy.wgs84Lon?.toDouble() ?: 0.0
                                userLocation.distanceTo(pharmacyLocation) // 사용자 위치와 약국 위치 간의 거리 계산
                            }

                            // 각 약국의 위치를 확인하고 2km 이내인 경우 리스트에 추가
                            for (pharmacy in sortedPharmacies) {
                                val pharmacyLocation = Location("")
                                pharmacyLocation.latitude = pharmacy.wgs84Lat?.toDouble() ?: 0.0
                                pharmacyLocation.longitude = pharmacy.wgs84Lon?.toDouble() ?: 0.0
                                val distance = userLocation.distanceTo(pharmacyLocation).toInt()
                                if (distance <= 1500) { // 1.5Km 이내인 경우
                                    pharmacy.distance = distance
                                    nearbyPharmacies.add(pharmacy)
                                }
                            }
                            Log.d("nearbyPharmacies",nearbyPharmacies.toString())
                            pharmacies.postValue(nearbyPharmacies)

                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.d("fail", t.message.toString())
                    }
                })

        }
    }
}