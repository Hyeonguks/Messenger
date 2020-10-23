package woogie.space.messenger.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e("MyFirebaseMessagingService", "Refreshed token: $token")
    }
}