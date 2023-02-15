package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.FeedbackAdapter
import tarc.edu.carpartsapp.Model.Feedback
import tarc.edu.carpartsapp.databinding.FragmentAdminViewFeedbackBinding

class FragmentAdminViewFeedback : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var feedbackRecyclerView: RecyclerView
    private lateinit var feedbackArrayList: ArrayList<Feedback>

    private lateinit var feedbackAdapter : FeedbackAdapter

    private var _binding : FragmentAdminViewFeedbackBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminViewFeedbackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedbackRecyclerView = view.findViewById(R.id.recyclerViewAllFeedback)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this.context)
        feedbackRecyclerView.setHasFixedSize(true)
        feedbackArrayList = arrayListOf<Feedback>()
        getData()


    }

    private fun getData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Admin View All Feedback")

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackArrayList.clear()
                if (snapshot.exists()) {
                    for (feedbackSnaps in snapshot.children) {
                        val feedback =
                            feedbackSnaps.getValue(Feedback::class.java)
                        feedbackArrayList.add(feedback!!)
                    }
                    feedbackRecyclerView.adapter = FeedbackAdapter(feedbackArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}