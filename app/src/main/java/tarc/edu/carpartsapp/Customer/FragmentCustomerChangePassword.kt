package tarc.edu.carpartsapp.Customer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCustomerChangePasswordBinding


class FragmentCustomerChangePassword : Fragment() {

    private var _binding: FragmentCustomerChangePasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var auth1: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCustomerChangePasswordBinding.inflate(inflater, container, false)


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnUpdatePasswd.setOnClickListener {
            if(validation()){
                resetPassword()
            }
        }

    }

    private fun validation():Boolean{
        val passwd = binding.newPassword.text.toString()
        val confirmPasswd = binding.confirmNewPassword.text.toString()

        if(passwd.isNullOrEmpty()){
            binding.newPassword.error = "Please enter your new password"
            return false
        }

        if(passwd.length < 8){
            binding.newPassword.error = "Password length must be 8 or more characters"
            return false
        }
        if(passwd.isNullOrEmpty()){
            binding.confirmNewPassword.error = "Please enter your confirm password"
            return false
        }

        if(passwd != confirmPasswd){
            binding.confirmNewPassword.error = "Your new password does not match with the confirm password"
            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    private fun resetPassword(){
        val passwd = binding.newPassword.text.toString()
        val confirmPasswd = binding.confirmNewPassword.text.toString()
        val result = binding.result.text
        val userId = FirebaseAuth.getInstance().currentUser
        //val text = binding.result.text

        userId!!.updatePassword(confirmPasswd).addOnCompleteListener { task ->
            if(task.isSuccessful){
                findNavController().navigate(R.id.action_fragmentCustomerChangePassword_to_nav_profile_customer)
            }else{
                binding.result.text = "Your password was not changed successfully"
                binding.result.setTextColor(Color.parseColor("#FE0000"))

            }
        }
    }

}