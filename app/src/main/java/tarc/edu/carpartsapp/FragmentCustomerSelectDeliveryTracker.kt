package tarc.edu.carpartsapp

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentCustomerSelectDeliveryTrackerBinding

class FragmentCustomerSelectDeliveryTracker : Fragment() {
    private var _binding: FragmentCustomerSelectDeliveryTrackerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Tracker Locations")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders: ArrayList<String?> = ArrayList()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val userIdDatabase = snap.child("userId").value as String
                        if (userId.equals(userIdDatabase)) {
                            val orderIds = snap.key.toString()
                            orders.add(orderIds)
                            // }

                        }
                    }
                    val spinner = binding.spinnerDelivery
                    val arrayAdapter = activity?.let {
                        ArrayAdapter<String>(
                            it,
                            R.layout.simple_spinner_item,
                            orders
                        )
                    }
                    spinner.adapter = arrayAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        _binding = FragmentCustomerSelectDeliveryTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonYes.setOnClickListener {
            try {
                val intent = Intent(context, DeliveryTrackerMapsActivity::class.java)
                intent.putExtra("orderID", binding.spinnerDelivery.selectedItem.toString())
                startActivity(intent)
            } catch (e: NullPointerException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}