package tarc.edu.carpartsapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.CarPartsCategoryAdapter
import tarc.edu.carpartsapp.Adapter.PopularAdapter
import tarc.edu.carpartsapp.Adapter.RecommendedAdapter
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentHomeCustomerBinding
import tarc.edu.carpartsapp.databinding.FragmentHomeMainBinding

class HomeMainFragment : Fragment() {

    private lateinit var scrollView : ScrollView
    private lateinit var progressBar : ProgressBar
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var popularModelArrayList : ArrayList<PopularModel>
    private lateinit var popularAdapter: PopularAdapter

    //car parts category
    private lateinit var recyclerView2: RecyclerView
    private lateinit var carPartsCategoryModelArrayList : ArrayList<CarPartsCategoryModel>
    private lateinit var carPartsCategoryAdapter: CarPartsCategoryAdapter

    //recommended
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recommendedModelArrayList : ArrayList<RecommendedModel>
    private lateinit var recommendedAdapter: RecommendedAdapter

    private var _binding: FragmentHomeMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeMainBinding.inflate(inflater, container, false)

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
                    popularAdapter.filter.filter(s)
                    carPartsCategoryAdapter.filter.filter(s)
                    recommendedAdapter.filter.filter(s)
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

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PopularCarParts")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(popularSnapshot in snapshot.children){

                        val popular = popularSnapshot.getValue(PopularModel::class.java)
                        popularModelArrayList.add(popular!!)
                    }
                    popularAdapter = PopularAdapter(popularModelArrayList, requireContext())
                    recyclerView.adapter = popularAdapter
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
                    carPartsCategoryAdapter = CarPartsCategoryAdapter(carPartsCategoryModelArrayList, requireContext())
                    recyclerView2.adapter = carPartsCategoryAdapter
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
                    recommendedAdapter = RecommendedAdapter(recommendedModelArrayList, requireContext())
                    recyclerView3.adapter = recommendedAdapter
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