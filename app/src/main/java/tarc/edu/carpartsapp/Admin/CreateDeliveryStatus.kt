package tarc.edu.carpartsapp.Admin


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import tarc.edu.carpartsapp.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.databinding.FragmentCreateDeliveryStatusBinding
import java.text.SimpleDateFormat
import java.util.*


class CreateDeliveryStatus : Fragment() {
    private var _binding: FragmentCreateDeliveryStatusBinding? = null
    private lateinit var deliveryStatusNew: HashMap<String, String>
    private lateinit var db : DatabaseReference
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val items = arrayListOf<MyOrderModel>() //Order


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deliveryStatusNew = hashMapOf()

        _binding = FragmentCreateDeliveryStatusBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        val inputData = args?.get("name")
       // val orderId = binding.editTextTextMultiLine
        //binding.editTextTextMultiLine.setText(inputData.toString())
        //var date = binding.textViewDate.text
        val dateNow = Calendar.getInstance().time
        val deliveryStatus = binding.editTextTextMultiLine.text.toString()
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)
        binding.outputCurrentDate.text = saveCurrentDate

        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("OrderItem(Cash On Delivery) Duplicate")
        val key = ref.key.toString()


        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val databaseReference = database.getReference("OrderItem(Cash On Delivery) Duplicate")
        //val key = databaseReference.push().key.toString()
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
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

                    val spinner = binding.spinnerOrder
                    val arrayAdapter = activity?.let {
                        ArrayAdapter<String>(
                            it,
                            android.R.layout.simple_spinner_item,
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
                            //val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            //val ref = database.getReference("OrderItem(Cash On Delivery) Duplicate")
                            ref.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snap in snapshot.children) {
                                        for (snap2 in snap.children) {
                                            if (snap2.child("orderID").value.toString()
                                                    .equals(binding.spinnerOrder.selectedItem.toString())
                                            ) {
                                                val userID = snap2.child("userId")
                                                    .getValue(String::class.java)
                                                binding.outputUserid.setText(userID)
                                                val dateOfOrder =
                                                    snap2.child("DateOfOrderPlaced").value as String
                                                binding.outputDateOrder.text = dateOfOrder
                                                val deliveryAddress =
                                                    snap2.child("DeliveryAddress").value as String
                                                binding.outputCustDeliveryAddress.text =
                                                    deliveryAddress
                                                val totalAmount =
                                                    snap2.child("totalAmount").value as String
                                                binding.outputOrderAmount.text = totalAmount
//                                                val url = snap2.child("img_url").value as String
//                                                binding.outputImgUrl.text = url
//                                                val productNames =
//                                                    snap2.child("name").value as String
//                                                binding.outputProductNames.text = productNames
                                               // val id = snap2.child("id").value as String
                                                //binding.outputId.text = id

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

        binding.buttonCreate.setOnClickListener {
            //deliveryStatuss()
            try{
                if(validate()){
                    storeDeliveryStatus()
                    deleteOrderId()
                }
        }catch (e: NullPointerException){
        }
        }
    }


    private fun validate():Boolean{
        val delivery = binding.editTextTextMultiLine.text

        if(delivery.isNullOrEmpty()){
            Toast.makeText(context, "Please enter the delivery status", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun deleteOrderId() {
        val spinners = binding.spinnerOrder
        val selectedId = spinners.selectedItem.toString()
        //val spinner = spinners.selectedItem.toString()

        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("OrderItem(Cash On Delivery) Duplicate")
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val key2 = snap.key.toString()
                    //for (snap2 in snap.children) {
                        // val uniqueId = snap.child("")
                        // val orderId = snap.child("orderId").getValue(String::class.java)
                        if (key2.equals(spinners.selectedItem.toString())
                        ) {
                            snap.ref.removeValue()
                        }
                  //  }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun deliveryStatuss() {
    }

    private fun storeDeliveryStatus() {
        val spinner = binding.spinnerOrder.selectedItem.toString()
        val deliveryStatus = binding.editTextTextMultiLine.text.toString()
        val dateNow = Calendar.getInstance().time


        //val id = binding.outputId.text.toString()
        val database = FirebaseDatabase.getInstance()
        //problem here, wont create delivery status for more than 1 ordered product. In 1 order that has 2 items
        // it creates the delivery for  1 items only
        val databaseReference = database.getReference("Delivery Status").push()
        val key = databaseReference.key

        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("OrderItem(Cash On Delivery) Duplicate")
        ref.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    //Toast.makeText(context, snap.key.toString(), Toast.LENGTH_LONG).show()
                    if(snap.key.toString().equals(spinner)) {
//                        val prodId = snap.child("id").value as String
//                        Toast.makeText(context, prodId, Toast.LENGTH_LONG).show()
                        for(snap2 in snap.children){
                            val key66 = snap2.key.toString()
                              //Toast.makeText(context, key66, Toast.LENGTH_LONG).show()
                            deliveryStatusNew["orderID"] = spinner
                            deliveryStatusNew["deliveryStatus"] = deliveryStatus
                            deliveryStatusNew["dateTime"] = dateNow.toString()
                            deliveryStatusNew["dateOfOrderPlaced"] = snap2.child("DateOfOrderPlaced").value as String
                            deliveryStatusNew["userId"] = binding.outputUserid.text.toString()
                            deliveryStatusNew["address"] = binding.outputCustDeliveryAddress.text.toString()
                            deliveryStatusNew["TotalAmount"] = snap2.child("totalAmount").value as String
                            deliveryStatusNew["img_url"] = snap2.child("img_url").value as String
                            deliveryStatusNew["names"] = snap2.child("name").value as String
                            deliveryStatusNew["quantity"] = snap2.child("quantity").value as String
                            deliveryStatusNew["deliveryStatusId"] = key.toString()
                            deliveryStatusNew["warranty"] = snap2.child("warranty").value as String
                            try {
                                databaseReference.child(key66).setValue(deliveryStatusNew)
                                findNavController().navigate(R.id.action_createDeliveryStatus_to_fragmentAdminDeliveryManagement)
                            } catch (e: Exception) {
                                   //Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
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



