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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.MyOrderCreditCardAdapter
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentMyOrderCreditCardBinding

class MyOrderCreditCardFragment : Fragment() {

    private lateinit var totalAmount: TextView
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myOrderModelArrayList2 : ArrayList<MyOrderModel>
    private var _binding: FragmentMyOrderCreditCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyOrderCreditCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalAmount = view.findViewById(R.id.textViewTotalAmount3)

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
        myOrderModelArrayList2 = arrayListOf<MyOrderModel>()

        getData()
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PaymentDetails")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (firstChildSnapshot in snapshot.children) {
                            for (secondChildSnapshot in firstChildSnapshot.children) {
                                val myOrder = secondChildSnapshot.getValue(MyOrderModel::class.java)
                                myOrderModelArrayList2.add(myOrder!!)
                            }
                        }
                        val myOrderAdapter = MyOrderCreditCardAdapter(myOrderModelArrayList2, requireContext())
                        recyclerView.adapter = myOrderAdapter
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