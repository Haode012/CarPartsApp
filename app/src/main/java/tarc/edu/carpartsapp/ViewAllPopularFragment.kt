package tarc.edu.carpartsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.ViewAllPopularAdapter
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentViewAllPopularBinding

class ViewAllPopularFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllPopularModelArrayList : ArrayList<ViewAllPopularModel>

    private var _binding: FragmentViewAllPopularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewAllPopularBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.view_all_popular_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        viewAllPopularModelArrayList = arrayListOf<ViewAllPopularModel>()

        getData()
    }

    private fun getData() {
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
                        val pAdapter =
                            ViewAllPopularAdapter(viewAllPopularModelArrayList, context)
                        recyclerView.adapter = pAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}