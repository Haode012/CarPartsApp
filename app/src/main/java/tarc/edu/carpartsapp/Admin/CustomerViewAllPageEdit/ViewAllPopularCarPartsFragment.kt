package tarc.edu.carpartsapp.Admin.CustomerViewAllPageEdit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_popular_car_parts.*
import tarc.edu.carpartsapp.Adapter.PopularAdapterAdmin
import tarc.edu.carpartsapp.Adapter.ViewAllPopularAdapterAdmin
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentViewAllPopularCarPartsAdminBinding

class ViewAllPopularCarPartsFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllPopularModelArrayList : ArrayList<ViewAllPopularModel>
    private lateinit var viewAllPopularAdapterAdmin: ViewAllPopularAdapterAdmin

    private var _binding: FragmentViewAllPopularCarPartsAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewAllPopularCarPartsAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddPopularCarPart.setOnClickListener{
            findNavController().navigate(R.id.action_nav_view_all_popular_car_parts_admin_to_nav_add_view_all_popular_car_parts_admin)
        }

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    viewAllPopularAdapterAdmin.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        recyclerView = view.findViewById(R.id.popularCarPartsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        viewAllPopularModelArrayList = arrayListOf<ViewAllPopularModel>()

        getData()
    }

    private fun getData() {
        try{
        dbref = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ViewAllPopular")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(popularSnapshot in snapshot.children){

                        val popular = popularSnapshot.getValue(ViewAllPopularModel::class.java)
                       viewAllPopularModelArrayList.add(popular!!)
                    }
                    val context = context
                    if (context != null) {
                        viewAllPopularAdapterAdmin = ViewAllPopularAdapterAdmin(
                            viewAllPopularModelArrayList,
                            context
                        )
                        recyclerView.adapter = viewAllPopularAdapterAdmin
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    } catch (e: Exception) {

    }
}
    }