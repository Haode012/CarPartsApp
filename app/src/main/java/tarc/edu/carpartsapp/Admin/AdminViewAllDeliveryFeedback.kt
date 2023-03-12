package tarc.edu.carpartsapp.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.DeliveryServiceRatingAdapter
import tarc.edu.carpartsapp.Adapter.DeliveryStatusAdapter
import tarc.edu.carpartsapp.Model.DeliveryFeedback
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.Model.Feedback
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentAdminViewAllDeliveryFeedbackBinding
import tarc.edu.carpartsapp.databinding.FragmentAdminViewAllDeliveryStatusBinding

class AdminViewAllDeliveryFeedback : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var deliveryFeedbackRecyclerView: RecyclerView
    private lateinit var deliveryFeedbackArrayList: ArrayList<DeliveryFeedback>

    private lateinit var deliveryFeedbackAdapter: DeliveryStatusAdapter

    private var _binding : FragmentAdminViewAllDeliveryFeedbackBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminViewAllDeliveryFeedbackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deliveryFeedbackRecyclerView = view.findViewById(R.id.recyclerViewAllRateFeedback)
        deliveryFeedbackRecyclerView.layoutManager = LinearLayoutManager(this.context)
        deliveryFeedbackRecyclerView.setHasFixedSize(true)
        deliveryFeedbackArrayList = arrayListOf<DeliveryFeedback>()
        getData()


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference().child("Admin View All Feedback Delivery")
        val displayedChildNodes = HashMap<String, Boolean>()
        db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                deliveryFeedbackArrayList.clear()
                if (snapshot.exists()) {
                    for (feedbackSnaps in snapshot.children) {
                            val deliveryFeedback = feedbackSnaps.getValue(DeliveryFeedback::class.java)
                            deliveryFeedbackArrayList.add(deliveryFeedback!!)
                            //Toast.makeText(context, key, Toast.LENGTH_LONG).show()

                    }
                    deliveryFeedbackRecyclerView.adapter = DeliveryServiceRatingAdapter(deliveryFeedbackArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}