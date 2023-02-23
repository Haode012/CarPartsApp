package tarc.edu.carpartsapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import tarc.edu.carpartsapp.Adapter.DeliveryNeededAdapter
import tarc.edu.carpartsapp.Adapter.FeedbackAdapter
import tarc.edu.carpartsapp.Model.Deliveries
import tarc.edu.carpartsapp.Model.Feedback
import tarc.edu.carpartsapp.databinding.FragmentAdminViewFeedbackBinding
import tarc.edu.carpartsapp.databinding.FragmentEditProfileBinding
import tarc.edu.carpartsapp.databinding.FragmentViewAllDeliveryBinding

class FragmentViewAllDelivery : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var needToDeliveryRecyclerView: RecyclerView
    private lateinit var needToDeliveryList: ArrayList<Deliveries>

    private lateinit var soonDeliveryAdapter : DeliveryNeededAdapter

    private var _binding : FragmentViewAllDeliveryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewAllDeliveryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        needToDeliveryRecyclerView = view.findViewById(R.id.recyclerViewAllDeliveryToCreate)
        needToDeliveryRecyclerView.layoutManager = LinearLayoutManager(this.context)
        needToDeliveryRecyclerView.setHasFixedSize(true)
        needToDeliveryList = arrayListOf<Deliveries>()
        getData()


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("OrderItem(Cash On Delivery) Duplicate")

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                needToDeliveryList.clear()
                if (snapshot.exists()) {
                    for (firstChildSnapshot in snapshot.children) {
                        for (secondChildSnapshot in firstChildSnapshot.children) {
                                val delivery =
                                    secondChildSnapshot.getValue(Deliveries::class.java)
                                needToDeliveryList.add(delivery!!)

                        }
                    }
                }
                    needToDeliveryRecyclerView.adapter = DeliveryNeededAdapter(needToDeliveryList)



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}