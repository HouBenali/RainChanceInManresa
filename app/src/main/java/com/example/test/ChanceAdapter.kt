package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChanceAdapter (val hourlyChance : List<HoraTiempo>):RecyclerView.Adapter<ChanceAdapter.ChanceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChanceHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChanceHolder(layoutInflater.inflate(R.layout.hourlychance,parent,false))
    }

    override fun getItemCount(): Int { return hourlyChance.size }

    override fun onBindViewHolder(holder: ChanceHolder, position: Int) {
        holder.hour.text = hourlyChance[position].hour.toString()
        holder.chance.text = hourlyChance[position].chance+"%"
    }

    class ChanceHolder(val view: View):RecyclerView.ViewHolder(view) {
        public val hour: TextView
        public val chance: TextView

        init {
            // Define click listener for the ViewHolder's View.
            hour = view.findViewById(R.id.tvhour)
            chance = view.findViewById(R.id.tvchance)
        }
    }

}