package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tarc.edu.carpartsapp.Model.Users
import tarc.edu.carpartsapp.databinding.FragmentRegistrationBinding
import java.lang.Exception

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userProfile: HashMap<String, String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)


        userProfile = HashMap()

        binding.button.setOnClickListener{
            validateInput()
            //createUserProfile()
            performRegister()
        }

        return binding.root

    }
    private fun validateInput():Boolean{
        val fullName = binding.editTextFullName.text.toString()
        val address = binding.editTextAddress.text.toString()
        val phoneNum = binding.editTextPhone.text.toString()
        val dateOfBirth = binding.editTextDate.text.toString()
        val email = binding.editTextEmail.text.toString()
        val passwd = binding.editTextPasswd.text.toString()

        userProfile["emailAddress"] = email
        if(email.isNullOrEmpty()){
            binding.editTextEmail.error = "Email Address is incorrect"
            return false
        }
        userProfile["phoneNumber"] = phoneNum
        if(phoneNum.isNullOrEmpty()){
            binding.editTextPhone.error = "Phone Number shall not be blank"
            return false
        }
        userProfile["birthDate"] = dateOfBirth
        if(dateOfBirth.isNullOrEmpty()){
            binding.editTextDate.error = "Date of birth shall not be blank"
            return false
        }
        userProfile["fullName"] = fullName
        if(fullName.isNullOrEmpty()){
            binding.editTextFullName.error = "Full name must be entered"
            return false
        }

        if(passwd.isNullOrEmpty()){
            binding.editTextPasswd.error = "Password must be entered"
            return false
        }
        userProfile["address"] = address
        if(address.isNullOrEmpty()){
            binding.editTextAddress.error = "Address must be entered"
            return false
        }
        return true
    }


    private fun performRegister() {
        val email = binding.editTextEmail.text.toString()
        val passwd = binding.editTextPasswd.text.toString()

        //val fullName = binding.editTextFullName.text.toString()
        val address = binding.editTextAddress.text.toString()
        val phoneNum = binding.editTextPhone.text.toString()
        val dateOfBirth = binding.editTextDate.text.toString()
        // val email = binding.editTextEmail.toString()
        //val passwd = binding.editTextPasswd.toString()

        val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Users")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fullName = binding.editTextFullName.text.toString()
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    userProfile(task.result.user?.uid!!)
                    if(binding.radioButtonMale.isChecked){
                        ref.child(firebaseUser.uid).setValue(Users(
                            "$fullName", "$email", "$address",
                            "$phoneNum", "$dateOfBirth", "Male",
                            "Customer"

                        ))
                    }else{
                        ref.child(firebaseUser.uid).setValue(Users("$fullName", "$email", "$address",
                            "$phoneNum", "$dateOfBirth", "Female",
                            "Customer"))
                    }
                    Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                } else {
                    Toast.makeText(context, "Error: ", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun userProfile(userId: String):Boolean{
        GlobalScope.launch {
            val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val databaseReference = database.getReference("Users/$userId")

            try {
                databaseReference.setValue(userProfile)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
    /*private fun createUserProfile(userId: String): Boolean {
        GlobalScope.launch {
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("users/$userId")

            try {
                databaseReference.setValue(userProfile)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}