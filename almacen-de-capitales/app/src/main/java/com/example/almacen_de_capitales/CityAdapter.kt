package com.example.almacen_de_capitales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.almacen_de_capitales.databinding.ItemCityBinding
import com.example.almacen_de_capitales.entity.CityEntity

class CityAdapter(
    private val onInfo: (CityEntity) -> Unit,
    private val onEdit: (CityEntity) -> Unit,
    private val onDelete: (CityEntity) -> Unit
) : ListAdapter<CityEntity, CityAdapter.CityVH>(Diff) {

    object Diff : DiffUtil.ItemCallback<CityEntity>() {
        override fun areItemsTheSame(a: CityEntity, b: CityEntity) = a.id == b.id
        override fun areContentsTheSame(a: CityEntity, b: CityEntity) = a == b
    }

    inner class CityVH(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: CityEntity) = with(binding) {
            tvCity.text = city.cityName
            tvCountry.text = city.countryName
            tvPopulation.text = "Población: ${city.population}"
            root.setOnClickListener { onInfo(city) }
            btnEdit.setOnClickListener { onEdit(city) }
            btnDelete.setOnClickListener { onDelete(city) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CityVH(ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CityVH, position: Int) =
        holder.bind(getItem(position))
}