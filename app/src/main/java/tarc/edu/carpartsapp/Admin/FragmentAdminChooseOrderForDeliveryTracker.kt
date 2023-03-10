package tarc.edu.carpartsapp.Admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentAdminChooseOrderForDeliveryTrackerBinding
import tarc.edu.carpartsapp.databinding.FragmentCreateDeliveryStatusBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentAdminChooseOrderForDeliveryTracker : Fragment() {

    private var _binding: FragmentAdminChooseOrderForDeliveryTrackerBinding? = null
    private lateinit var db : DatabaseReference
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val uid : String =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminChooseOrderForDeliveryTrackerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Tracker Locations")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders: ArrayList<String?> = ArrayList()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val orderIds = snap.key.toString()
                        val uids = snap.child("userId").value as String
                        binding.outputId.text = uids.toString()
                        orders.add(orderIds)
                    }
                    val spinner = binding.spinnerAllOrderId
                    val arrayAdapter = activity?.let {
                        ArrayAdapter<String>(
                            it,
                            android.R.layout.simple_spinner_item,
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


        binding.btnGoToMaps.setOnClickListener {
            val intent = Intent(context, AdminCreateUpdateDeliveryTrackerLocation::class.java)
            intent.putExtra("orderID", binding.spinnerAllOrderId.selectedItem.toString())
            intent.putExtra("uid", binding.outputId.text.toString() )
            startActivity(intent)
        }
    }
    }