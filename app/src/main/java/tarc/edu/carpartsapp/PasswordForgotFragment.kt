package tarc.edu.carpartsapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentPasswordForgotBinding

class PasswordForgotFragment : Fragment() {
    private var _binding: FragmentPasswordForgotBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPasswordForgotBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener{
          findNavController().navigate(R.id.action_passwordForgotFragment_to_registrationFragment)
        }

        binding.btnForgotPassword.setOnClickListener{
            forgotPassword()
        }
    }

    private fun validation(){
        val email = binding.editTextTextEmailAddress3.text.toString()
        if(email.isNullOrEmpty()){
            binding.editTextTextEmailAddress3.error = "Please enter your email address"
            Toast.makeText(context, "Please enter you email address", Toast.LENGTH_LONG).show()
        }
    }

    private fun forgotPassword(){
        auth= Firebase.auth
            val email = binding.editTextTextEmailAddress3.text.toString().trim{it <= ' '}
            if(email.isNullOrEmpty()){
                validation()
            }else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if(it.isSuccessful){
                       findNavController().navigate(R.id.action_passwordForgotFragment_to_loginFragment)
                        Toast.makeText(this.context, "Email was sent successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this.context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}