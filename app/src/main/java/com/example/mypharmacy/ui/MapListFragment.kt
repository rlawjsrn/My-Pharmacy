package com.example.mypharmacy.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mypharmacy.R
import com.example.mypharmacy.databinding.FragmentDetailBinding
import com.example.mypharmacy.databinding.FragmentMapListBinding
import com.example.mypharmacy.model.Item
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapListFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapListBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null
    private var pharmacies: List<Item> = listOf()
    private val viewModel: MainListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locations = arguments?.getDoubleArray("userLocation")

        if (locations != null) {
            viewModel.getPharmacies(locations[0], locations[1])
        }

        viewModel.pharmacies.observe(viewLifecycleOwner, Observer { updatedPharmacies ->
            pharmacies = updatedPharmacies
            updateMapMarkers()
        })

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_mapListFragment_to_mainListFragment)
        }

    }

//    override fun onMapReady(map: GoogleMap) {
//        googleMap = map
//        pharmacies.forEach { pharmacy ->
//            if (pharmacy.wgs84Lat != null && pharmacy.wgs84Lon != null) {
//                val location =
//                    LatLng(pharmacy.wgs84Lat!!.toDouble(), pharmacy.wgs84Lon!!.toDouble())
//                googleMap?.addMarker(
//                    MarkerOptions()
//                        .position(location)
//                        .title(pharmacy.dutyName)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
//                )
//            }
//        }
//        if (pharmacies.isNotEmpty()) {
//            val firstLocation = LatLng(
//                pharmacies[0].wgs84Lat?.toDoubleOrNull() ?: 0.0,
//                pharmacies[0].wgs84Lon?.toDoubleOrNull() ?: 0.0
//            )
//            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12f))
//        }
//    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMapMarkers()
    }

    private fun updateMapMarkers() {
        googleMap?.clear()
        pharmacies.forEach { pharmacy ->
            if (pharmacy.wgs84Lat != null && pharmacy.wgs84Lon != null) {
                val location =
                    LatLng(pharmacy.wgs84Lat!!.toDouble(), pharmacy.wgs84Lon!!.toDouble())
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(pharmacy.dutyName)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                )
            }
        }
        if (pharmacies.isNotEmpty()) {
            val firstLocation = LatLng(
                pharmacies[0].wgs84Lat?.toDoubleOrNull() ?: 0.0,
                pharmacies[0].wgs84Lon?.toDoubleOrNull() ?: 0.0
            )
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 15f))
        }
    }

}