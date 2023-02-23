package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create_delivery_status.*
import tarc.edu.carpartsapp.Adapter.CustomerViewDeliveryStatusAdapter
import tarc.edu.carpartsapp.Adapter.DeliveryStatusProductsAdapter
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.databinding.FragmentCustDeliveryStatusBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentCustDeliveryStatus : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var custDeliveryStatusRecyclerview: RecyclerView
    private lateinit var deliveryStatusProducts: RecyclerView
    private lateinit var custDeliveryStatusArrayList: ArrayList<DeliveryStatus>
    private lateinit var deliveryProductsArrayList: ArrayList<DeliveryStatus>

    private lateinit var deliveryStatusProductAdapter: DeliveryStatusProductsAdapter
    private lateinit var deliveryStatusAdapter: CustomerViewDeliveryStatusAdapter

    private lateinit var deliveredItems: HashMap<String, String>

    private var _binding: FragmentCustDeliveryStatusBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCustDeliveryStatusBinding.inflate(inflater, container, false)
        val args = this.arguments
        val inputData = args?.get("orderID")
        val orderId = binding.outputOrderIdd
        orderId.text = inputData.toString()

        deliveredItems = hashMapOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //products
        deliveryStatusProducts = view.findViewById(R.id.recyclerViewDeliveryStatusCustomer)
        deliveryStatusProducts.layoutManager = LinearLayoutManager(this.context)
        deliveryStatusProducts.setHasFixedSize(true)
        deliveryProductsArrayList = arrayListOf<DeliveryStatus>()
        val args = this.arguments
        val inputData = args?.get("orderID")
        val orderId = binding.outputOrderIdd
        orderId.text = inputData.toString()

        //  val myLinearLayoutManager = object : LinearLayoutManager(context) {
        //   override fun canScrollVertically(): Boolean {
        //      return false
        //  }
        // }
        //custDeliveryStatusRecyclerview.layoutManager = myLinearLayoutManager
        //custDeliveryStatusRecyclerview.adapter = CustomerViewDeliveryStatusAdapter(custDeliveryStatusArrayList)
        //getData()
        getProductData()

        binding.btnComplete.setOnClickListener {
            createDeliveredItem()
        }


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db =
            FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Delivery Status")

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                custDeliveryStatusArrayList.clear()
                if (snapshot.exists()) {
                    for (deliverysnaps in snapshot.children) {
                        if (deliverysnaps.child("userId").value.toString().equals(userId)) {
                            //binding.outputOrderId.text = deliverysnaps.child("orderID").value.toString()
                            //binding.outputDeliveryAddress.text = deliverysnaps.child("deliveryAddress").value.toString()
                            val deliveryStatus =
                                deliverysnaps.getValue(DeliveryStatus::class.java)
                            custDeliveryStatusArrayList.add(deliveryStatus!!)
                        }
                    }
                    custDeliveryStatusRecyclerview.adapter =
                        CustomerViewDeliveryStatusAdapter(custDeliveryStatusArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getProductData() {
        val orderId = binding.outputOrderIdd.text.toString()
        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Status")
        val key = ref.key.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db =
            FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Delivery Status")

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                deliveryProductsArrayList.clear()
                if (snapshot.exists()) {
                    var test = 0
                    //problem here wont show the new updated delivery status path problem
                    for (snap in snapshot.children) {
                        val keyRuth = snap.key.toString()
                        for (snap2 in snap.children) {
                            if (snap2.child("orderID").value.toString().equals(orderId)) {
                                test++
                                binding.labelHouseAddress.text =
                                    snap2.child("address").value.toString()
                                binding.outputPurchasedDate.text =
                                    snap2.child("dateTime").value.toString()
                                binding.labelAmount.text =
                                    snap2.child("TotalAmount").value.toString()
                                //binding.outputOrderId.text = deliverysnaps.child("orderID").value.toString()
                                //binding.outputDeliveryAddress.text = deliverysnaps.child("deliveryAddress").value.toString()
                                val deliveryStatus =
                                    snap2.getValue(DeliveryStatus::class.java)
                                deliveryProductsArrayList.add(deliveryStatus!!)
                                // }
                                //Toast.makeText(context, keyRuth, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    Toast.makeText(context, test.toString(), Toast.LENGTH_LONG).show()
                    deliveryStatusProducts.adapter =
                        DeliveryStatusProductsAdapter(deliveryProductsArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createDeliveredItem() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val orderId = binding.outputOrderIdd.text
        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("Delivery Status")
        val databaseReference = databaseNew.getReference().child("Delivered Items").child(FirebaseAuth.getInstance().currentUser!!.uid).push()
        val key = databaseReference.key.toString()

        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val deliveredDate = currentDate.format(Calendar.getInstance().time)

        val currentTime = SimpleDateFormat("HH:mm:ss")
        val deliveredTime = currentTime.format(Calendar.getInstance().time)

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                        //Toast.makeText(context, snap.key.toString(), Toast.LENGTH_LONG).show()
//                        val prodId = snap.child("id").value as String
//                        Toast.makeText(context, prodId, Toast.LENGTH_LONG).show()
                        for (snap2 in snap.children) {
                            val prodId = snap2.key.toString()
                                if (snap2.child("orderID").value.toString().equals(orderId)) {
                                    deliveredItems["orderID"] = orderId.toString()
                                    deliveredItems["userId"] = userId
                                    val id = snap2.child("id").value.toString()
                                    deliveredItems["id"] = prodId
                                    deliveredItems["address"] = snap2.child("address").value as String
                                    deliveredItems["TotalAmount"] =
                                        snap2.child("TotalAmount").value as String
                                    deliveredItems["img_url"] =
                                        snap2.child("img_url").value as String
                                    deliveredItems["names"] = snap2.child("names").value as String
                                    deliveredItems["quantity"] =
                                        snap2.child("quantity").value as String
                                    deliveredItems["deliveryID"] = key
                                    deliveredItems["deliveredDate"] = "$deliveredDate"
                                    deliveredItems["deliveredTime"] = "$deliveredTime"

                                    try {
                                        databaseReference.child(prodId).setValue(deliveredItems)
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
    }

    //for testing purposes
    private fun push(){
        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("DeliveryStatus")

        val new = databaseNew.getReference().child("LOL").push()
        val key = new.key.toString()

        ref.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    for (snap2 in snap.children) {
                        for (snap3 in snap2.children) {
                            if (snap3.child("orderID").value.toString()
                                    .equals("-NOo0PV6E1kxwBoJwBVv")
                            ) {
                                deliveredItems["quantity"] =
                                    "LOL"
                                try {
                                    new.child(key).setValue(deliveredItems)
                                } catch (e: Exception) {
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





//                                    deliveredItems["orderID"] =
//                                        snap2.child("orderID").value as String
//                                    deliveredItems["deliveryStatus"] =
//                                        snap2.child("orderID").value as String
//                                    deliveredItems["dateTime"] =
//                                        snap2.child("dateTime").value as String
//                                    deliveredItems["userId"] = snap2.child("userId").value as String
//                                    deliveredItems["address"] =
//                                        snap2.child("address").value as String
//                                    deliveredItems["TotalAmount"] =
//                                        snap2.child("TotalAmount").value as String
//                                    deliveredItems["img_url"] =
//                                        snap2.child("img_url").value as String
//                                    deliveredItems["names"] = snap2.child("names").value as String
//                                    deliveredItems["quantity"] =
//                                        snap2.child("quantity").value as String
//                                    deliveredItems["deliveryStatusId"] = keyRuth
//                                    try {
//                                        ref.setValue(deliveredItems)
//                                    } catch (e: Exception) {
//                                        //   Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//                                    }







