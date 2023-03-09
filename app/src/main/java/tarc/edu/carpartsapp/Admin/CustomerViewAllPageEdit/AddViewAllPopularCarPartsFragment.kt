package tarc.edu.carpartsapp.Admin.CustomerViewAllPageEdit

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
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
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.Model.ViewAllRecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentAddViewAllPopularCarPartsBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AddViewAllPopularCarPartsFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentAddViewAllPopularCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddViewAllPopularCarPartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewPopularCarPartImage.setOnClickListener(){
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }

        binding.buttonConfirmAddPopularCarPart.setOnClickListener(){
            val name = binding.editTextPopularCarPartName.text.toString().trim()
            val description = binding.editTextPopularCarPartDescription.text.toString().trim()
            val price = binding.editTextPopularCarPartPrice.text.toString().trim()
            val warranty = binding.spinnerPopularCarPartWarranty.selectedItem.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextPopularCarPartName.error = "Enter the car part name"
            } else if (name.length < 3) {
                binding.editTextPopularCarPartName.error = "Car part name must have at least three character"
            } else if (!name.matches(Regex("^[a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextPopularCarPartName.error = "Car part name must start with three letter"
            } else if(description.isEmpty()){
                binding.editTextPopularCarPartDescription.error = "Enter the car part description"
            }  else if (description.length < 3) {
                binding.editTextPopularCarPartDescription.error = "Car part description must have at least three character"
            } else if (!description.matches(Regex("^[a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextPopularCarPartDescription.error = "Car part description must start with three letter"
            } else if(price.isEmpty()){
                binding.editTextPopularCarPartPrice.error = "Enter the car part price"
            } else if(!price.matches(Regex("-?\\d+(\\.\\d+)?"))) {
                binding.editTextPopularCarPartPrice.error = "Car part price can't contain letters"
            } else {
                    val priceDouble = price.toDouble()
                    val df = DecimalFormat("0.00")
                    val priceString = df.format(priceDouble).toString()

                    binding.editTextPopularCarPartPrice.setText(priceString)
                    if (warranty.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Choose a expiration years",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        addDataToFirebase(name, description, priceString, warranty, imageUri!!)
                    }
                }
        }
    }

    private fun addDataToFirebase(name: String, description: String, priceString: String, warranty: String, uri: Uri) {
        try{
        val ref = FirebaseDatabase.getInstance().getReference("ViewAllPopular")
        val key = ref.push().key
            val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$key"
            hashMap["name"] = "$name"
            hashMap["description"] = "$description"
            hashMap["price"] = "$priceString"
            hashMap["warranty"] = "$warranty"
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
        } catch (e: Exception) {

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
            binding.imageViewPopularCarPartImage.setImageURI(imageUri)
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr: ContentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }
}