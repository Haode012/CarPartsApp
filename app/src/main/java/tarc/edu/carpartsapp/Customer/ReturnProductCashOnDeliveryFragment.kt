package tarc.edu.carpartsapp.Customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.MyOrderCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Adapter.PopularAdapterCustomer
import tarc.edu.carpartsapp.Adapter.ReturnProductCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentReturnProductCashOnDeliveryBinding

class ReturnProductCashOnDeliveryFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var myOrderModelArrayList : ArrayList<MyOrderModel>
    private lateinit var returnProductCashOnDeliveryAdapter: ReturnProductCashOnDeliveryAdapter

    private var _binding: FragmentReturnProductCashOnDeliveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReturnProductCashOnDeliveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        myOrderModelArrayList = arrayListOf<MyOrderModel>()

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    returnProductCashOnDeliveryAdapter.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        getData()

    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderItem(Cash On Delivery)").child(
            FirebaseAuth.getInstance().currentUser!!.uid)

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
                        returnProductCashOnDeliveryAdapter = ReturnProductCashOnDeliveryAdapter(myOrderModelArrayList, requireContext())
                        recyclerView.adapter = returnProductCashOnDeliveryAdapter
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