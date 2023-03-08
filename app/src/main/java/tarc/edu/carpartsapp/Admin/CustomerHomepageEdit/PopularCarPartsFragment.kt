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
import tarc.edu.carpartsapp.Adapter.PopularAdapterAdmin
import tarc.edu.carpartsapp.Adapter.PopularAdapterCustomer
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentPopularCarPartsBinding

class PopularCarPartsFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var popularModelArrayList : ArrayList<PopularModel>
    private lateinit var popularAdapterAdmin: PopularAdapterAdmin

    private var _binding: FragmentPopularCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPopularCarPartsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search
        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    popularAdapterAdmin.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.buttonAddPopularCarPart.setOnClickListener{
            findNavController().navigate(R.id.action_nav_popular_car_parts_admin_to_nav_add_popular_car_parts_admin)
        }

        recyclerView = view.findViewById(R.id.popularCarPartsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        popularModelArrayList = arrayListOf<PopularModel>()

        getData()
    }

    private fun getData() {

        try {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("PopularCarParts")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (popularSnapshot in snapshot.children) {

                            val popular = popularSnapshot.getValue(PopularModel::class.java)
                            popularModelArrayList.add(popular!!)
                        }
                        val context = context
                        if (context != null) {
                            popularAdapterAdmin =
                                PopularAdapterAdmin(popularModelArrayList, context)
                            recyclerView.adapter = popularAdapterAdmin
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