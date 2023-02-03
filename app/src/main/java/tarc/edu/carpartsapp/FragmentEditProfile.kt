package tarc.edu.carpartsapp

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import tarc.edu.carpartsapp.databinding.FragmentEditProfileBinding
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding

class FragmentEditProfile : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val image: ImageView = binding.profilePicture

        //image.setImageResource(R.drawable.ic_baseline_person_24)

        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Users/$userId")

        ref.get().addOnSuccessListener {
            val name = it.child("fullName").value as String
            val email = it.child("emailAddress").value as String
            val address = it.child("address").value as String
            val phoneNumber = it.child("phoneNumber").value as String
            val birthDate = it.child("birthDate").value as String
            val gender = it.child("gender").value as String

            binding.textViewLabelBigName.text = name
            binding.textInputFullName.setText(name)
            binding.textInputEmail.setText(email)
            binding.textInputHomeAddress.setText(address)
            binding.textInputPhone.setText(phoneNumber)
            binding.textInputDateOfBirth.setText(birthDate)
            binding.textInputGender.setText(gender)
        }

        binding.buttonProfile.setOnClickListener {

              ref.child("fullName").setValue(binding.textInputFullName.text.toString())
            //ref.child("fullName").setValue(binding.textViewLabelBigName.text)
               ref.child("emailAddress").setValue(binding.textInputEmail.text.toString())
               ref.child("address").setValue(binding.textInputHomeAddress.text.toString())
            ref.child("phoneNumber").setValue(binding.textInputPhone.text.toString())
            ref.child("birthDate").setValue(binding.textInputDateOfBirth.text.toString())
            ref.child("gender").setValue(binding.textInputGender.text.toString())
            Toast.makeText(context,"User Profile Updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_fragmentEditProfile2_to_nav_profile_customer)
        }
    }

}
