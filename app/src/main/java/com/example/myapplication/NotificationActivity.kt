package com.example.myapplication

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActivity : AppCompatActivity() {

    private lateinit var adapter: NotificationAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var unreadCount: TextView

    private lateinit var allButton: Button
    private lateinit var unreadButton: Button

    private var userId: Int = -1
    private var showUnreadOnly = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)

        userId = intent.getIntExtra("userId", -1)

        val backButton =
            findViewById<ImageButton>(R.id.back_btn)

        recyclerView =
            findViewById(R.id.notificationsRecyclerView)

        emptyView =
            findViewById(R.id.emptyNotifications)

        unreadCount =
            findViewById(R.id.unreadCount)

        allButton =
            findViewById(R.id.allNotifications)

        unreadButton =
            findViewById(R.id.unreadNotifications)

        adapter = NotificationAdapter { notification ->

            lifecycleScope.launch {

                withContext(Dispatchers.IO) {

                    AppDatabase.getInstance(
                        applicationContext
                    ).notificationDao().markAsRead(
                        notificationId = notification.id,
                        userId = userId
                    )
                }

                loadNotifications()
            }
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            finish()
        }

        allButton.setOnClickListener {
            showUnreadOnly = false
            updateFilterButtons()
            loadNotifications()
        }

        unreadButton.setOnClickListener {
            showUnreadOnly = true
            updateFilterButtons()
            loadNotifications()
        }

        findViewById<TextView>(R.id.markAllRead)
            .setOnClickListener {

                if (userId == -1) return@setOnClickListener

                lifecycleScope.launch {

                    withContext(Dispatchers.IO) {

                        AppDatabase.getInstance(
                            applicationContext
                        ).notificationDao()
                            .markAllAsRead(userId)
                    }

                    loadNotifications()
                }
            }

        updateFilterButtons()
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            loadNotifications()
        }
    }

    private fun loadNotifications() {

        if (userId == -1) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = "Unable to identify the current user."
            unreadCount.text = "0"
            return
        }

        lifecycleScope.launch {

            val result = withContext(Dispatchers.IO) {

                val dao = AppDatabase.getInstance(
                    applicationContext
                ).notificationDao()

                val notifications =
                    dao.getNotificationsForUser(userId)

                val unread =
                    dao.getUnreadCount(userId)

                Pair(notifications, unread)
            }

            val allNotifications = result.first
            val unread = result.second

            unreadCount.text = unread.toString()

            val displayedNotifications =
                if (showUnreadOnly) {
                    allNotifications.filter { !it.isRead }
                } else {
                    allNotifications
                }

            adapter.submitList(displayedNotifications)

            val isEmpty = displayedNotifications.isEmpty()

            emptyView.visibility =
                if (isEmpty) View.VISIBLE else View.GONE

            recyclerView.visibility =
                if (isEmpty) View.GONE else View.VISIBLE

            if (isEmpty) {
                emptyView.text = if (showUnreadOnly) {
                    "You're all caught up!\nNo unread notifications."
                } else {
                    "No notifications yet.\nWe'll keep you updated!"
                }
            }
        }
    }

    private fun updateFilterButtons() {

        val blue = Color.parseColor("#1A73E8")
        val white = Color.WHITE
        val dark = Color.parseColor("#172033")

        allButton.backgroundTintList =
            ColorStateList.valueOf(
                if (showUnreadOnly) white else blue
            )

        allButton.setTextColor(
            if (showUnreadOnly) dark else white
        )

        unreadButton.backgroundTintList =
            ColorStateList.valueOf(
                if (showUnreadOnly) blue else white
            )

        unreadButton.setTextColor(
            if (showUnreadOnly) white else dark
        )
    }
}