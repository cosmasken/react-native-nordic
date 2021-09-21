package com.harambeeapps.blekotlin

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.cosmasken.reactnativenordicble.GattService
import com.cosmasken.reactnativenordicble.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.ktx.BuildConfig

class MainActivity : AppCompatActivity() {

    private val defaultScope = CoroutineScope(Dispatchers.Default)

    private val mainHandler = Handler(Looper.getMainLooper())

    private var gattServiceConn: GattServiceConn? = null

    private var gattServiceData: GattService.DataPlane? = null

    private val myCharacteristicValueChangeNotifications = Channel<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the UI so that notifications we receive are rendered

        val gattCharacteristicValue = findViewById<TextView>(R.id.textViewGattCharacteristicValue)

        defaultScope.launch {
            for (newValue in myCharacteristicValueChangeNotifications) {
                mainHandler.run {
                    gattCharacteristicValue.text = newValue
                }
            }
        }

        // Startup our Bluetooth GATT service explicitly so it continues to run even if
        // this activity is not in focus
        startForegroundService(Intent(this, GattService::class.java))
    }

    override fun onStart() {
        super.onStart()

        val latestGattServiceConn = GattServiceConn()
        if (bindService(Intent(GattService.DATA_PLANE_ACTION, null, this, GattService::class.java), latestGattServiceConn, 0)) {
            gattServiceConn = latestGattServiceConn
        }
    }

    override fun onStop() {
        super.onStop()

        /*if (gattServiceConn != null) {
            unbindService(gattServiceConn!!)
            gattServiceConn = null
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()

        // We only want the service around for as long as our app is being run on the device
        //stopService(Intent(this, GattService::class.java))
    }

    private inner class GattServiceConn : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            if (BuildConfig.DEBUG && GattService::class.java.name != name?.className) {
                error("Disconnected from unknown service")
            } else {
                gattServiceData = null
            }
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (BuildConfig.DEBUG && GattService::class.java.name != name?.className)
                error("Connected to unknown service")
            else {
                gattServiceData = service as GattService.DataPlane

                gattServiceData?.setMyCharacteristicChangedChannel(myCharacteristicValueChangeNotifications)
            }
        }
    }
}
