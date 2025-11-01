package com.example.fishsecure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.model.Aquarium

class AquariumAdapter(
    private val aquariums: MutableList<Aquarium>,
    private val clickListener: (Aquarium, ActionType) -> Unit
) : RecyclerView.Adapter<AquariumAdapter.AquariumViewHolder>() {

    enum class ActionType { VIEW, EDIT, DELETE }

    inner class AquariumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Find all the views from item_aquarium.xml
        val name: TextView = itemView.findViewById(R.id.tv_aquarium_name)
        val description: TextView = itemView.findViewById(R.id.tv_aquarium_description)
        val status: TextView = itemView.findViewById(R.id.tv_aquarium_status)
        val comment: TextView = itemView.findViewById(R.id.tv_aquarium_comment)
        private val btnView: Button = itemView.findViewById(R.id.btn_view)
        private val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(aquarium: Aquarium) {
            name.text = aquarium.name
            description.text = aquarium.description
            status.text = aquarium.status
            comment.text = aquarium.comment

            // Attach the click listener with the specific action type
            btnView.setOnClickListener { clickListener(aquarium, ActionType.VIEW) }
            btnEdit.setOnClickListener { clickListener(aquarium, ActionType.EDIT) }
            btnDelete.setOnClickListener { clickListener(aquarium, ActionType.DELETE) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AquariumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aquarium, parent, false)
        return AquariumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AquariumViewHolder, position: Int) {
        holder.bind(aquariums[position])
    }

    override fun getItemCount(): Int = aquariums.size

    // Helper method to add a new item and refresh the list
    fun addItem(aquarium: Aquarium) {
        aquariums.add(aquarium)
        notifyItemInserted(aquariums.size - 1)
    }

    // Helper method to delete an item and refresh the list
    fun deleteItem(aquarium: Aquarium) {
        val index = aquariums.indexOfFirst { it.id == aquarium.id }
        if (index != -1) {
            aquariums.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
