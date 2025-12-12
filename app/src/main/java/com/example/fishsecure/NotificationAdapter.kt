package com.example.fishsecure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.model.NotificationModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    private val items = mutableListOf<NotificationModel>()

    fun submitList(list: List<NotificationModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): NotificationModel = items[position]

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: NotificationModel, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    inner class NotifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val problem: TextView = itemView.findViewById(R.id.txtProblem)
        private val timestamp: TextView = itemView.findViewById(R.id.txtTimestamp)
        private val aquariumId: TextView = itemView.findViewById(R.id.txtAquarium)
        private val deleteBtn: ImageView = itemView.findViewById(R.id.btnDelete)

        fun bind(model: NotificationModel) {
            problem.text = model.problem
            aquariumId.text = "Aquarium: ${model.aquariumName}"
            timestamp.text = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
                .format(Date(model.timestamp))
            deleteBtn.setOnClickListener { onDelete(model.notificationId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

