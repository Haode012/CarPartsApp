package tarc.edu.carpartsapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Admin.AdminActivity
import tarc.edu.carpartsapp.Customer.CustomerActivity
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                ref.child(firebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uid = snapshot.child("uid").value
                        val userType = snapshot.child("userType").value
                        if(userType == "Admin"){
                            val intent = Intent(context, AdminActivity::class.java)
                            startActivity(intent)
                        }else if(userType == "Customer"){
                            val intent = Intent(context, CustomerActivity::class.java)
                            startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}