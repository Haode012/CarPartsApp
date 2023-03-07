package tarc.edu.carpartsapp

import android.annotation.SuppressLint
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
import tarc.edu.carpartsapp.Model.Feedback
import tarc.edu.carpartsapp.databinding.FragmentCustomerFeedbackBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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

        binding.btnSubmit.setOnClickListener { task->
            validateFeedback()
                    storeFeedbacks()
                    Toast.makeText(context, "Rated Successfully", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_nav_feedback_customer_to_nav_home_customer)
               // }else{
                 //   Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                }
            }



    @SuppressLint("SimpleDateFormat")
    private fun validateFeedback(){

        val ratings = binding.ratingBar.rating.toString()
        val comments = binding.comments.text.toString()
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

        feedbacks["rating"] = ratings
        if(ratings.isEmpty()){
            Toast.makeText(context,"You will not like to rate our service.", Toast.LENGTH_LONG).show()
            //findNavController().navigate(R.id.action_customerFeedbackFragment_to_customerHomepage)
            }

        feedbacks["comment"] = comments
        if(comments.isEmpty()){
            binding.comments.error = "You have no made any comments"
        }

        feedbacks["userId"] = userId
        feedbacks["DateOfFeedback"] = saveCurrentDate

    }

   private fun storeFeedbacks(){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("Customer Feedback").push()
       val databaseReference2 = database.getReference("Admin View All Feedback").push()
       //val databaseReference = database.getReference("Feedback").child("$userId").push()
       //val databaseReference = database.getReference("Feedback").child("$userId").child("Feedback").push()
       val ref = databaseReference2.key
       feedbacks["feedbackId"] = ref.toString()
        try {
            databaseReference.setValue(feedbacks)
            databaseReference2.setValue(feedbacks)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}