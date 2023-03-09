package tarc.edu.carpartsapp.Admin.CustomerViewAllPageEdit

import android.app.Activity
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
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import tarc.edu.carpartsapp.databinding.FragmentEditDeleteViewAllRecommendedCarPartsBinding
import java.text.DecimalFormat

class EditDeleteViewAllRecommendedCarPartsFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentEditDeleteViewAllRecommendedCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditDeleteViewAllRecommendedCarPartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val description =  requireArguments().getString("description").toString()
        val price = requireArguments().getString("price").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val img_url = requireArguments().getString("img_url").toString()

        binding.editTextRecommendedCarPartId.setText(id)
        binding.editTextRecommendedCarPartName.setText(name)
        binding.editTextRecommendedCarPartDescription.setText(description)
        binding.editTextRecommendedCarPartPrice.setText(price)

        if(warranty == "1"){
            binding.spinnerRecommendedCarPartWarranty.setSelection(0)
        } else if(warranty == "1.5"){
            binding.spinnerRecommendedCarPartWarranty.setSelection(1)
        } else if(warranty == "2"){
            binding.spinnerRecommendedCarPartWarranty.setSelection(2)
        } else if(warranty == "2.5"){
            binding.spinnerRecommendedCarPartWarranty.setSelection(3)
        } else {
            binding.spinnerRecommendedCarPartWarranty.setSelection(4)
        }

        Glide.with(requireContext()).load(img_url).into(binding.imageViewRecommendedCarPartImage)

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

        binding.buttonEditRecommendedCarPart.setOnClickListener(){

            val id = binding.editTextRecommendedCarPartId.text.toString().trim()
            val name = binding.editTextRecommendedCarPartName.text.toString().trim()
            val description = binding.editTextRecommendedCarPartDescription.text.toString().trim()
            val price = binding.editTextRecommendedCarPartPrice.text.toString().trim()
            val warranty = binding.spinnerRecommendedCarPartWarranty.selectedItem.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextRecommendedCarPartName.error = "Enter the car part name"
            }else if (name.length < 3) {
                binding.editTextRecommendedCarPartName.error = "Car part name must have at least three character"
            } else if (!name.matches(Regex("^[a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextRecommendedCarPartName.error = "Car part name must start with three letter"
            } else if(description.isEmpty()){
                binding.editTextRecommendedCarPartDescription.error = "Enter the car part description"
            }  else if (description.length < 3) {
                binding.editTextRecommendedCarPartDescription.error = "Car part description must have at least three character"
            } else if (!description.matches(Regex("^[a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextRecommendedCarPartDescription.error = "Car part description must start with three letter"
            } else if(price.isEmpty()){
                binding.editTextRecommendedCarPartPrice.error = "Enter the car part price"
            }else if(!price.matches(Regex("-?\\d+(\\.\\d+)?"))){
                binding.editTextRecommendedCarPartPrice.error = "Please enter a valid double number"
            } else {
                val priceDouble = price.toDouble()
                val df = DecimalFormat("0.00")
                val priceString = df.format(priceDouble).toString()

                binding.editTextRecommendedCarPartPrice.setText(priceString)
                if (warranty.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Choose a expiration years",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    editData(id, name, description, priceString, warranty, imageUri!!)
                }
            }
        }

        binding.buttonDeleteRecommendedCarPart.setOnClickListener() {

            val id = binding.editTextRecommendedCarPartId.text.toString().trim()

            deleteData(id)
        }
    }

    private fun editData(id: String, name: String, description: String, price: String, warranty: String, uri: Uri) {
        try{
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$id"
        hashMap["name"] = "$name"
        hashMap["description"] = "$description"
        hashMap["price"] = "$price"
        hashMap["warranty"] = "$warranty"
        val ref = FirebaseDatabase.getInstance().getReference("ViewAllRecommended").child(id)
        ref.updateChildren(hashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Edited Successfully", Toast.LENGTH_SHORT).show()
                    if (uri != null) {
                        editImage(uri, id) { downloadUrl ->
                            hashMap["img_url"] = downloadUrl
                            ref.updateChildren(hashMap)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to Edit", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {

        }
    }


    private fun editImage(uri: Uri, id: String, callback: (String) -> Unit) {
        val fileRef = reference.child("$id.${getFileExtension(uri)}")
        fileRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                callback(downloadUrl.toString())
                Toast.makeText(requireContext(), "Edited Image Successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnProgressListener { snapshot ->
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Editing Image Failed !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData(id: String) {
        val ref = FirebaseDatabase.getInstance().getReference("ViewAllRecommended").child(id)
        ref.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            }

        binding.editTextRecommendedCarPartId.setText("")
        binding.editTextRecommendedCarPartName.setText("")
        binding.editTextRecommendedCarPartDescription.setText("")
        binding.editTextRecommendedCarPartPrice.setText("")
        Glide.with(requireContext()).clear(binding.imageViewRecommendedCarPartImage)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==2 && resultCode == Activity.RESULT_OK && data != null){

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