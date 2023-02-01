package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.R
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

        binding.button2.setOnClickListener{
            forgotPassword()
        }
    }

    private fun forgotPassword(){
        auth= Firebase.auth

        val buttonForgot = binding.button2

        buttonForgot.setOnClickListener{
            val email = binding.editTextTextEmailAddress3.text.toString().trim{it <= ' '}
            if(email.isNullOrEmpty()){
                Toast.makeText(this.context, "Please enter you email address", Toast.LENGTH_LONG).show()
            }else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if(it.isSuccessful){
                       findNavController().navigate(R.id.action_passwordForgotFragment_to_loginFragment)
                        Toast.makeText(this.context, "Email was sent successfully", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this.context, "Please enter a valid email address", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}