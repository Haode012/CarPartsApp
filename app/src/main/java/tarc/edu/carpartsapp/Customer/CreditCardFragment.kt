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
import com.google.common.reflect.TypeToken
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCreditCardBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreditCardFragment : Fragment() {

    private var _binding: FragmentCreditCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreditCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val retrievedJson = sharedPref.getString("itemList", "")
        if (retrievedJson!!.isNotEmpty()) {
            Log.d("Retrieved JSON", retrievedJson)
            val type = object : TypeToken<ArrayList<MyCartModel>>() {}.type
            val myCartModelArrayList = gson.fromJson<ArrayList<MyCartModel>>(retrievedJson, type)

            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val hashMap = HashMap<String, Any>()
            hashMap["currentDate"] = "$saveCurrentDate"

            // Add to Firebase Realtime Database
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("OrderItem(Credit Card)")
            myRef.setValue(myCartModelArrayList).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle empty string case
            Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
        }

        binding.buttonConfirmToPay.setOnClickListener{
        }

    }
}