package tarc.edu.carpartsapp

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Customer.CustomerActivity
import tarc.edu.carpartsapp.databinding.ActivityDeliveryTrackerMapsBinding
import java.util.*

class DeliveryTrackerMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var binding: ActivityDeliveryTrackerMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(this, Locale.getDefault()) // initialise the geocoder
        binding = ActivityDeliveryTrackerMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.buttonExit.setOnClickListener {
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true


        val orderId = intent.getStringExtra("orderID").toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val databaseReference = database.getReference("Delivery Tracker Locations")
        //val key = databaseReference.push().key.toString()
        var countLocation = 1
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    var count = snap.childrenCount
                    // val key = snap.key.toString()
                    val orderID = snap.child("orderID").value as String
                    for (snap2 in snap.children) {
                        val uuid = snap2.key.toString()
                        for (snap3 in snap2.children) {
                            if (orderID.equals(orderId)) {
                                if (userId.equals(uuid)) {
                                    val latitude = snap3.child("latitude").value as String
                                    val longitude = snap3.child("longitude").value as String
                                    val orderLocation =
                                        LatLng(latitude.toDouble(), longitude.toDouble())

                                    var addresses: String?
                                    geocoder = Geocoder(applicationContext, Locale.getDefault())
                                    val locations = geocoder.getFromLocation(
                                        orderLocation.latitude,
                                        orderLocation.longitude,
                                        10
                                    )
                                    addresses = locations?.get(0)?.adminArea // get city name
                                    if (countLocation.toLong() != count) {
                                        mMap.addMarker(
                                            MarkerOptions().position(orderLocation)
                                                .title("Location of your order " + countLocation + " is at " + addresses)
                                        )
                                    } else {
                                        mMap.addMarker(
                                            MarkerOptions().position(orderLocation)
                                                .title("Current Location of your order is at " + addresses)
                                        )
                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(orderLocation))
                                    mMap.setMaxZoomPreference(20.0f)
                                    countLocation++
                                }
                            }
                        }
                    }

                }
            }







            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }
}