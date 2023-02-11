package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_car_parts_category.*
import tarc.edu.carpartsapp.Adapter.CarPartsCategoryAdapterAdmin
import tarc.edu.carpartsapp.Adapter.CarPartsCategoryAdapterCustomer
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCarPartsCategoryBinding

class CarPartsCategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carPartsCategoryModelArrayList : ArrayList<CarPartsCategoryModel>
    private lateinit var carPartsCategoryAdapterAdmin: CarPartsCategoryAdapterAdmin
    private lateinit var dbref : DatabaseReference

    private var _binding: FragmentCarPartsCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCarPartsCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddCarPartCategory.setOnClickListener{
            findNavController().navigate(R.id.action_nav_car_parts_category_admin_to_nav_add_car_parts_category_admin)
        }

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    carPartsCategoryAdapterAdmin.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        recyclerView = view.findViewById(R.id.carPartsCategoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        carPartsCategoryModelArrayList = arrayListOf<CarPartsCategoryModel>()

        getData()
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CarPartsCategory")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(categorySnapshot in snapshot.children){

                        val category = categorySnapshot.getValue(CarPartsCategoryModel::class.java)
                        carPartsCategoryModelArrayList.add(category!!)
                    }
                    carPartsCategoryAdapterAdmin = CarPartsCategoryAdapterAdmin(carPartsCategoryModelArrayList, requireContext())
                    recyclerView.adapter = carPartsCategoryAdapterAdmin
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}