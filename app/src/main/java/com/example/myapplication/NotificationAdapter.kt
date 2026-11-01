package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(
    private val onNotificationClick: (NotificationEntity) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications = listOf<NotificationEntity>()

    fun submitList(newList: List<NotificationEntity>) {
        notifications = newList
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val icon: TextView =
            itemView.findViewById(R.id.notificationIcon)

        val title: TextView =
            itemView.findViewById(R.id.notificationTitle)

        val message: TextView =
            itemView.findViewById(R.id.notificationMessage)

        val time: TextView =
            itemView.findViewById(R.id.notificationTime)

        val unreadDot: View =
            itemView.findViewById(R.id.unreadDot)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)

        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {

        val notification = notifications[position]

        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.time.text = formatTime(notification.createdAt)

        holder.icon.text = when (notification.type) {
            "found" -> "📦"
            "lost" -> "🔍"
            "match" -> "🎯"
            "claim" -> "✅"
            else -> "🔔"
        }

        holder.unreadDot.visibility =
            if (notification.isRead) View.INVISIBLE else View.VISIBLE

        holder.itemView.setOnClickListener {
            onNotificationClick(notification)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    private fun formatTime(timestamp: Long): String {

        val elapsed = System.currentTimeMillis() - timestamp

        val minutes = elapsed / 60_000
        val hours = elapsed / 3_600_000
        val days = elapsed / 86_400_000

        return when {
            elapsed < 0 -> "Just now"
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "Yesterday"
            else -> "$days days ago"
        }
    }
}