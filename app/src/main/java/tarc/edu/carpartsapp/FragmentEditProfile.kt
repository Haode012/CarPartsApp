package tarc.edu.carpartsapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import tarc.edu.carpartsapp.databinding.FragmentEditProfileBinding
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat

class FragmentEditProfile : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private var imageUri: Uri? = null
    private var storageRef = Firebase.storage

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val auth = FirebaseAuth.getInstance()

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
            val profilePic = it.child("profilePicture").child("url").value.toString()
            Glide.with(this).load(profilePic).into(binding.imageView6)

            binding.textViewLabelBigName.text = name
            binding.textInputFullName.setText(name)
            binding.textInputEmail.setText(email)
            binding.textInputHomeAddress.setText(address)
            binding.textInputPhone.setText(phoneNumber)
            binding.textInputDateOfBirth.setText(birthDate)
            binding.textInputGender.setText(gender)


            //binding.profilePicture.setOnClickListener {
            //  getPhoto.launch("image/*")
            //}
            // }

            //  binding.button4.setOnClickListener {
            //   ref.child("fullName").setValue(binding.fullName.text.toString())
            //   ref.child("emailAddress").setValue(binding.emailAddress.text.toString())
            //   ref.child("address").setValue(binding.homeAddress.text.toString())
            //ref.setValue(binding.emailAddress.text.toString())
            // ref.setValue(binding.emailAddress.text.toString())
        }


        binding.buttonSaveProfile.setOnClickListener {
            if(validateInput()){
                saveProfileInformation()
            }
            //saveProfileInformation()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun validateInput():Boolean{

        val name = binding.textInputFullName.text
        val address = binding.textInputHomeAddress.text
        val birthDate = binding.textInputDateOfBirth.text
        val phoneNumber = binding.textInputPhone.text

        if(name.isNullOrEmpty()){
            binding.textInputFullName.error = "Please enter your full name"
            return false
        }
        if(phoneNumber!!.length > 11|| phoneNumber.isEmpty()){
            binding.textInputPhone.error = "Please enter your correct phone number!"
            return false
        }
        if (address.isNullOrEmpty()) {
            binding.textInputHomeAddress.error = "Please enter your house address"
            return false
        }
        if (birthDate.isNullOrEmpty() || birthDate.length!="dd/MM/yyyy".length) {
            binding.textInputDateOfBirth.error = "Please enter your birth date"
            return false
        }
        return true
    }

    fun saveProfileInformation() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val databaseReference =
            FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users/$userId")
        databaseReference.child("fullName").setValue(binding.textViewLabelBigName.text.toString())
        databaseReference.child("fullName").setValue(binding.textInputFullName.text.toString())
        databaseReference.child("emailAddress").setValue(binding.textInputEmail.text.toString())
        databaseReference.child("address").setValue(binding.textInputHomeAddress.text.toString())
        databaseReference.child("phoneNumber").setValue(binding.textInputPhone.text.toString())
        databaseReference.child("birthDate").setValue(binding.textInputDateOfBirth.text.toString())
        databaseReference.child("gender").setValue(binding.textInputGender.text.toString())
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_fragmentEditProfile2_to_nav_profile_customer)
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to edit profile", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateProfilePicture() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        storageRef.getReference("Images").child(System.currentTimeMillis().toString())
            .putFile(imageUri!!).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    val imageMap = mapOf("url" to uri.toString())
                    //val databaseReference = Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    //val ref = databaseReference.getReference("Users/$userId")
                    //ref.child("profilePicture").setValue(imageMap)
                    val databaseReference =
                        FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Users/$userId")
                    databaseReference.child("profilePicture").setValue(imageMap)
                    findNavController().navigate(R.id.action_fragmentEditProfile2_to_nav_profile_customer)
                    Toast.makeText(context, "Profile Picture Updated", Toast.LENGTH_LONG).show()

                }

            }
    }
}


