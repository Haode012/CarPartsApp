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
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCreditCardBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreditCardFragment : Fragment() {

    private lateinit var myCartModelArrayList: ArrayList<MyCartModel>
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
        val sharedPref =
            requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return
        val gson = Gson()
        val retrievedJson = sharedPref.getString("itemList2", "")
        if (retrievedJson!!.isNotEmpty()) {
            Log.d("Retrieved JSON", retrievedJson)
            val type = object : TypeToken<ArrayList<MyCartModel>>() {}.type
            val myCartModelArrayList = gson.fromJson<ArrayList<MyCartModel>>(retrievedJson, type)
            val myOrderModelArrayList = myCartModelArrayList

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("OrderItem(Credit Card)")
            val orderID = myRef.push().key

            for (myCartModel in myCartModelArrayList) {
                val id = myCartModel.id
                myCartModel.orderID = orderID!!
                myRef.child(orderID!!).child(id!!).setValue(myCartModel)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Store myOrderModelArrayList in another SharedPreferences
                            val sharedPref3 = requireActivity().getSharedPreferences("MyPref3", Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                            val editor3 = sharedPref3.edit()
                            val gson3 = Gson()
                            val json3 = gson3.toJson(myOrderModelArrayList)
                            editor3.putString("orderID", orderID)
                            editor3.putString("itemList3", json3)
                            editor3.apply()
                        } else {
                            Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        } else {
            Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
        }

        binding.buttonConfirmToPay.setOnClickListener {

            val bankType = binding.spinnerBankType.selectedItem.toString().trim()
            val cardNumber = binding.editTextCardNumber.text.toString().trim()
            val expirationDateMonth = binding.editTextExpirationDateMonth.text.toString().trim()
            val expirationDateYear = binding.editTextExpirationDateYear.text.toString().trim()
            val CVV = binding.editTextCVV.text.toString().trim()

            //validation
            if (bankType.isEmpty()) {
                Toast.makeText(requireContext(), "Choose a bank type", Toast.LENGTH_SHORT).show()
            } else if (cardNumber.isEmpty()) {
                binding.editTextCardNumber.error = "Card Number cannot be blank"
            } else if (!cardNumber.matches(Regex("^\\d{16}$"))) {
                binding.editTextCardNumber.error = "Card Number must be 16 digits and no letters"
            } else if (expirationDateMonth.isEmpty()) {
                binding.editTextExpirationDateMonth.error = "Month cannot be blank"
            } else if (!expirationDateMonth.matches(Regex("^\\d{2}$"))) {
                binding.editTextExpirationDateMonth.error = "Month must be 2 digits and no letters"
            } else if (expirationDateYear.isEmpty()) {
                binding.editTextExpirationDateYear.error = "Year cannot be blank"
            } else if (!expirationDateYear.matches(Regex("^\\d{2}$"))) {
                binding.editTextExpirationDateYear.error = "Year must be 2 digits and no letters"
            } else if (CVV.isEmpty()) {
                binding.editTextCVV.error = "CVV cannot be blank"
            } else if (!CVV.matches(Regex("^\\d{3}$"))) {
                binding.editTextCVV.error = "CVV must be 3 digits and no letters"
            } else {
                    findNavController().navigate(
                        R.id.action_nav_creditCard_customer_to_nav_home_customer,
                        Bundle().apply {
                            putString("bankType", bankType)
                            putString("cardNumber", cardNumber)
                            putString("expirationDateMonth", expirationDateMonth)
                            putString("expirationDateYear", expirationDateYear)
                            putString("CVV", CVV)
                        })
                }
            }
        }
}