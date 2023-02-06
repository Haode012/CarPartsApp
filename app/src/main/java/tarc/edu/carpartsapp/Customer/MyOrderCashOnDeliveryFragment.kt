package tarc.edu.carpartsapp.Customer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.MyCartAdapter
import tarc.edu.carpartsapp.Adapter.MyOrderCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentMyOrderCashOnDeliveryBinding

class MyOrderCashOnDeliveryFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myOrderModelArrayList : ArrayList<MyOrderModel>
    private var _binding: FragmentMyOrderCashOnDeliveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyOrderCashOnDeliveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        myOrderModelArrayList = arrayListOf<MyOrderModel>()

        getData()
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderItem(Cash On Delivery)")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (firstChildSnapshot in snapshot.children) {
                            for (secondChildSnapshot in firstChildSnapshot.children) {
                                val myOrder = secondChildSnapshot.getValue(MyOrderModel::class.java)
                                myOrderModelArrayList.add(myOrder!!)
                            }
                        }
                        val myOrderAdapter = MyOrderCashOnDeliveryAdapter(myOrderModelArrayList, requireContext())
                        recyclerView.adapter = myOrderAdapter
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