package tarc.edu.carpartsapp

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
import com.google.firebase.storage.ktx.storage
import tarc.edu.carpartsapp.databinding.FragmentChangeProfilePictureBinding
import tarc.edu.carpartsapp.databinding.FragmentEditProfileBinding


class FragmentChangeProfilePicture : Fragment() {
    private var _binding: FragmentChangeProfilePictureBinding? = null
    private var imageUri: Uri? = null
    private var storageRef = Firebase.storage

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangeProfilePictureBinding.inflate(inflater, container, false)
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
            val profilePic = it.child("profilePicture").child("url").value.toString()
            Glide.with(this).load(profilePic).into(binding.outputProfilePicture)

        }

        val galleryImg = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.outputProfilePicture.setImageURI(it)
                imageUri = it
            }
        )

        binding.outputProfilePicture.setOnClickListener {
            galleryImg.launch("image/*")
        }

        binding.btnSaveProfilePic.setOnClickListener {
            updateProfilePicture()
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
                    findNavController().navigate(R.id.action_fragmentChangeProfilePicture_to_nav_profile_customer)
                    Toast.makeText(context, "Profile Picture Updated", Toast.LENGTH_LONG).show()

                }

            }
    }
}