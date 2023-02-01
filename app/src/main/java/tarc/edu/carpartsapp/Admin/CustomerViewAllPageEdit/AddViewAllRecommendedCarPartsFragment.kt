package tarc.edu.carpartsapp.Admin.CustomerViewAllPageEdit

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_popular_car_parts.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.Model.ViewAllRecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentAddViewAllRecommendedCarPartsBinding
import java.text.SimpleDateFormat
import java.util.*

class AddViewAllRecommendedCarPartsFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentAddViewAllRecommendedCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddViewAllRecommendedCarPartsBinding.inflate(inflater, container, false)
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
            val price = binding.editTextRecommendedCarPartPrice.text.toString().trim()
            val warranty = binding.spinnerRecommendedCarPartWarranty.selectedItem.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextRecommendedCarPartName.error = "Enter the car part name"
            }else if(description.isEmpty()){
                binding.editTextRecommendedCarPartDescription.error = "Enter the car part description"
            }else if(price.isEmpty()){
                binding.editTextRecommendedCarPartPrice.error = "Enter the car part price"
            }else if(!price.matches(Regex("-?\\d+(\\.\\d+)?"))){
                binding.editTextRecommendedCarPartPrice.error = "Please enter a valid double number"
            }else if(warranty.isEmpty()){
                Toast.makeText(requireContext(), "Choose a expiration years", Toast.LENGTH_SHORT).show()
            }else{
                addDataToFirebase(name, description, price, warranty, imageUri!!)
            }
        }
    }

    private fun addDataToFirebase(name: String, description: String, price: String, warranty: String, uri: Uri) {
        val hashMap = HashMap<String, Any>()
        hashMap["name"] = "$name"
        hashMap["description"] = "$description"
        hashMap["price"] = "$price"
        hashMap["warranty"] = "$warranty"
        val ref = FirebaseDatabase.getInstance().getReference("ViewAllRecommended")
        val key = ref.push().key
        hashMap["id"] = "$key"
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