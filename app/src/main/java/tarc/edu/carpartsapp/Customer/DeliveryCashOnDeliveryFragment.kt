package tarc.edu.carpartsapp.Customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.MyCartAdapter
import tarc.edu.carpartsapp.Adapter.MyOrderCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentDeliveryCashOnDeliveryBinding

class DeliveryCashOnDeliveryFragment : Fragment() {

    private lateinit var totalAmount: TextView
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myOrderModelArrayList : ArrayList<MyOrderModel>
    private var _binding: FragmentDeliveryCashOnDeliveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDeliveryCashOnDeliveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalAmount = view.findViewById(R.id.textViewTotalAmount2)

        val messageReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val totalBill = intent?.getDoubleExtra("totalAmount", 0.00)
                val totalItemsBill = String.format("%.2f", totalBill)
                totalAmount.text = totalItemsBill
            }
        }

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(messageReceiver, IntentFilter("totalAmount"))

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        myOrderModelArrayList = arrayListOf<MyOrderModel>()

        getData()

        binding.buttonViewDeliveryStatus.setOnClickListener {
            //findNavController().navigate(R.id.action_nav_delivery_cash_on_delivery_customer_to_fragmentCustSelectOrderCashOnDeliveryViewDeliveryTracker2)

            val deliveryAddress = binding.editTextDeliveryAddress.text.toString().trim()

            //validation
            if (deliveryAddress.isEmpty()) {
                binding.editTextDeliveryAddress.error = "Delivery address cannot be blank"
            }

        }
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderItem(Cash On Delivery)").child(
            FirebaseAuth.getInstance().currentUser!!.uid)

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (firstChildSnapshot in snapshot.children) {
                            for (secondChildSnapshot in firstChildSnapshot.children) {
                                val myOrder = secondChildSnapshot.getValue(MyOrderModel::class.java)
                                myOrderModelArrayList.add(myOrder!!)

                                // get address and display from database
                                val address = secondChildSnapshot.child("deliveryAddress").value as String
                                binding.editTextDeliveryAddress.setText(address)
                            }
                        }
                        val context = context
                        if (context != null) {
                            val myOrderAdapter = MyOrderCashOnDeliveryAdapter(
                                myOrderModelArrayList,
                               context
                            )
                            recyclerView.adapter = myOrderAdapter
                        }
                    }
                } catch (e: Exception) {
                    Log.e("getData", "Error getting data from Firebase: ${e.message}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}