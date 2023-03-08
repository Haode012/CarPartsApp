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
import tarc.edu.carpartsapp.Adapter.ReturnProductCashOnDeliveryAdapter
import tarc.edu.carpartsapp.Adapter.ReturnProductStatusAdapter
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.Model.ReturnProductModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentReturnProductStatusBinding

class ReturnProductStatusFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var returnProductModelArrayList : ArrayList<ReturnProductModel>
    private lateinit var returnProductStatusAdapter: ReturnProductStatusAdapter

    private var _binding: FragmentReturnProductStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReturnProductStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        returnProductModelArrayList = arrayListOf<ReturnProductModel>()

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    returnProductStatusAdapter.filter.filter(s)
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

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ReturnProductStatus").child(
            FirebaseAuth.getInstance().currentUser!!.uid)

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (firstChildSnapshot in snapshot.children) {
                            for (secondChildSnapshot in firstChildSnapshot.children) {
                                val returnProduct = secondChildSnapshot.getValue(ReturnProductModel::class.java)
                                returnProductModelArrayList.add(returnProduct!!)
                            }
                        }
                        val context = context
                        if (context != null) {
                            returnProductStatusAdapter = ReturnProductStatusAdapter(
                                returnProductModelArrayList,
                                context
                            )
                            recyclerView.adapter = returnProductStatusAdapter
                        }
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