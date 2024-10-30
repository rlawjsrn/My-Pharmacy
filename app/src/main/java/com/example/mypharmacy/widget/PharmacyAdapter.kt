package com.example.members.widget

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmacy.R
import com.example.mypharmacy.model.Item

class PharmacyAdapter : RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>() {
    private var listener: OnItemSelected? = null
    private var data: List<Item> = listOf()

    fun updateData(data: List<Item>) {
        this.data = data
        notifyItemRangeChanged(0, data.size)
    }


    fun addListener(listener: OnItemSelected) {
        this.listener = listener
    }

    // Data보다는 적은 수지만 RecyclerView를 가득 채우고 스크롤 할 수 있을 만큼의 ViewHolder는 만들어야 함.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pharmacy, parent, false)
        return PharmacyViewHolder(view)
    }

    // ViewHolder에 Data의 내용을 넣는 작업
    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    // Data의 개수를 알려주는 함수. 실제 Data가 많아도 여기서 0을 반환하면 Recycler view에는 아무것도 그려지지 않음
    override fun getItemCount(): Int = data.size

    fun interface OnItemSelected {
        fun onItemSelected(pharmacy: Item)
    }

    inner class PharmacyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val textViewName: TextView = v.findViewById(R.id.textViewName)
        private val textViewDistance: TextView = v.findViewById(R.id.textViewDistance)
        private lateinit var pharmacy: Item

        init {
            v.setOnClickListener {
                listener?.onItemSelected(pharmacy)
                Log.d("PharmacyAdapter", "Clicked item with ID: ${pharmacy.hpid}")
            }
        }

        fun bind(pharmacy: Item) {
            this.pharmacy = pharmacy
            textViewName.text = pharmacy.dutyName

            // 거리 정보 처리
            pharmacy.distance?.let { distance ->
                val formattedDistance = if (distance >= 1000) {
                    val kmDistance = distance / 1000.0
                    val roundedDistance = String.format("%.1f", kmDistance) // 소수점 첫째자리 반올림
                    "${roundedDistance}km"
                } else {
                    "${distance}m"
                }
                textViewDistance.text = formattedDistance
            } ?: run {
                textViewDistance.text = "거리 정보 없음"
            }
        }
    }
}
