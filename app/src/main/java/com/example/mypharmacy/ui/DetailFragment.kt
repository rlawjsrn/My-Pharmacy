package com.example.mypharmacy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mypharmacy.R
import com.example.mypharmacy.databinding.FragmentDetailBinding
import com.example.mypharmacy.model.Item
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class DetailFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private var googleMap: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val hpId = arguments?.getString("HPID") ?: return
        viewModel.getPharmacy(hpId)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_mainListFragment)
        }

        viewModel.pharmacy.observe(viewLifecycleOwner) { item ->
            var timeList: List<String> = listOf()
            val timeListBuilder = mutableListOf<String>()
            if (item.dutyTime1s != null && item.dutyTime1c != null) {
                timeListBuilder.add("월요일  ${formatTime(item.dutyTime1s)} - ${formatTime(item.dutyTime1c)}")
            }
            if (item.dutyTime2s != null && item.dutyTime2c != null) {
                timeListBuilder.add("화요일  ${formatTime(item.dutyTime2s)} - ${formatTime(item.dutyTime2c)}")
            }
            if (item.dutyTime3s != null && item.dutyTime3c != null) {
                timeListBuilder.add("수요일  ${formatTime(item.dutyTime3s)} - ${formatTime(item.dutyTime3c)}")
            }
            if (item.dutyTime4s != null && item.dutyTime4c != null) {
                timeListBuilder.add("목요일  ${formatTime(item.dutyTime4s)} - ${formatTime(item.dutyTime4c)}")
            }
            if (item.dutyTime5s != null && item.dutyTime5c != null) {
                timeListBuilder.add("금요일  ${formatTime(item.dutyTime5s)} - ${formatTime(item.dutyTime5c)}")
            }
            if (item.dutyTime6s != null && item.dutyTime6c != null) {
                timeListBuilder.add("토요일  ${formatTime(item.dutyTime6s)} - ${formatTime(item.dutyTime6c)}")
            }
            if (item.dutyTime7s != null && item.dutyTime7c != null) {
                timeListBuilder.add("일요일  ${formatTime(item.dutyTime7s)} - ${formatTime(item.dutyTime7c)}")
            }
            if (item.dutyTime8s != null && item.dutyTime8c != null) {
                timeListBuilder.add("공휴일  ${formatTime(item.dutyTime8s)} - ${formatTime(item.dutyTime8c)}")
            }
            timeList = timeListBuilder
            binding.pharmacyName.text = item.dutyName
            binding.tel.text = item.dutyTel1
            binding.addr.text = item.dutyAddr

            val dutyTimeText = timeList.joinToString("\n")
            binding.textViewDutyTime.text = dutyTimeText


            updateMapLocation(item)

        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun updateMapLocation(item: Item) {
        val latitude = item.wgs84Lat?.toDoubleOrNull()
        val longitude = item.wgs84Lon?.toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val location = LatLng(latitude, longitude)
            val markerOptions = MarkerOptions().position(location).title(item.dutyName)

            // 커스텀 아이콘
            val customIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker)
            markerOptions.icon(customIcon)

            googleMap?.addMarker(markerOptions)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 시간 형식
    private fun formatTime(time: String?): String {
        if (time.isNullOrEmpty() || time.length != 4) return time ?: ""
        val hour = time.substring(0, 2)
        val minute = time.substring(2, 4)
        return "$hour:$minute"
    }
}
