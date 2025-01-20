package tarc.edu.carpartsapp

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.databinding.FragmentCreateDeliveryStatusBinding
import tarc.edu.carpartsapp.databinding.FragmentCreateDeliveryStatusCreditCardPaymentBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentCreateDeliveryStatusCreditCardPayment : Fragment() {
    private var _binding: FragmentCreateDeliveryStatusCreditCardPaymentBinding? = null
    private lateinit var deliveryStatusNewCreditCard: HashMap<String, String>
    private lateinit var db : DatabaseReference
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val items = arrayListOf<MyOrderModel>() //Order


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deliveryStatusNewCreditCard = hashMapOf()

        _binding = FragmentCreateDeliveryStatusCreditCardPaymentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        val inputData = args?.get("name")
        // val orderId = binding.editTextTextMultiLine
       // binding.editTextTextMultiLine.setText(inputData.toString())
        //var date = binding.textViewDate.text
        val dateNow = Calendar.getInstance().time
        //val deliveryStatus = binding.editTextTextMultiLine.text.toString()
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)
        //binding.outputCurrentDate.text = saveCurrentDate

        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("PaymentDetails Duplicate")
        val key = ref.key.toString()

//        ref.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    for (snap in snapshot.children) {
//                        if(snap.child("orderID").value.toString().equals(binding.spinnerOrder.selectedItem.toString())) {
//                            val userID = snap.child("userId")
//                                .getValue(String::class.java)
//                            binding.outputUserid.setText(userID)
//                            val dateOfOrder = snap.child("Date of Order Placed").value as String
//                            binding.outputDateOrder.text = dateOfOrder
//                            val deliveryAddress = snap.child("Delivery Address").value as String
//                            binding.outputCustDeliveryAddress.text = deliveryAddress
//
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val databaseReference = database.getReference("PaymentDetails Duplicate")
        //val key = databaseReference.push().key.toString()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders: ArrayList<String?> = ArrayList()
                if (snapshot.exists()) {
                    for(snap in snapshot.children) {
                        val keys = snap.key.toString()
                        orders.add(keys
                            //snap.child("orderID")
                            // .getValue(String::class.java)
                        )

                    }



                    // val order = orderSnap.getValue(Order::class.java)
                    //  val orders: ArrayList<String?> = ArrayList()

                    val spinner = binding.spinnerCreditCardOrderId
                    val arrayAdapter = activity?.let {
                        ArrayAdapter<String>(
                            it,
                            R.layout.simple_spinner_item,
                            orders
                        )
                    }
                    spinner.adapter = arrayAdapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            val databases = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            val ref2 = databases.getReference("PaymentDetails Duplicate")
                            ref2.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snap in snapshot.children) {
                                        for (snap2 in snap.children) {
                                            if (snap2.child("orderID").value.toString()
                                                    .equals(binding.spinnerCreditCardOrderId.selectedItem.toString())
                                            ) {
//                                                val userID = snap2.child("userId")
//                                                    .getValue(String::class.java)
//                                                binding.outputUserid.setText(userID)
//                                                val dateOfOrder =
//                                                    snap2.child("DateOfOrderPlaced").value as String
//                                                binding.outputDateOrder.text = dateOfOrder
//                                                val deliveryAddress =
//                                                    snap2.child("DeliveryAddress").value as String
//                                                binding.outputCustDeliveryAddress.text =
//                                                    deliveryAddress
//                                                val totalAmount =
//                                                    snap2.child("totalAmount").value as String
//                                                binding.outputOrderAmount.text = totalAmount
//                                                val url = snap2.child("img_url").value as String
//                                                binding.outputImgUrl.text = url
//                                                val productNames =
//                                                    snap2.child("name").value as String
//                                                binding.outputProductNames.text = productNames
//                                                // val id = snap2.child("id").value as String
//                                                //binding.outputId.text = id

                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                        }


                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        // hide some details to not view on layout
//        if(binding.outputImgUrl.getVisibility() == View.INVISIBLE){
//        }
//        if(binding.outputProductNames.getVisibility() == View.INVISIBLE){
//        }

        binding.btnCreateDeliveryStatus2.setOnClickListener {
            try {
                if (validate()) {
                    storeDeliveryStatus()
                    deleteOrderId()
                }
                //deliveryStatuss()
                //    deleteOrderId()
            }catch (e: NullPointerException){

            }
        }


        //val orders: List<String> = ArrayList()
        // databaseReference.get().addOnSuccessListener {
        //    val orderId: String? = it.child("orderId").getValue(String::class.java)
        //    val orders: MutableList<String?> = ArrayList()
        //val orderId = it.child("fullName").value as String
        //    orders.add(orderId)

        //val orderList = Order(orderId)
        // items.add(orderList)


        // val spinner = binding.spinnerOrder
        //var languages = arrayOf("fuck","sex")

        //val adapter = ArrayAdapter(
        //   this@CreateDeliveryStatus,android.R.layout.simple_spinner_item,
        //    languages)
        // adapter

        //  val arrayAdapter = activity?.let {
        //     ArrayAdapter<String>(
        //         it,
        //        android.R.layout.simple_spinner_item,
        //         orders
        //    )
        // }
        //      spinner.adapter = arrayAdapter

        //   spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        //     override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //         Toast.makeText(context, "Thank You", Toast.LENGTH_SHORT).show()
        //      }

        //     override fun onNothingSelected(p0: AdapterView<*>?) {
        //        TODO("Not yet implemented")
        //    }
        // }
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    items.add(it.key!!)
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        }
//        databaseReference.addValueEventListener(listener)
    }


    private fun deleteOrderId() {
        val spinners = binding.spinnerCreditCardOrderId
        val selectedId = spinners.selectedItem.toString()
        //val spinner = spinners.selectedItem.toString()

        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("PaymentDetails Duplicate")
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    for (snap2 in snap.children) {
                        // val uniqueId = snap.child("")
                        // val orderId = snap.child("orderId").getValue(String::class.java)
                        if (snap2.child("orderID").value.toString()
                                .equals(spinners.selectedItem.toString())
                        ) {
                            snap.ref.removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun validate():Boolean{
        val delivery = binding.deliveryStatusMessage.text

        if(delivery.isNullOrEmpty()){
            Toast.makeText(context, "Please enter the delivery status", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun deliveryStatuss() {
    }

    private fun storeDeliveryStatus() {
        val spinner = binding.spinnerCreditCardOrderId.selectedItem.toString()
        val deliveryStatus = binding.deliveryStatusMessage.text.toString()
        val dateNow = Calendar.getInstance().time


       // val id = binding.outputId.text.toString()
        val database = FirebaseDatabase.getInstance()
        //problem here, wont create delivery status for more than 1 ordered product. In 1 order that has 2 items
        // it creates the delivery for  1 items only
        val databaseReference = database.getReference("Delivery Status(Credit Card Payment)").push()
        val key = databaseReference.key

        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("PaymentDetails Duplicate")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    //Toast.makeText(context, snap.key.toString(), Toast.LENGTH_LONG).show()
                    if(snap.key.toString().equals(spinner)) {
//                        val prodId = snap.child("id").value as String
//                        Toast.makeText(context, prodId, Toast.LENGTH_LONG).show()
                        for(snap2 in snap.children){
                            val key66 = snap2.key.toString()
                            //Toast.makeText(context, key66, Toast.LENGTH_LONG).show()
                            deliveryStatusNewCreditCard["orderID"] = spinner
                            deliveryStatusNewCreditCard["deliveryStatus"] = deliveryStatus
                            deliveryStatusNewCreditCard["dateTime"] = dateNow.toString()
                            deliveryStatusNewCreditCard["userId"] = snap2.child("userId").value as String
                            deliveryStatusNewCreditCard["address"] = snap2.child("DeliveryAddress").value as String
                            deliveryStatusNewCreditCard["TotalAmount"] = snap2.child("totalAmount").value as String
                            deliveryStatusNewCreditCard["img_url"] = snap2.child("img_url").value as String
                            deliveryStatusNewCreditCard["names"] = snap2.child("name").value as String
                            deliveryStatusNewCreditCard["quantity"] = snap2.child("quantity").value as String
                            deliveryStatusNewCreditCard["deliveryStatusId"] = key.toString()
                            deliveryStatusNewCreditCard["warranty"] = snap2.child("warranty").value as String
                            try {
                                databaseReference.child(key66).setValue(deliveryStatusNewCreditCard)
                            } catch (e: Exception) {
                                //   Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            //}

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        //val databaseReference = database.getReference("Feedback").child("$userId").push()
        //val databaseReference = database.getReference("Feedback").child("$userId").child("Feedback").push()
    }
}