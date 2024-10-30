package com.example.mypharmacy.ui

import android.location.Location
import android.util.Log
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

class DetailViewModel : ViewModel() {
    val pharmacy = MutableLiveData<Item>()

    fun getPharmacy(hpId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            RetrofitClient.pharmacyApi.getPharmacy(
                "uFfX1uIn4VJvKAYCqPen0T41BiE0aQKe+wQbD1v1RATxmvbSB7ohrYKZVCQ9uHXM1SC4RvgbMc6/N1mDRpb2xg==",
                hpId
            )
                .enqueue(object : Callback<Response> {

                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        val body = response.body()
                        Log.d("DetailViewModel", response.toString())
                        Log.d("DetailViewModel hpid: ", hpId)
                        if (body != null) {
                            val items = body.body?.items?.item
                            if (items != null && items.isNotEmpty()) {
                                pharmacy.postValue(items[0])
                            }
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.d("fail", t.message.toString())
                    }
                })

        }
    }
}