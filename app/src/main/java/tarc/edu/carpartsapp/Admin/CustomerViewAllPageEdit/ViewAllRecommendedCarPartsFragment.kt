package tarc.edu.carpartsapp.Admin.CustomerViewAllPageEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.ViewAllRecommendedAdapterAdmin
import tarc.edu.carpartsapp.Model.ViewAllRecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentViewAllRecommendedCarPartsAdminBinding

class ViewAllRecommendedCarPartsFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllRecommendedModelArrayList : ArrayList<ViewAllRecommendedModel>

    private var _binding: FragmentViewAllRecommendedCarPartsAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewAllRecommendedCarPartsAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddRecommendedCarPart.setOnClickListener{
            findNavController().navigate(R.id.action_nav_view_all_recommended_car_parts_admin_to_nav_add_view_all_recommended_car_parts_admin)
        }

        recyclerView = view.findViewById(R.id.recommendedCarPartsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        viewAllRecommendedModelArrayList = arrayListOf<ViewAllRecommendedModel>()

        getData()
    }

    private fun getData() {
        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ViewAllRecommended")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(recommendedSnapshot in snapshot.children){

                        val recommended = recommendedSnapshot.getValue(ViewAllRecommendedModel::class.java)
                        viewAllRecommendedModelArrayList.add(recommended!!)
                    }
                    val pAdapter = ViewAllRecommendedAdapterAdmin(viewAllRecommendedModelArrayList, requireContext())
                    recyclerView.adapter = pAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}