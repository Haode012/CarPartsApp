package tarc.edu.carpartsapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create_delivery_status.*
import kotlinx.android.synthetic.main.rate_delivery.*
import kotlinx.android.synthetic.main.rate_delivery.view.*
import tarc.edu.carpartsapp.Adapter.CustomerViewDeliveryStatusAdapter
import tarc.edu.carpartsapp.Adapter.DeliveryStatusProductsAdapter
import tarc.edu.carpartsapp.Customer.CustomerActivity
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
    private lateinit var feedbackDelivery: HashMap<String, String>
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
        feedbackDelivery = hashMapOf()
        return binding.root
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
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

            val dialogBinding = layoutInflater.inflate(R.layout.rate_delivery, null)
            val dialog = Dialog(requireContext())
            dialog.setContentView(dialogBinding)

            dialog.setCancelable(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            //dialog.show()

            val buttonMaybe = dialogBinding.findViewById<Button>(R.id.buttonLater)
            val buttonRate = dialogBinding.findViewById<Button>(R.id.buttonRate)
            val ratingBar = dialogBinding.findViewById<RatingBar>(R.id.ratingBarRate)
            val ratingBarRates = ratingBar.rating.toString()


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
        var total : Double = 0.0
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
                                binding.outputHouseAddress.text =
                                    snap2.child("address").value.toString()
                                binding.outputPurchasedDate.text =
                                    snap2.child("dateTime").value.toString()

                                //total all product price from order
                                    val amount = snap2.child("TotalAmount").value.toString()
                                total += amount.toDouble()
                                binding.outputTotalAmount.text = "RM"+total.toString()
                                binding.outputDeliveryStatusnew.text = snap2.child("deliveryStatus").value.toString()
                                val deliveryStatus =
                                    snap2.getValue(DeliveryStatus::class.java)
                                deliveryProductsArrayList.add(deliveryStatus!!)

                            }
                        }
                    }
//                    Toast.makeText(context, test.toString(), Toast.LENGTH_LONG).show()
                    deliveryStatusProducts.adapter =
                        DeliveryStatusProductsAdapter(deliveryProductsArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun createDeliveredItem() {

        val dialogBinding = layoutInflater.inflate(R.layout.rate_delivery, null)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding)

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val buttonMaybe = dialogBinding.findViewById<Button>(R.id.buttonLater)
        val buttonRate = dialogBinding.findViewById<Button>(R.id.buttonRate)


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
                            if ((binding.outputDeliveryStatusnew.text.toString()
                                    .equals("Delivered"))
                            ) {
                                deliveredItems["orderID"] = orderId.toString()
                                deliveredItems["userId"] = userId
                                val id = snap2.child("id").value.toString()
                                deliveredItems["id"] = prodId
                                deliveredItems["address"] =
                                    snap2.child("address").value as String
                                deliveredItems["TotalAmount"] =
                                    snap2.child("TotalAmount").value as String
                                deliveredItems["img_url"] =
                                    snap2.child("img_url").value as String
                                deliveredItems["names"] =
                                    snap2.child("names").value as String
                                deliveredItems["quantity"] =
                                    snap2.child("quantity").value as String
                                deliveredItems["deliveryID"] = key
                                deliveredItems["deliveredDate"] = "$deliveredDate"
                                deliveredItems["deliveredTime"] = "$deliveredTime"
                                deliveredItems["warranty"] =
                                    snap2.child("warranty").value as String
                                try {
                                    databaseReference.child(prodId).setValue(deliveredItems)
                                } catch (e: Exception) {
                                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                try {
                                    val lol = getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)
                                    Toast.makeText(context,lol.toString(),Toast.LENGTH_SHORT).show()
                                    dialog.show()
                                    buttonMaybe.setOnClickListener {
                                        dialog.dismiss()
                                        findNavController().navigate(R.id.action_fragmentCustDeliveryStatus_to_nav_home_customer)
                                    }
                                    buttonRate.setOnClickListener {
                                        val ratingBar =
                                            dialog.findViewById<RatingBar>(R.id.ratingBarRate)
                                        val ratings = ratingBar.rating
                                        // Toast.makeText(context, ratingBar.rating.toString(), Toast.LENGTH_LONG).show()
                                        saveDeliveryFeedback(ratings)
                                        dialog.dismiss()
                                        findNavController().navigate(R.id.action_fragmentCustDeliveryStatus_to_nav_home_customer)

                                    }

                                }catch (e:NullPointerException){

                                }
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

    @SuppressLint("InflateParams")
    private fun saveDeliveryFeedback(ratings: Float) {

            val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val databaseReference2 = database.getReference("Admin View All Feedback Delivery").push()
            val ref = databaseReference2.key
            //val fullRatings = rating.toString()

            feedbackDelivery["deliveryFeedbackId"] = ref.toString()
            feedbackDelivery["ratingBarRateDelivery"] = ratings.toString()
            try {
                databaseReference2.setValue(feedbackDelivery)
                Toast.makeText(context, "Thank you for rating our service", Toast.LENGTH_LONG).show()
            } catch (e: NullPointerException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
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







