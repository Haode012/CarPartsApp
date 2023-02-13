package tarc.edu.carpartsapp.Customer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import tarc.edu.carpartsapp.Adapter.*
import tarc.edu.carpartsapp.Model.*
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentHomeCustomerBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeCustomerFragment : Fragment() {
    private lateinit var orderDuplication: HashMap<String, String>
    private lateinit var paymentDuplication: HashMap<String, String>
    private lateinit var scrollView : ScrollView
    private lateinit var progressBar : ProgressBar
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var popularModelArrayList : ArrayList<PopularModel>
    private lateinit var popularAdapterCustomer: PopularAdapterCustomer

    //car parts category
    private lateinit var recyclerView2: RecyclerView
    private lateinit var carPartsCategoryModelArrayList : ArrayList<CarPartsCategoryModel>
    private lateinit var carPartsCategoryAdapterCustomer: CarPartsCategoryAdapterCustomer

    //recommended
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recommendedModelArrayList : ArrayList<RecommendedModel>
    private lateinit var recommendedAdapterCustomer: RecommendedAdapterCustomer

    private var _binding: FragmentHomeCustomerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        orderDuplication = hashMapOf()
        paymentDuplication = hashMapOf()

        _binding = FragmentHomeCustomerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        scrollView = view.findViewById(R.id.scrollView)

        progressBar.setVisibility(View.VISIBLE)
        scrollView.setVisibility(View.GONE)

        recyclerView = view.findViewById(R.id.recycleViewHorizontal)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        popularModelArrayList = arrayListOf<PopularModel>()

        //car parts category
        recyclerView2 = view.findViewById(R.id.recycleViewHorizontal2)
        recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerView2.setHasFixedSize(true)
        carPartsCategoryModelArrayList = arrayListOf<CarPartsCategoryModel>()


        //recommended
        recyclerView3 = view.findViewById(R.id.recycleViewHorizontal3)
        recyclerView3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerView3.setHasFixedSize(true)
        recommendedModelArrayList = arrayListOf<RecommendedModel>()

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    popularAdapterCustomer.filter.filter(s)
                    carPartsCategoryAdapterCustomer.filter.filter(s)
                    recommendedAdapterCustomer.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        getData()

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
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val orderID = myRef.push().key
            val deliveryAddress = arguments?.getString("deliveryAddress").toString()
            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val currentTime = SimpleDateFormat("HH:mm:ss")
            val saveCurrentTime = currentTime.format(Calendar.getInstance().time)

            val myRefNew = database.getReference("OrderItem(Cash On Delivery) Duplicate")

            for (myCartModel in myCartModelArrayList) {
                val id = myCartModel.id
                myCartModel.orderID = orderID!!
                myCartModel.deliveryAddress = deliveryAddress!!
                myCartModel.currentDate = saveCurrentDate!!
                myCartModel.currentTime = saveCurrentTime!!
                myRef.child(uid).child(orderID!!).child(id!!).setValue(myCartModel).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                                orderDuplication["orderID"] = orderID
                                orderDuplication["userId"] = uid
                                orderDuplication["TotalAmount"] = myCartModel.total_price
                                orderDuplication["Delivery Address"] = myCartModel.deliveryAddress
                                orderDuplication["Date of Order Placed"] = myCartModel.currentDate
                                try {
                                    myRefNew.child(orderID).setValue(orderDuplication)
                                } catch (e: Exception) {
                                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                                }

                        Toast.makeText(requireContext(), "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()
                        val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                        val editor = sharedPref.edit()
                        editor.clear()
                        editor.apply()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
        }

        // Retrieve data from SharedPreferences3
        val sharedPref3 =
            requireActivity().getSharedPreferences("MyPref3", Context.MODE_PRIVATE) ?: return
        val gson3 = Gson()
        val retrievedJson3 = sharedPref3.getString("itemList3", "")
        if (retrievedJson3!!.isNotEmpty()) {
            Log.d("Retrieved JSON3", retrievedJson3)
            val type3 = object : TypeToken<ArrayList<MyOrderModel>>() {}.type
            val myOrderModelArrayList = gson3.fromJson<ArrayList<MyOrderModel>>(retrievedJson3, type3)

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("PaymentDetails")
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val paymentID = myRef.push().key
            val bankType = arguments?.getString("bankType").toString()
            val cardNumber = arguments?.getString("cardNumber").toString()
            val expirationDateMonth = arguments?.getString("expirationDateMonth").toString()
            val expirationDateYear = arguments?.getString("expirationDateYear").toString()
            val CVV = arguments?.getString("CVV").toString()
            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val currentTime = SimpleDateFormat("HH:mm:ss")
            val saveCurrentTime = currentTime.format(Calendar.getInstance().time)

            val myRefNew = database.getReference("PaymentDetails Duplicate")

            for (myOrderModel in myOrderModelArrayList) {
                val id = myOrderModel.id
                val orderID = myOrderModel.orderID
                myOrderModel.paymentID = paymentID!!
                myOrderModel.bankType = bankType!!
                myOrderModel.cardNumber = cardNumber!!
                myOrderModel.expirationDateMonth = expirationDateMonth!!
                myOrderModel.expirationDateYear = expirationDateYear!!
                myOrderModel.CVV = CVV!!
                myOrderModel.currentDate = saveCurrentDate!!
                myOrderModel.currentTime = saveCurrentTime!!

                myRef.child(uid).child(paymentID!!).child(id!!).setValue(myOrderModel).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        paymentDuplication["orderID"] = orderID
                        paymentDuplication["paymentID"] = paymentID
                        paymentDuplication["userId"] = uid
                        orderDuplication["Total Amount"] = myOrderModel.total_price
                        orderDuplication["Delivery Address"] = myOrderModel.deliveryAddress
                        orderDuplication["Date of Order Placed"] = myOrderModel.currentDate

                        try {
                            myRefNew.child(orderID).setValue(paymentDuplication)
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }

                        Toast.makeText(requireContext(), "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()

                        val sharedPref3 = requireActivity().getSharedPreferences("MyPref3", Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                        val editor3 = sharedPref3.edit()
                        editor3.clear()
                        editor3.apply()

                    } else {
                        Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
        }
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PopularCarParts")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(popularSnapshot in snapshot.children){

                        val popular = popularSnapshot.getValue(PopularModel::class.java)
                        popularModelArrayList.add(popular!!)
                    }
                    popularAdapterCustomer = PopularAdapterCustomer(popularModelArrayList, requireContext())
                    recyclerView.adapter = popularAdapterCustomer
                    progressBar.setVisibility(View.GONE)
                    scrollView.setVisibility(View.VISIBLE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CarPartsCategory")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(categorySnapshot in snapshot.children){

                        val category = categorySnapshot.getValue(CarPartsCategoryModel::class.java)
                        carPartsCategoryModelArrayList.add(category!!)
                    }
                    carPartsCategoryAdapterCustomer = CarPartsCategoryAdapterCustomer(carPartsCategoryModelArrayList, requireContext())
                    recyclerView2.adapter = carPartsCategoryAdapterCustomer
                    progressBar.setVisibility(View.GONE)
                    scrollView.setVisibility(View.VISIBLE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Recommended")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(recommendedSnapshot in snapshot.children){

                        val recommended = recommendedSnapshot.getValue(RecommendedModel::class.java)
                        recommendedModelArrayList.add(recommended!!)
                    }
                    recommendedAdapterCustomer = RecommendedAdapterCustomer(recommendedModelArrayList, requireContext())
                    recyclerView3.adapter = recommendedAdapterCustomer
                    progressBar.setVisibility(View.GONE)
                    scrollView.setVisibility(View.VISIBLE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}