package tarc.edu.carpartsapp.Customer

import android.content.Context
import android.os.Bundle
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

class HomeCustomerFragment : Fragment() {

    private lateinit var scrollView : ScrollView
    private lateinit var progressBar : ProgressBar
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var popularModelArrayList : ArrayList<PopularModel>

    //car parts category
    private lateinit var recyclerView2: RecyclerView
    private lateinit var carPartsCategoryModelArrayList : ArrayList<CarPartsCategoryModel>

    //recommended
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recommendedModelArrayList : ArrayList<RecommendedModel>

    private var _binding: FragmentHomeCustomerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

            for (myCartModel in myCartModelArrayList) {
                val id = myCartModel.id
                myCartModel.orderID = orderID!!
                myRef.child(uid).child(orderID!!).child(id!!).setValue(myCartModel).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
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

            for (myOrderModel in myOrderModelArrayList) {
                val id = myOrderModel.id
                myOrderModel.paymentID = paymentID!!
                myOrderModel.bankType = bankType!!
                myOrderModel.cardNumber = cardNumber!!
                myOrderModel.expirationDateMonth = expirationDateMonth!!
                myOrderModel.expirationDateYear = expirationDateYear!!
                myOrderModel.CVV = CVV!!

                myRef.child(uid).child(paymentID!!).child(id!!).setValue(myOrderModel).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()
                        val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                        val editor = sharedPref.edit()
                        editor.clear()
                        editor.apply()

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
                    recyclerView.adapter = PopularAdapterCustomer(popularModelArrayList, requireContext())
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
                    recyclerView2.adapter = CarPartsCategoryAdapterCustomer(carPartsCategoryModelArrayList, requireContext())
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
                    recyclerView3.adapter = RecommendedAdapterCustomer(recommendedModelArrayList, requireContext())
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