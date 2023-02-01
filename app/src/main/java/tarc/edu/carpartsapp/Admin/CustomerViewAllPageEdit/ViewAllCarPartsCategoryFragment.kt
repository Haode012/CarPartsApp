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
import tarc.edu.carpartsapp.Adapter.ViewAllCategoryAdapterAdmin
import tarc.edu.carpartsapp.Model.ViewAllCategoryModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentViewAllCarPartsCategoryAdminBinding

class ViewAllCarPartsCategoryFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllCategoryModelArrayList : ArrayList<ViewAllCategoryModel>

    private var _binding: FragmentViewAllCarPartsCategoryAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewAllCarPartsCategoryAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddCarPartCategory.setOnClickListener{
            findNavController().navigate(R.id.action_nav_view_all_car_parts_category_admin_to_nav_add_view_all_car_parts_category_admin)
        }

        recyclerView = view.findViewById(R.id.carPartsCategoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        viewAllCategoryModelArrayList = arrayListOf<ViewAllCategoryModel>()

        getData()
    }

    private fun getData() {

            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {

                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            viewAllCategoryModelArrayList.add(category!!)
                        }
                        recyclerView.adapter =
                            ViewAllCategoryAdapterAdmin(viewAllCategoryModelArrayList, requireContext())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
}