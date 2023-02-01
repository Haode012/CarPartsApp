package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import tarc.edu.carpartsapp.Adapter.RecommendedAdapterAdmin
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentRecommendedCarPartsBinding

class RecommendedCarPartsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recommendedModelArrayList : ArrayList<RecommendedModel>
    private lateinit var dbref : DatabaseReference

    private var _binding: FragmentRecommendedCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendedCarPartsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddRecommendedCarPart.setOnClickListener{
            findNavController().navigate(R.id.action_nav_recommended_car_parts_admin_to_nav_add_recommended_car_parts)
        }

        recyclerView = view.findViewById(R.id.recommendedCarPartsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recommendedModelArrayList = arrayListOf<RecommendedModel>()

        getData()
    }

    private fun getData(){

        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Recommended")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(recommendedSnapshot in snapshot.children){

                        val recommended = recommendedSnapshot.getValue(RecommendedModel::class.java)
                        recommendedModelArrayList.add(recommended!!)
                    }
                    recyclerView.adapter = RecommendedAdapterAdmin(recommendedModelArrayList, requireContext())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}