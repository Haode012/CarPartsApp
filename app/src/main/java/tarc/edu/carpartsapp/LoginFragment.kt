package tarc.edu.carpartsapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Value
import tarc.edu.carpartsapp.Admin.AdminActivity
import tarc.edu.carpartsapp.Customer.CustomerActivity
import tarc.edu.carpartsapp.databinding.FragmentLoginBinding
import kotlin.Exception

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var auth1: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //If user previously logged in and didnt sign out, allow login synchronously
            val database =
                Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            auth1 = Firebase.auth
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(auth1.uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uid = snapshot.child("uid").value
                        val name = snapshot.child("fullName").value
                        val userType = snapshot.child("userType").value
                        if (userType == "Admin") {
                            val intent = Intent(context, AdminActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(context, "Welcome Back "+name, Toast.LENGTH_LONG).show()
                        } else if (userType == "Customer") {
                            val intent = Intent(context, CustomerActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(context, "Welcome Back "+name, Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

        //If user previously logged in and didnt sign out, allow login synchronously
//        auth1 = Firebase.auth
//        if (auth1.currentUser !== null) {
//            val intent = Intent(context, AdminActivity::class.java)
//            startActivity(intent)
//
//        }

        //auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.textViewCreateAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordForgotFragment)
        }

        return binding.root


    }

    private var email = ""
    private var password = ""

    private fun validation() {
        email = binding.editTextTextEmailAddress.text.toString()
        password = binding.editTextTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email and Password cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email and Password cannot be blank", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {


                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                val database =
                    Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val firebaseUser = auth.currentUser!!
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.child(firebaseUser.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val uid = snapshot.child("uid").value
                            val userType = snapshot.child("userType").value
                            if (userType == "Admin") {
                                val intent = Intent(context, AdminActivity::class.java)
                                startActivity(intent)
                            } else if (userType == "Customer") {
                                    val intent = Intent(context, CustomerActivity::class.java)
                                    startActivity(intent)

                                    getData()


                            }
//                        if (snapshot.value == "Admin") {
//                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(context, AdminMainActivityNew::class.java)
//                            startActivity(intent)
//                        } else if (snapshot.value == "Customer") {
//                            findNavController().navigate(R.id.action_loginFragment_to_customerHomepage)
//                        }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }.addOnFailureListener {
                Toast.makeText(context, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        try {
            val id = requireArguments().getString("id").toString()
            val name = requireArguments().getString("name").toString()
            val price = requireArguments().getString("price").toString()
            val warranty = requireArguments().getString("warranty").toString()
            val total_price = requireArguments().getString("total_price").toString()
            val total_quantity = requireArguments().getString("total_quantity").toString()
            val img_url = requireArguments().getString("img_url").toString()
            val currentDate = requireArguments().getString("currentDate").toString()
            val currentTime = requireArguments().getString("currentTime").toString()

                val hashMap = HashMap<String, Any>()
                hashMap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid
                hashMap["id"] = "$id"
                hashMap["name"] = "$name"
                hashMap["price"] = "$price"
                hashMap["warranty"] = "$warranty"
                hashMap["total_price"] = "$total_price"
                hashMap["total_quantity"] = "$total_quantity"
                hashMap["img_url"] = "$img_url"
                hashMap["currentDate"] = "$currentDate"
                hashMap["currentTime"] = "$currentTime"

                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("CartItem")

                reference.child(FirebaseAuth.getInstance().currentUser!!.uid).child(id)
                    .setValue(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
        } catch (e: IllegalArgumentException){
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException){
//            Toast.makeText(requireContext(), "error2", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}