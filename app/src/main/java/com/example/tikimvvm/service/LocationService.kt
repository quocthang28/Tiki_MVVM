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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tikimvvm.R
import com.example.tikimvvm.db.TikiDatabase
import com.example.tikimvvm.db.entity.UserLocation
import com.example.tikimvvm.view.MainActivity
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class LocationService : Service() {

    private var isServiceStarted = false
    private lateinit var client: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var locationManager: LocationManager
    private lateinit var gnssCallback: GnssStatus.Callback

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var numOfSatellites = 0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
            }
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            gnssCallback = object : GnssStatus.Callback() {
                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    super.onSatelliteStatusChanged(status)
                    numOfSatellites = status.satelliteCount
                }
            }

            locationManager.registerGnssStatusCallback(gnssCallback)
        }

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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun stopService() {
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        isServiceStarted = false
        client.removeLocationUpdates(locationCallback)
        locationManager.unregisterGnssStatusCallback(gnssCallback)
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
                    writeToLocal(UserLocation(0, latitude, longitude, addresses[0].getAddressLine(0), numOfSatellites, 0.0))
                }
            }

            client.requestLocationUpdates(request, locationCallback, null)
        }
    }

//    private fun calculateHDOP() {
//        val accuracy = locationManager.get
//    }

    private fun writeToLocal(userLocation: UserLocation) {
        val dao = TikiDatabase.getInstance(applicationContext).userLocationDAO
        CoroutineScope(Dispatchers.Main).launch {
            dao.insertUserLocation(userLocation)
        }
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