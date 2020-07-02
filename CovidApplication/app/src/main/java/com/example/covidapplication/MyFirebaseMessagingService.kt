package com.example.covidapplication

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    val TAG="FCM Service"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d(TAG, "From: " + p0!!.from)
        Log.d(TAG, "Notification Message Body: "+ p0.notification!!.body!!)
        }
    }
