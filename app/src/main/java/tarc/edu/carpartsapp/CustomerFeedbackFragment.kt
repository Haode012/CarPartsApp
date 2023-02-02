package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentCustomerFeedbackBinding
import java.lang.Exception

class CustomerFeedbackFragment : Fragment() {
    private var _binding: FragmentCustomerFeedbackBinding? = null
    private lateinit var feedbacks: HashMap<String, String>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        feedbacks = HashMap()

        _binding = FragmentCustomerFeedbackBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ratings = binding.ratingBar.rating.toString()
        val comments = binding.comments.text.toString()

        binding.btnSubmit.setOnClickListener {
//            val database = Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")
//            database.getReference("Feedback").child("feedback"+1).get().addOnCompleteListener { task ->
                //if(task.isSuccessful){
                    feedback()
                    storeFeedbacks()
                    Toast.makeText(context, "Rated Successfully", Toast.LENGTH_LONG)
                    //findNavController().navigate(R.id.action_customerFeedbackFragment_to_customerHomepage)
               // }else{
                   // Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                //}
            }
        }
   // }

    private fun feedback(){

        val ratings = binding.ratingBar.rating.toString()
        val comments = binding.comments.text.toString()

        feedbacks["rating"] = ratings
        if(ratings.isNullOrEmpty()){
            Toast.makeText(context,"You will not like to rate our service.", Toast.LENGTH_LONG)
            //findNavController().navigate(R.id.action_customerFeedbackFragment_to_customerHomepage)
            }

        feedbacks["comment"] = comments
        if(comments.isNullOrEmpty()){
            binding.comments.error = "You have no made any comments"
        }

        feedbacks["userId"] = userId

    }

   private fun storeFeedbacks(){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("Feedback").push()
       //val databaseReference = database.getReference("Feedback").child("$userId").push()
       //val databaseReference = database.getReference("Feedback").child("$userId").child("Feedback").push()

        try {
            databaseReference.setValue(feedbacks)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}