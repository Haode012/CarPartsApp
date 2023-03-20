package tarc.edu.carpartsapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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

              binding.btnEditProfile.setOnClickListener {
                  findNavController().navigate(R.id.action_nav_profile_customer_to_fragmentEditProfile2)
              }

            binding.btnChangeProfilePic.setOnClickListener {
                findNavController().navigate(R.id.action_nav_profile_customer_to_fragmentChangeProfilePicture)
            }

            binding.resetPasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_nav_profile_customer_to_fragmentCustomerChangePassword)
            }
            //   ref.child("fullName").setValue(binding.fullName.text.toString())
            //   ref.child("emailAddress").setValue(binding.emailAddress.text.toString())
            //   ref.child("address").setValue(binding.homeAddress.text.toString())
            //ref.setValue(binding.emailAddress.text.toString())
            // ref.setValue(binding.emailAddress.text.toString())
        }

    }

    private fun uploadInfo() {
    }

    private fun storeData(imageUri: UploadTask.TaskSnapshot) {


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}