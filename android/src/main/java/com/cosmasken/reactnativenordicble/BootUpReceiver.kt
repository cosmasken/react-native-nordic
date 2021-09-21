package com.cosmasken.reactnativenordicble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootUpReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action === Intent.ACTION_BOOT_COMPLETED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //log("Starting the service in >=26 Mode from a BroadcastReceiver")
                context.startForegroundService(Intent(context, GattService::class.java))
                return
            }
            //log("Starting the service in < 26 Mode from a BroadcastReceiver")
            context.startService(Intent(context, GattService::class.java))
        }
    }
}