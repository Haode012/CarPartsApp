package tarc.edu.carpartsapp.Customer

import android.app.AlertDialog
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import tarc.edu.carpartsapp.Adapter.MyCartAdapter
import tarc.edu.carpartsapp.Adapter.PopularAdapterAdmin
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentMyCartBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyCartFragment : Fragment() {

    private lateinit var totalAmount: TextView
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myCartModelArrayList : ArrayList<MyCartModel>
    private var _binding: FragmentMyCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCashOnDelivery.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null)
            val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = "Are you sure you want to order?\nYou are not allow to make any changes after ordered"
            messageTextView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))

            builder.setTitle("Order")
                .setView(view)
                .setPositiveButton("Confirm"){a,d->
                    saveData()
                }
                .setNegativeButton("Cancel"){a,d->
                    a.dismiss()
                }
                .show()
        }

        binding.buttonCreditCard.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null)
            val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = "Are you sure you want to order?\nYou are not allow to make any changes after ordered"
            messageTextView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))

            builder.setTitle("Order")
                .setView(view)
                .setPositiveButton("Confirm"){a,d->
                    saveDataCreditCard()
                }
                .setNegativeButton("Cancel"){a,d->
                    a.dismiss()
                }
                .show()
        }

        totalAmount = view.findViewById(R.id.textViewTotalAmount)

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
        myCartModelArrayList = arrayListOf<MyCartModel>()

        getData()
    }

    private fun saveDataCreditCard() {
        findNavController().navigate(R.id.action_nav_myCart_customer_to_nav_creditCard_customer)
    }

    private fun saveData() {
        // Store data in SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(myCartModelArrayList)
        Log.d("JSON", json)
        editor.putString("itemList", json)
        editor.apply()

        findNavController().navigate(R.id.action_nav_myCart_customer_to_nav_placedOrder_customer)
    }

    private fun getData(){


        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CartItem")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (myCartSnapshot in snapshot.children) {
                            val myCart = myCartSnapshot.getValue(MyCartModel::class.java)
                            myCartModelArrayList.add(myCart!!)
                        }
                        val myCartAdapter = MyCartAdapter(myCartModelArrayList, requireContext())
                        recyclerView.adapter = myCartAdapter
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