package com.example.tikimvvm.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tikimvvm.R
import com.example.tikimvvm.view.MainActivity
import com.google.android.gms.location.*
import java.util.*


class LocationService : Service() {

    private var isServiceStarted = false
    private lateinit var client: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var gnssStatusCallback: GnssStatus.Callback
    private var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        } else {
            print(
                    "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()
        client = LocationServices.getFusedLocationProviderClient(this)
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun startService() {
        if (isServiceStarted) return
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        startLocationUpdate()
    }

    private fun stopService() {
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        isServiceStarted = false
        client.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdate() {
        val request = LocationRequest.create().apply {
            interval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.i("myTag", "getting location data every 10 secs")
                    val location: Location = locationResult.lastLocation
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.i("myTag", "lat: $latitude, long: $longitude")
                    val gcd = Geocoder(baseContext, Locale.getDefault())
                    val addresses: List<Address> = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    Log.i("myTag", addresses[0].getAddressLine(0).toString())
                    
                }
            }

            client.requestLocationUpdates(request, locationCallback, null)
        }
    }

    private fun writeToLocal() {

    }

    private fun createNotification(): Notification {
        val notificationChannelId = "LOCATION SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                    notificationChannelId,
                    "Location Service notifications channel",
                    NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Location Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
                Intent(this, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }

        val builder: Notification.Builder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                        this,
                        notificationChannelId
                ) else Notification.Builder(this)

        return builder
                .setContentTitle("Location Service")
                .setContentText("location service is working")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Ticker text")
                .build()
    }

}