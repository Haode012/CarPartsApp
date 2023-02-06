package tarc.edu.carpartsapp.Customer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentPlacedOrderBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlacedOrderFragment : Fragment() {

    private var _binding: FragmentPlacedOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlacedOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


// Retrieve data from SharedPreferences
        val sharedPref =
            requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val retrievedJson = sharedPref.getString("itemList", "")
        if (retrievedJson!!.isNotEmpty()) {
            Log.d("Retrieved JSON", retrievedJson)
            val type = object : TypeToken<ArrayList<MyCartModel>>() {}.type
            val myCartModelArrayList = gson.fromJson<ArrayList<MyCartModel>>(retrievedJson, type)

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("OrderItem(Cash On Delivery)")
            val orderID = myRef.push().key

            for (myCartModel in myCartModelArrayList) {
                val id = myCartModel.id
                myCartModel.orderID = orderID!!
                myRef.child(orderID!!).child(id!!).setValue(myCartModel).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
        }
    }
}