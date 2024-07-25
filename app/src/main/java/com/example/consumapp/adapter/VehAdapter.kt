package com.example.consumapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.consumapp.databinding.ActivityStartScreenBinding
import com.google.android.gms.tasks.Task

class VehAdapter(
    val vehList: List<Task<String>>
    //private lateinit var vehicles : VehicleModel
) : RecyclerView.Adapter<VehAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehAdapter.MyViewHolder {

        return MyViewHolder(
            ActivityStartScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VehAdapter.MyViewHolder, position: Int) {
        val vehicle = vehList[position]
        //if() vehicles = VehicleModel
        /*val brand = vehicles.brand.toString().trim()
        val model = vehicles.model.toString().trim()
        val age = vehicles.age.toString().trim()
        holder.binding.vehicleName.text = "$brand $model"
        holder.binding.vehicleAge.text = "Ano $age"*/
    }

    override fun getItemCount() = vehList.size

    inner class MyViewHolder(val binding: ActivityStartScreenBinding) :
            RecyclerView.ViewHolder(binding.root)
}