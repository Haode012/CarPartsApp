package tarc.edu.carpartsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.DeliveryStatusAdapter
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.databinding.FragmentAdminViewAllDeliveryStatusBinding

class FragmentAdminViewAllDeliveryStatus : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var deliveryStatusRecyclerview: RecyclerView
    private lateinit var deliveryStatusArrayList: ArrayList<DeliveryStatus>

    private lateinit var deliveryStatusAdapter: DeliveryStatusAdapter

    private var _binding : FragmentAdminViewAllDeliveryStatusBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminViewAllDeliveryStatusBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deliveryStatusRecyclerview = view.findViewById(R.id.deliveryStatusRecyclerView)
        deliveryStatusRecyclerview.layoutManager = LinearLayoutManager(this.context)
        deliveryStatusRecyclerview.setHasFixedSize(true)
        deliveryStatusArrayList = arrayListOf<DeliveryStatus>()
        getData()


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference().child("Delivery Status")
        val displayedChildNodes = HashMap<String, Boolean>()
        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                deliveryStatusArrayList.clear()
                if (snapshot.exists()) {
                    for (deliverysnaps in snapshot.children) {
                        val key = deliverysnaps.key.toString()
                        for (snap in deliverysnaps.children) {
                            val deliveryStatus = snap.getValue(DeliveryStatus::class.java)
                            deliveryStatusArrayList.add(deliveryStatus!!)
                            break
                            //Toast.makeText(context, key, Toast.LENGTH_LONG).show()
                        }
                    }
                    try {
                        deliveryStatusRecyclerview.adapter =
                            DeliveryStatusAdapter(deliveryStatusArrayList)
                    }catch (e: NullPointerException){}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}