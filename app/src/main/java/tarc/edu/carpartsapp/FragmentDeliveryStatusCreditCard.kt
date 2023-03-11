package tarc.edu.carpartsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Adapter.DeliveryStatusProductsCreditCardAdapter
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.databinding.FragmentCustDeliveryStatusCreditCardBinding
import tarc.edu.carpartsapp.databinding.FragmentDeliveryStatusCreditCardBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentDeliveryStatusCreditCard : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var custDeliveryStatusCreditCardRecyclerview: RecyclerView
    private lateinit var custDeliveryCreditCardList: ArrayList<DeliveryStatus>

    private lateinit var deliveryStatusCreditAdapter: DeliveryStatusProductsCreditCardAdapter

    private lateinit var deliveredItemsCreditCard: HashMap<String, String>
    private var _binding : FragmentDeliveryStatusCreditCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDeliveryStatusCreditCardBinding.inflate(inflater, container, false)
        val args = this.arguments
        val inputData = args?.get("orderID")
        val orderId = binding.orderIdd
        orderId.text = inputData.toString()

        deliveredItemsCreditCard = hashMapOf()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//deliveryStatus
        custDeliveryStatusCreditCardRecyclerview = view.findViewById(R.id.recyclerViewCustDeliveryCredit)
        custDeliveryStatusCreditCardRecyclerview.layoutManager = LinearLayoutManager(this.context)
        custDeliveryStatusCreditCardRecyclerview.setHasFixedSize(true)
        custDeliveryCreditCardList = arrayListOf<DeliveryStatus>()

        val args = this.arguments
        val inputData = args?.get("orderID")
        val orderId = binding.orderIdd
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

        binding.btnComplete2.setOnClickListener {
            createDeliveredItem()
        }


    }

    private fun getProductData() {
        var total : Double = 0.0
        val orderId = binding.orderIdd.text.toString()
        val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Status")
        val key = ref.key.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Delivery Status(Credit Card Payment)")

        db.addValueEventListener(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                custDeliveryCreditCardList.clear()
                if (snapshot.exists()) {
                    var test = 0
                    //problem here wont show the new updated delivery status path problem
                    for (snap in snapshot.children) {
                        val keyRuth = snap.key.toString()
                        for(snap2 in snap.children) {
                            if (snap2.child("orderID").value.toString().equals(orderId)) {
                                test++
                                binding.address.text =
                                    snap2.child("address").value.toString()
                                binding.datePurchased.text =
                                    snap2.child("dateTime").value.toString()
                                val amount =
                                    snap2.child("TotalAmount").value.toString()
                                total += amount.toDouble()
                                binding.total.text = "RM " + total.toString()
                                binding.status.text = snap2.child("deliveryStatus").value.toString()
                                //binding.outputOrderId.text = deliverysnaps.child("orderID").value.toString()
                                //binding.outputDeliveryAddress.text = deliverysnaps.child("deliveryAddress").value.toString()
                                val deliveryStatus =
                                    snap2.getValue(DeliveryStatus::class.java)
                                custDeliveryCreditCardList.add(deliveryStatus!!)
                                // }
                                //Toast.makeText(context, keyRuth, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
//                    Toast.makeText(context, test.toString(), Toast.LENGTH_LONG).show()
                    custDeliveryStatusCreditCardRecyclerview.adapter = DeliveryStatusProductsCreditCardAdapter(custDeliveryCreditCardList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createDeliveredItem() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val orderId = binding.orderIdd.text
        val databaseNew = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = databaseNew.getReference().child("Delivery Status(Credit Card Payment)")
        val databaseReference = databaseNew.getReference().child("Delivered Item(Credit Card Payment)").child(FirebaseAuth.getInstance().currentUser!!.uid).push()
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
                            deliveredItemsCreditCard["deliveryID"] = key
                            deliveredItemsCreditCard["orderID"] = orderId.toString()
                            deliveredItemsCreditCard["userId"] = userId
                            val id = snap2.child("id").value.toString()
                            deliveredItemsCreditCard["id"] = prodId
                            deliveredItemsCreditCard["address"] = snap2.child("address").value as String
                            deliveredItemsCreditCard["TotalAmount"] =
                                snap2.child("TotalAmount").value as String
                            deliveredItemsCreditCard["img_url"] =
                                snap2.child("img_url").value as String
                            deliveredItemsCreditCard["names"] = snap2.child("names").value as String
                            deliveredItemsCreditCard["quantity"] =
                                snap2.child("quantity").value as String
                            deliveredItemsCreditCard["deliveredDate"] = "$deliveredDate"
                            deliveredItemsCreditCard["deliveredTime"] = "$deliveredTime"
                            deliveredItemsCreditCard["warranty"] = snap2.child("warranty").value as String

                            try {
                                databaseReference.child(prodId).setValue(deliveredItemsCreditCard)
                            } catch (e: Exception) {
                                //   Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                        //////
                        ////
                        ////
                    }
                }
            }


            //}

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}