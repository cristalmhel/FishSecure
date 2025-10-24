package com.example.fishsecure.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.R
import com.example.fishsecure.model.Aquarium

class AquariumAdapter(private var aquariums: List<Aquarium>,
                      private val onDeleteClick: (Int) -> Unit,
                      private val onRenameClick: (Int) -> Unit) :
    RecyclerView.Adapter<AquariumAdapter.AquariumViewHolder>() {

    class AquariumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.aquariumTitle)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteAquariumBtn)
        val editBtn: Button = itemView.findViewById(R.id.editAquariumBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AquariumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aquarium, parent, false)
        return AquariumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AquariumViewHolder, position: Int) {
        val aquarium = aquariums[position]
        holder.title.text = aquarium.name
        holder.deleteBtn.setOnClickListener { onDeleteClick(aquarium.id) }
        holder.editBtn.setOnClickListener { onRenameClick(aquarium.id) }
    }

    override fun getItemCount(): Int = aquariums.size

    fun updateData(newAquariums: List<Aquarium>) {
        aquariums = newAquariums
        notifyDataSetChanged()
    }
}
