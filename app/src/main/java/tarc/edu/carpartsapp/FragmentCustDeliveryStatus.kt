package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.CustomerViewDeliveryStatusAdapter
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.databinding.FragmentCustDeliveryStatusBinding

class FragmentCustDeliveryStatus : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var custDeliveryStatusRecyclerview: RecyclerView
    private lateinit var custDeliveryStatusArrayList: ArrayList<DeliveryStatus>

    private lateinit var deliveryStatusAdapter: CustomerViewDeliveryStatusAdapter

    private var _binding : FragmentCustDeliveryStatusBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCustDeliveryStatusBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         custDeliveryStatusRecyclerview = view.findViewById(R.id.recyclerViewDeliveryStatusCustomer)
        custDeliveryStatusRecyclerview.layoutManager = LinearLayoutManager(this.context)
         custDeliveryStatusRecyclerview.setHasFixedSize(true)
        custDeliveryStatusArrayList = arrayListOf<DeliveryStatus>()

      //  val myLinearLayoutManager = object : LinearLayoutManager(context) {
         //   override fun canScrollVertically(): Boolean {
          //      return false
          //  }
       // }
        //custDeliveryStatusRecyclerview.layoutManager = myLinearLayoutManager
        //custDeliveryStatusRecyclerview.adapter = CustomerViewDeliveryStatusAdapter(custDeliveryStatusArrayList)
        getData()


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Delivery Status")

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                custDeliveryStatusArrayList.clear()
                if (snapshot.exists()) {
                    for (deliverysnaps in snapshot.children) {
                        if(deliverysnaps.child("userId").value.toString().equals(userId)) {
                            binding.outputOrderId.text = deliverysnaps.child("orderID").value.toString()
                            binding.outputDeliveryAddress.text = deliverysnaps.child("deliveryAddress").value.toString()
                        val deliveryStatus =
                            deliverysnaps.getValue(DeliveryStatus::class.java)
                        custDeliveryStatusArrayList.add(deliveryStatus!!)
                         }
                    }
                    custDeliveryStatusRecyclerview.adapter = CustomerViewDeliveryStatusAdapter(custDeliveryStatusArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}