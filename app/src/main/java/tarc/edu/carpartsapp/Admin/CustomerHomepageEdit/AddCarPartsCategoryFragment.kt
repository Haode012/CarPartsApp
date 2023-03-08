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
import tarc.edu.carpartsapp.databinding.FragmentAddCarPartsCategoryBinding
import java.util.*

class AddCarPartsCategoryFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentAddCarPartsCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddCarPartsCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewCarPartCategoryImage.setOnClickListener(){
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }

        binding.buttonConfirmAddCarPartCategory.setOnClickListener(){
            val name = binding.editTextCarPartCategoryName.text.toString().trim()
            val type = binding.editTextCarPartCategoryType.text.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextCarPartCategoryName.error = "Enter the car part name"
            }else if(type.isEmpty()){
                binding.editTextCarPartCategoryType.error = "Enter the car part type"
            }else {

                addDataToFirebase(name, type, imageUri!!)

            }
        }
    }

    private fun addDataToFirebase(name: String, type: String, uri: Uri) {
        try{
            val ref = FirebaseDatabase.getInstance().getReference("CarPartsCategory")
            val key = ref.push().key
            val hashMap = HashMap<String, Any>()
            hashMap["id"] = "$key"
            hashMap["name"] = "$name"
            hashMap["type"] = "$type"
            if (key != null) {
                ref.child(key).setValue(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Added Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            uploadImage(uri, key) { downloadUrl ->
                                hashMap["img_url"] = downloadUrl
                                ref.child("$key").updateChildren(hashMap)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT)
                                .show()
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
            binding.imageViewCarPartCategoryImage.setImageURI(imageUri)
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr: ContentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }

}