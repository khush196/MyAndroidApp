package com.khush.devicemapper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location // Import Android's Location class
import android.os.Build
import android.os.Bundle
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await // For GMS tasks

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback, DeviceScanner.ScanListener {
    private lateinit var googleMap: GoogleMap
    private lateinit var deviceScanner: DeviceScanner
    private val scannedDevicesThisSession = mutableListOf<ScannedDevice>() // Store devices from current scan
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null

    private var activeScanTypes = mutableSetOf<String>() // To track "BLE" or "LAN"

    companion object {
        private const val TAG = "MainActivity"
    }

    private val requiredPermissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        // Add other permissions if needed, e.g., Manifest.permission.ACCESS_WIFI_STATE for subnet
        add(Manifest.permission.ACCESS_WIFI_STATE) // For getting Wi-Fi info for subnet
    }.toTypedArray()

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var allPermissionsGranted = true
            permissions.entries.forEach {
                Log.d(TAG, "Permission ${it.key} granted: ${it.value}")
                if (!it.value) {
                    allPermissionsGranted = false
                }
            }
            if (allPermissionsGranted) {
                Log.d(TAG, "All permissions granted.")
                initializeMapAndScanner()
                fetchLastLocation() // Fetch location once permissions are granted
            } else {
                Log.w(TAG, "Some permissions were denied.")
                toast("Some permissions were denied. App functionality may be limited.")
                // Optionally, disable scan button or show a more prominent message
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        deviceScanner = DeviceScanner(this) // Initialize here

        val startScanButton = findViewById<Button>(R.id.btn_start_scan)
        startScanButton.setOnClickListener {
            if (areAllPermissionsGranted()) {
                performFullScan("All") // Example: scan for all types
            } else {
                toast("Permissions required to start scan. Please grant permissions.")
                checkAndRequestAllPermissions() // Request again if not granted
            }
        }

        setupToolbarAndDrawer()
        checkAndRequestAllPermissions()
    }

    private fun setupToolbarAndDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_filter -> { /* showFilterDialog() */ }
                R.id.nav_export -> { /* export code */ }
                R.id.nav_history -> {
                    startActivity(Intent(this, ScanHistoryActivity::class.java))
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun checkAndRequestAllPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            Log.d(TAG, "Requesting permissions: ${permissionsToRequest.joinToString()}")
            requestMultiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            Log.d(TAG, "All required permissions are already granted.")
            initializeMapAndScanner()
            fetchLastLocation()
        }
    }

    private fun areAllPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initializeMapAndScanner() {
        Log.d(TAG, "Initializing map and scanner.")
        // DeviceScanner is already initialized in onCreate
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this) ?: Log.e(TAG, "MapFragment not found")
    }

    @SuppressLint("MissingPermission") // Permissions are checked by areAllPermissionsGranted
    private fun fetchLastLocation() {
        if (!areAllPermissionsGranted()) {
            Log.w(TAG, "Location permission not granted, cannot fetch location.")
            return
        }
        lifecycleScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    lastKnownLocation = location
                    Log.d(TAG, "Fetched last location: Lat ${location.latitude}, Lon ${location.longitude}")
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
                } else {
                    Log.w(TAG, "Last location is null.")
                    toast("Could not get current location. Please ensure GPS is enabled.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching location: ${e.message}", e)
                toast("Error getting location.")
            }
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.d(TAG, "GoogleMap is ready.")
        if (areAllPermissionsGranted()) {
            try {
                googleMap.isMyLocationEnabled = true
            } catch (se: SecurityException){
                Log.e(TAG, "SecurityException setting my location enabled on map: ${se.message}")
            }
            fetchLastLocation() // Fetch and move camera once map is ready and permissions are granted
        } else {
            Log.w(TAG, "Permissions not granted when map is ready.")
        }
        // You can also set map settings here, like map type, UI settings etc.
        // googleMap.uiSettings.isZoomControlsEnabled = true
    }


    private fun performFullScan(filter: String) {
        if (!areAllPermissionsGranted()) {
            toast("Permissions are required to scan.")
            Log.w(TAG, "Scan attempt failed: Permissions not granted.")
            checkAndRequestAllPermissions() // Prompt for permissions again
            return
        }

        if (lastKnownLocation == null) {
            toast("Current location not available. Please ensure GPS is enabled and try again.")
            fetchLastLocation() // Try to fetch it again
            Log.w(TAG, "Scan attempt failed: Location not available.")
            return
        }

        if (activeScanTypes.isNotEmpty()) {
            toast("Scan already in progress...")
            Log.d(TAG, "Scan attempt while another scan is active: $activeScanTypes")
            return
        }

        Log.d(TAG, "Starting full scan with filter: $filter")
        scannedDevicesThisSession.clear()
        googleMap.clear() // Clear previous markers
        displayMarkersOnMap(emptyList()) // Update UI if needed

        var scanInitiated = false

        if (filter == "All" || filter == "Bluetooth") {
            Log.d(TAG, "Initiating BLE scan.")
            activeScanTypes.add("BLE")
            deviceScanner.scanBleDevices(this)
            scanInitiated = true
        }

        if (filter == "All" || filter == "WiFi") {
            val subnet = getSubnet() // Ensure this function works correctly
            if (subnet != null) {
                Log.d(TAG, "Initiating LAN scan for subnet: $subnet")
                activeScanTypes.add("LAN")
                lifecycleScope.launch { // LAN scan is a suspend function
                    deviceScanner.scanLocalNetwork(subnet, this@MainActivity)
                }
                scanInitiated = true
            } else {
                toast("Could not determine Wi-Fi subnet.")
                Log.w(TAG, "Could not get subnet for LAN scan.")
            }
        }

        if (scanInitiated) {
            toast("Scanning started...")
        } else {
            toast("No scan type selected or runnable.")
        }
    }

    // --- DeviceScanner.ScanListener Implementation ---

    override fun onDeviceFound(device: ScannedDevice) {
        Log.d(TAG, "Device found: ${device.name} (${device.type})")
        lastKnownLocation?.let {
            val updatedDevice = device.copy(latitude = it.latitude, longitude = it.longitude, timestamp = System.currentTimeMillis())
            scannedDevicesThisSession.add(updatedDevice)

            // Save to database immediately
            lifecycleScope.launch(Dispatchers.IO) {
                val dao = AppDatabase.getInstance(this@MainActivity).scanRecordDao()
                dao.insert(ScanRecord(
                    type = updatedDevice.type,
                    name = updatedDevice.name,
                    identifier = updatedDevice.identifier,
                    timestamp = updatedDevice.timestamp ?: System.currentTimeMillis(),
                    latitude = updatedDevice.latitude,
                    longitude = updatedDevice.longitude
                ))
                Log.d(TAG,"Device saved to DB: ${updatedDevice.name}")
            }

            // Update map markers
            displayMarkersOnMap(scannedDevicesThisSession)
            // Optionally, update a bottom sheet or list view
            // showScannedDevicesBottomSheet(scannedDevicesThisSession)
        } ?: Log.w(TAG, "Location not available when device found, cannot assign coordinates.")
    }

    override fun onScanFinished(type: String) {
        Log.d(TAG, "$type scan finished.")
        activeScanTypes.remove(type)
        if (activeScanTypes.isEmpty()) {
            toast("All scans finished. Found ${scannedDevicesThisSession.size} devices.")
            Log.d(TAG, "All scans finished. Total devices this session: ${scannedDevicesThisSession.size}")
            // Perform any final actions after all scans are done
        }
    }

    override fun onScanFailed(type: String, message: String) {
        Log.e(TAG, "$type scan failed: $message")
        activeScanTypes.remove(type)
        toast("$type scan failed: $message")
        if (activeScanTypes.isEmpty()) {
            Log.d(TAG, "All scans finished (possibly with failures).")
            // Perform any final actions
        }
    }



    private fun displayMarkersOnMap(devices: List<ScannedDevice>) {
        googleMap.clear() // Clear existing markers before adding new ones
        devices.forEach { device ->
            if (device.latitude != null && device.longitude != null) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(device.latitude!!, device.longitude!!))
                        .title("${device.type}: ${device.name ?: device.identifier}")
                )
            }
        }
        if (devices.isNotEmpty() && lastKnownLocation != null) {
            // Optionally move camera to the current location or the average location of devices
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15f))
        }
        Log.d(TAG, "Displayed ${devices.size} markers on map.")
    }

    @SuppressLint("DefaultLocale")
    private fun getSubnet(): String? {
        // Basic implementation to get subnet. Requires ACCESS_WIFI_STATE permission.
        // This is a simplified example. Robust subnet detection can be more complex.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo != null) {
                val ipAddress = wifiInfo.ipAddress
                if (ipAddress == 0) return null
                // Convert IP address to string format
                val ipString = String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
                return ipString.substringBeforeLast('.') + ".0/24" // Assumes a /24 subnet mask
            }
        } else {
            Log.w(TAG, "ACCESS_WIFI_STATE permission not granted, cannot get subnet.")
        }
        return null // Fallback or if Wi-Fi not connected
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        deviceScanner.stopAllScans() // Important to stop scans to prevent leaks
        Log.d(TAG, "onDestroy called, stopping all scans.")
    }

    // You would implement showScannedDevicesBottomSheet if needed
    // private fun showScannedDevicesBottomSheet(devices: List<ScannedDevice>) { ... }
}
