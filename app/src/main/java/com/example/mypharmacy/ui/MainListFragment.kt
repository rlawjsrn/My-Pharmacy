package com.example.mypharmacy.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.members.widget.PharmacyAdapter
import com.example.mypharmacy.R
import com.example.mypharmacy.databinding.FragmentMainListBinding
import com.example.mypharmacy.model.Item

class MainListFragment : Fragment() {
    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainListViewModel by viewModels()
    private val adapter = PharmacyAdapter()
    private var userLocation: Location? = null // 사용자 위치를 저장할 변수

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            Log.d("MainListFragment", "권한 부여됨")
            getLocation()
        } else {
            Log.d("MainListFragment", "권한 미허용")
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("MainListFragment", "GPS 비활성화됨")
                // GPS가 비활성화되어 있는 경우, 사용자에게 위치 서비스를 활성화하도록 요청
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                return
            }
            val provider = LocationManager.GPS_PROVIDER
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                userLocation = location
                Log.d("MyLocation", "마지막 위치: $location")
                // 위치가 업데이트되면 ViewModel의 함수 호출
                viewModel.getPharmacies(
                    userLocation?.latitude ?: 0.0,
                    userLocation?.longitude ?: 0.0
                )
            } else {
                Log.d("MainListFragment", "마지막 위치 없음, 위치 업데이트 요청")
                // GPS 제공자에 대해 위치 업데이트 요청
                locationManager.requestLocationUpdates(provider, 3000L, 100f) { result ->
                    userLocation = result
                    Log.d("MyLocation", "업데이트된 위치 (GPS): $result")
                    // 위치가 업데이트되면 ViewModel의 함수 호출
                    viewModel.getPharmacies(
                        userLocation?.latitude ?: 0.0,
                        userLocation?.longitude ?: 0.0
                    )
                }
                // 네트워크 제공자에 대해 위치 업데이트 요청
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    3000L,
                    100f
                ) { result ->
                    userLocation = result
                    Log.d("MyLocation", "업데이트된 위치 (NETWORK): $result")
                    // 위치가 업데이트되면 ViewModel의 함수 호출
                    viewModel.getPharmacies(
                        userLocation?.latitude ?: 0.0,
                        userLocation?.longitude ?: 0.0
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewPharmacy.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPharmacy.adapter = adapter

        // 어댑터에 리스너 추가 및 로그 추가
        adapter.addListener {
            val bundle = Bundle().apply { putString("HPID", it.hpid) }
            findNavController().navigate(R.id.action_mainListFragment_to_detailFragment, bundle)
        }

        binding.fab.setOnClickListener {
            userLocation?.let { location ->
                val locations = doubleArrayOf(location.latitude, location.longitude)
                val bundle = Bundle().apply { putDoubleArray("userLocation", locations) }
                findNavController().navigate(R.id.action_mainListFragment_to_mapListFragment, bundle)
            }
        }

        viewModel.pharmacies.observe(viewLifecycleOwner) { pharmacies ->
            adapter.updateData(pharmacies)
        }

        // 권한 요청
        requestPermission.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
