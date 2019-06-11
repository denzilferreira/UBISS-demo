package com.example.myapplication

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aware.*
import com.aware.providers.Accelerometer_Provider

class MainActivity : AppCompatActivity() {

    companion object {
        const val RESPONSE_PERMISSIONS = 12345
        val APP_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            //,...
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        var all_ok = true
        for (permission in APP_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                all_ok = false
                break
            }
        }

        if (!all_ok) {
            ActivityCompat.requestPermissions(this, APP_PERMISSIONS, RESPONSE_PERMISSIONS)
        } else {

            //we can load our sensing
            sensing()
        }
    }

    fun sensing() {
        Aware.startAWARE(this)

        Aware.setSetting(this, Aware_Preferences.FREQUENCY_BLUETOOTH, 60)

        Aware.startBluetooth(this)
        Bluetooth.setSensorObserver(object: Bluetooth.AWARESensorObserver {
            override fun onBLEScanStarted() {
                println("Bluetooth BLE scan started")
            }

            override fun onBluetoothBLEDetected(data: ContentValues?) {
                println("Bluetooth BLE detected: ${data.toString()}")
            }

            override fun onBLEScanEnded() {
                println("Bluetooth BLE scan ended")
            }

            override fun onBluetoothDetected(data: ContentValues?) {
                println("Bluetooth detected: ${data.toString()}")
            }

            override fun onBluetoothDisabled() {
                println("Bluetooth disabled")
            }

            override fun onScanStarted() {
                println("Blueotooth scan started")
            }

            override fun onScanEnded() {
                println("Bluetooth scan ended")
            }
        })

        Aware.startAccelerometer(this)
        Accelerometer.setSensorObserver {

            val x = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_0)
            val y = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_1)
            val z = it.getAsDouble(Accelerometer_Provider.Accelerometer_Data.VALUES_2)

            //println("x = $x y = $y, z = $z")
        }

        Aware.startBattery(this)
        Battery.setSensorObserver(object : Battery.AWARESensorObserver {
            override fun onBatteryChanged(data: ContentValues?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBatteryCharging() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPhoneReboot() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPhoneShutdown() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBatteryDischarging() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBatteryLow() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        Aware.startCommunication(this)
        Communication.setSensorObserver(object : Communication.AWARESensorObserver{
            override fun onCall(data: ContentValues?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessage(data: ContentValues?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBusy(number: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onRinging(number: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFree(number: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }



    override fun onDestroy() {
        super.onDestroy()

        Aware.stopAccelerometer(this)
        Aware.stopBluetooth(this)
        Aware.stopAWARE(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this, APP_PERMISSIONS, RESPONSE_PERMISSIONS)
        }
    }
}
