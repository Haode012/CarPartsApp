package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import tarc.edu.carpartsapp.databinding.FragmentAddRecommendedCarPartsBinding
import java.util.*

class AddRecommendedCarPartsFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentAddRecommendedCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddRecommendedCarPartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewRecommendedCarPartImage.setOnClickListener(){
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }

        binding.buttonConfirmAddRecommendedCarPart.setOnClickListener(){

            val name = binding.editTextRecommendedCarPartName.text.toString().trim()
            val description = binding.editTextRecommendedCarPartDescription.text.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextRecommendedCarPartName.error = "Enter the car part name"
            }else if (name.length < 3) {
                binding.editTextRecommendedCarPartName.error =
                    "Car part name must have at least three character"
            } else if (!name.matches(Regex("^[a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextRecommendedCarPartName.error =
                    "Car part name must start with three letter"
            } else if(description.isEmpty()){
                binding.editTextRecommendedCarPartDescription.error = "Enter the car part description"
            } else if (description.length < 3) {
                binding.editTextRecommendedCarPartDescription.error =
                    "Car part description must have at least three character"
            } else if (!description.matches(Regex("^[a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextRecommendedCarPartDescription.error =
                    "Car part description must start with three letter"
            }else {
                addDataToFirebase(name, description, imageUri!!)
            }
        }
    }

    private fun addDataToFirebase(name: String, description: String, uri: Uri) {

    try{
        val ref = FirebaseDatabase.getInstance().getReference("Recommended")
        val key = ref.push().key
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$key"
        hashMap["name"] = "$name"
        hashMap["description"] = "$description"
        if (key != null) {
            ref.child(key).setValue(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_SHORT).show()
                        uploadImage(uri, key) { downloadUrl ->
                            hashMap["img_url"] = downloadUrl
                            ref.child("$key").updateChildren(hashMap)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    } catch (e: Exception){

    }
    }

    private fun uploadImage(uri: Uri, key: String , callback: (String) -> Unit) {
        val fileRef = reference.child("$key.${getFileExtension(uri)}")
        fileRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                callback(downloadUrl.toString())
                Toast.makeText(requireContext(), "Uploaded Image Successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnProgressListener { snapshot ->
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Uploading Image Failed !!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.data
            binding.imageViewRecommendedCarPartImage.setImageURI(imageUri)
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr: ContentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }


}