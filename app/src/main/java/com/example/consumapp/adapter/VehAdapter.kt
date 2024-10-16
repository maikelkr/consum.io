package com.example.consumapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.consumapp.R
import com.example.consumapp.model.VehicleModel

class VehAdapter(private val context: Context, private val vehicles: MutableList<VehicleModel>) : RecyclerView.Adapter<VehAdapter.VehicleViewHolder>() {
    //var onItemClick : ((VehicleModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehAdapter.VehicleViewHolder {
        val itemList = LayoutInflater.from(context).inflate(R.layout.activity_vehicle_item,parent,false)
        val holder = VehicleViewHolder(itemList)
        return holder
    }

    override fun onBindViewHolder(holder: VehAdapter.VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.model.text = vehicle.model
        holder.age.text = "Ano " + vehicle.age
        holder.brand.text = vehicle.brand
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(vehicle)
        }
    }

    override fun getItemCount() = vehicles.size

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val model = itemView.findViewById<TextView>(R.id.vehicle_name)
        val age = itemView.findViewById<TextView>(R.id.vehicle_age)
        val brand = itemView.findViewById<TextView>(R.id.vehicle_brand)
    }

    companion object {
        var onItemClick: ((VehicleModel) -> Unit)? = null
    }

}