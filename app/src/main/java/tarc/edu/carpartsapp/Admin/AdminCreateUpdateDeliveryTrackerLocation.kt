package tarc.edu.carpartsapp.Admin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ActivityAdminCreateUpdateDeliveryTrackerLocationBinding
import java.io.IOException
import java.lang.Exception
import java.util.*


class AdminCreateUpdateDeliveryTrackerLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var binding: ActivityAdminCreateUpdateDeliveryTrackerLocationBinding
    private lateinit var orderDeliveryLocation: HashMap<String, String>
    private lateinit var db: DatabaseReference
    var currentMarker: Marker? = null
    var currentLocation: Location? = null
    private var latitude = 0.000
    private var longitude = 0.0

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(this, Locale.getDefault()) // initialise the geocoder
        binding = ActivityAdminCreateUpdateDeliveryTrackerLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        orderDeliveryLocation = HashMap()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        fetchLocation()
        val database =
            Firebase.database("https://mapstesting-4184b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Order Locations")

       // val orderId = intent.getStringExtra("orderID").toString()
        //Toast.makeText(this, orderId, Toast.LENGTH_LONG).show()

        binding.btnSavelocation.setOnClickListener {
            storeLocations()
        }

        binding.buttonExitMap.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun storeLocations(){
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
            .child("Delivery Tracker Locations")
        val orderId = intent.getStringExtra("orderID").toString()
        val userId = intent.getStringExtra("uid").toString()
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val key = snap.key.toString()
                    if (key.equals(orderId)) {
//                        for(snap2 in snap.children) {
//                            for (snap3 in snap2.children) {
//                                var count = snap3.childrenCount
                        var count = 0
                        count++
                        orderDeliveryLocation["latitude"] =
                            latitude.toString()
                        orderDeliveryLocation["longitude"] =
                            longitude.toString()
                        val locationn = "Location" + count.toString()
                        //orderDeliveryLocation["location"] = newlatLng.toString()
                        try {
                        db.child(key).child(userId).push()
                            .setValue(orderDeliveryLocation)
                            Toast.makeText(this@AdminCreateUpdateDeliveryTrackerLocation, "Location Added", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@AdminCreateUpdateDeliveryTrackerLocation, e.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@AdminCreateUpdateDeliveryTrackerLocation, "Not Matching Order",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, currentLocation!!.latitude.toString() + "" + currentLocation!!.longitude,
                    Toast.LENGTH_SHORT).show()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        moveMarket(latLng)
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            @SuppressLint("SetTextI18n")
            override fun onMarkerDragEnd(marker: Marker) {
                Log.d("====", "latitude : " + marker.position.latitude)

                if (currentMarker != null) {
                    currentMarker?.remove()
                }
                val newlatLng = LatLng(marker.position.latitude, marker.position.longitude)
                latitude = newlatLng.latitude
                longitude = newlatLng.longitude
                binding.outputMarkerLocation.text = newlatLng.toString()
                //Toast.makeText(this@AdminCreateUpdateDeliveryTrackerLocation, newlatLng.toString(), Toast.LENGTH_LONG).show()
                moveMarket(newlatLng)

                //val orderId = intent.getStringExtra("orderID").toString()

                }
            override fun onMarkerDrag(marker: Marker) {}
        })


    }


    private fun moveMarket(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng).title("$latLng")
            .snippet(getTheAddress(latLng!!.latitude, latLng!!.longitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }
    private fun getTheAddress(latitude: Double, longitude: Double): String? {
        var retVal = ""

        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            retVal = addresses?.get(0)?.adminArea +"\n"+ addresses?.get(0)?.countryName
            Toast.makeText(this, retVal, Toast.LENGTH_LONG).show()
            //binding.outputAddress.text = retVal
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retVal
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}