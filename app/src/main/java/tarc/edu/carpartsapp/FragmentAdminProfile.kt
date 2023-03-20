package tarc.edu.carpartsapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import tarc.edu.carpartsapp.databinding.FragmentAdminProfileBinding
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding

class FragmentAdminProfile : Fragment() {
    private var _binding: FragmentAdminProfileBinding? = null
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
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

            binding.textViewFullNameAdmin.text = name
            binding.adminFullName.setText(name)
            binding.adminEmailAddress.setText(email)
            binding.adminAddress.setText(address)
            binding.adminPhoneNumber.setText(phoneNumber)
            binding.adminBirthDate.setText(birthDate)
            binding.adminGender.setText(gender)

            //binding.profilePicture.setOnClickListener {
            //  getPhoto.launch("image/*")
            //}
            // }

            binding.btnAdminSaveProfile.setOnClickListener {
                if(validateInput()){
                    saveProfileInformation()
                }
            }

        }

    }

    private fun validateInput():Boolean{

        val name = binding.adminFullName.text
        val address = binding.adminAddress.text
        val birthDate = binding.adminBirthDate.text
        val phoneNumber = binding.adminPhoneNumber.text

        if(name.isNullOrEmpty()){
            binding.adminFullName.error = "Please enter your full name"
            return false
        }
        if(phoneNumber!!.length > 11|| phoneNumber.isEmpty()){
            binding.adminPhoneNumber.error = "Please enter your correct phone number!"
            return false
        }
        if (address.isNullOrEmpty()) {
            binding.adminAddress.error = "Please enter your house address"
            return false
        }
        if (birthDate.isNullOrEmpty() || birthDate.length!="dd/MM/yyyy".length) {
            binding.adminBirthDate.error = "Please enter your birth date"
            return false
        }
        return true
    }

    fun saveProfileInformation() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val databaseReference =
            FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users/$userId")
        databaseReference.child("fullName").setValue(binding.textViewFullNameAdmin.text.toString())
        databaseReference.child("fullName").setValue(binding.adminFullName.text.toString())
        databaseReference.child("emailAddress").setValue(binding.adminEmailAddress.text.toString())
        databaseReference.child("address").setValue(binding.adminAddress.text.toString())
        databaseReference.child("phoneNumber").setValue(binding.adminPhoneNumber.text.toString())
        databaseReference.child("birthDate").setValue(binding.adminBirthDate.text.toString())
        databaseReference.child("gender").setValue(binding.adminGender.text.toString())
            .addOnSuccessListener {
                //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                //Toast.makeText(context, "Failed to edit profile", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}