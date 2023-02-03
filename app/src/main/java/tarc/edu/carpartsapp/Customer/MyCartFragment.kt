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
import tarc.edu.carpartsapp.Adapter.PopularAdapterAdmin
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentMyCartBinding

class MyCartFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myCartModelArrayList : ArrayList<MyCartModel>
    private var _binding: FragmentMyCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCashOnDelivery.setOnClickListener{
            findNavController().navigate(R.id.action_nav_myCart_customer_to_nav_placedOrder_customer)
        }

        binding.buttonCreditCard.setOnClickListener{
            findNavController().navigate(R.id.action_nav_myCart_customer_to_nav_creditCard_customer)
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        myCartModelArrayList = arrayListOf<MyCartModel>()

        getData()
    }

    private fun getData(){


        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CartItem")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (myCartSnapshot in snapshot.children) {
                            val myCart = myCartSnapshot.getValue(MyCartModel::class.java)
                            myCartModelArrayList.add(myCart!!)
                        }
                        val myCartAdapter = MyCartAdapter(myCartModelArrayList, requireContext())
                        recyclerView.adapter = myCartAdapter
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