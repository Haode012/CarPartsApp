package tarc.edu.carpartsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_admin_return_product_decision.*
import tarc.edu.carpartsapp.databinding.FragmentAdminReturnProductDecisionBinding

class FragmentAdminReturnProductDecision : Fragment() {
    private var _binding: FragmentAdminReturnProductDecisionBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminReturnProductDecisionBinding.inflate(inflater, container, false)

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val img_url = requireArguments().getString("img_url").toString()
        val uid = requireArguments().getString("uid").toString()
        val orderId = requireArguments().getString("order_id").toString()
        binding.outputReturnProductName.text = "Return Product Name: "+ name
        binding.outputWarrantyPeriod.text = "Warranty Period:  " + warranty +" Years"
        binding.outputReturnProductId.text = id
        binding.outputUserId.text = uid
        binding.outputOrderId.text = orderId
        Glide.with(requireContext()).load(img_url).into(binding.imageView4)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = binding.outputUserId.text.toString()
        val returnProductId = binding.outputReturnProductId.text.toString()
        val name = binding.outputReturnProductName.text.toString()
        val orderId = binding.outputOrderId.text.toString()

        val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        //val ref2 = database.getReference("ReturnProductStatus")

        btnAccept.setOnClickListener{
            //val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
           // val ref = database.getReference("RequestReturnProduct")
            val ref2 = database.getReference("ReturnProductStatus").child(userId).child(orderId).child(returnProductId)
            ref2.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                   ref2.child("status").setValue("Accepted (Pls call to\n office 03-12343212 to\n ask more information,\n thank you!!)")
                    Toast.makeText(context, "Return Product Request Accepted", Toast.LENGTH_SHORT).show()
                }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            deleteData(userId, orderId, returnProductId)

            findNavController().navigate(R.id.action_fragmentAdminReturnProductDecision_to_nav_home_admin)

        }

        btnReject.setOnClickListener {
            val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val ref = database.getReference("ReturnProductStatus").child(userId).child(orderId).child(returnProductId)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ref.child("status").setValue("Rejected (Sorry your ordered\n item has over the\n warranty period)")
                    Toast.makeText(context, "Return Product Request Rejected", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            deleteData2(userId, orderId, returnProductId)

            findNavController().navigate(R.id.action_fragmentAdminReturnProductDecision_to_nav_home_admin)
        }
    }

    private fun deleteData2(userId: String, orderId: String, returnProductId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("RequestReturnProduct").child(userId).child(orderId).child(returnProductId)
        ref.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }

    private fun deleteData(userId: String, orderId: String, returnProductId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("RequestReturnProduct").child(userId).child(orderId).child(returnProductId)
        ref.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


