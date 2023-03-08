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
import tarc.edu.carpartsapp.databinding.FragmentEditDeleteViewAllCarPartsCategoryBinding

class EditDeleteViewAllCarPartsCategoryFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentEditDeleteViewAllCarPartsCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditDeleteViewAllCarPartsCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val type = requireArguments().getString("type").toString()
        val description = requireArguments().getString("description").toString()
        val price = requireArguments().getString("price").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val img_url = requireArguments().getString("img_url").toString()

        binding.editTextCarPartCategoryId.setText(id)
        binding.editTextCarPartCategoryName.setText(name)
        binding.editTextCarPartCategoryType.setText(type)
        binding.editTextCarPartCategoryDescription.setText(description)
        binding.editTextCarPartCategoryPrice.setText(price)

        if(warranty == "1"){
            binding.spinnerCarPartCategoryWarranty.setSelection(0)
        } else if(warranty == "1.5"){
            binding.spinnerCarPartCategoryWarranty.setSelection(1)
        } else if(warranty == "2"){
            binding.spinnerCarPartCategoryWarranty.setSelection(2)
        } else if(warranty == "2.5"){
            binding.spinnerCarPartCategoryWarranty.setSelection(3)
        } else {
            binding.spinnerCarPartCategoryWarranty.setSelection(4)
        }

        Glide.with(requireContext()).load(img_url).into(binding.imageViewCarPartCategoryImage)

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

        binding.buttonEditCarPartCategory.setOnClickListener(){

            val id = binding.editTextCarPartCategoryId.text.toString().trim()
            val name = binding.editTextCarPartCategoryName.text.toString().trim()
            val type = binding.editTextCarPartCategoryType.text.toString().trim()
            val description = binding.editTextCarPartCategoryDescription.text.toString().trim()
            val price = binding.editTextCarPartCategoryPrice.text.toString().trim()
            val warranty = binding.spinnerCarPartCategoryWarranty.selectedItem.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextCarPartCategoryName.error = "Enter the car part name"
            }else if(description.isEmpty()){
                binding.editTextCarPartCategoryDescription.error = "Enter the car part description"
            }else if(type.isEmpty()){
                binding.editTextCarPartCategoryType.error = "Enter the car part type"
            }else if(price.isEmpty()){
                binding.editTextCarPartCategoryPrice.error = "Enter the car part price"
            }else if(!price.matches(Regex("-?\\d+(\\.\\d+)?"))){
                binding.editTextCarPartCategoryPrice.error = "Please enter a valid double number"
            }else if(warranty.isEmpty()){
                Toast.makeText(requireContext(), "Choose a expiration years", Toast.LENGTH_SHORT).show()
            }else{
                editData(id, name, description, type, price, warranty, imageUri!!)
            }
        }

        binding.buttonDeleteCarPartCategory.setOnClickListener() {

            val id = binding.editTextCarPartCategoryId.text.toString().trim()

            deleteData(id)
        }
    }

    private fun editData(id: String, name: String, description: String, type: String, price: String, warranty: String, uri: Uri) {
        try {
            val hashMap = HashMap<String, Any>()
            hashMap["id"] = "$id"
            hashMap["name"] = "$name"
            hashMap["description"] = "$description"
            hashMap["type"] = "$type"
            hashMap["price"] = "$price"
            hashMap["warranty"] = "$warranty"
            val ref = FirebaseDatabase.getInstance().getReference("ViewAllCategory").child(id)
            ref.updateChildren(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Edited Successfully", Toast.LENGTH_SHORT)
                            .show()
                        if (uri != null) {
                            editImage(uri, id) { downloadUrl ->
                                hashMap["img_url"] = downloadUrl
                                ref.updateChildren(hashMap)
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to Edit", Toast.LENGTH_SHORT)
                            .show()
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
        val ref = FirebaseDatabase.getInstance().getReference("ViewAllCategory").child(id)
        ref.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            }

        binding.editTextCarPartCategoryId.setText("")
        binding.editTextCarPartCategoryName.setText("")
        binding.editTextCarPartCategoryDescription.setText("")
        binding.editTextCarPartCategoryType.setText("")
        binding.editTextCarPartCategoryPrice.setText("")
        Glide.with(requireContext()).clear(binding.imageViewCarPartCategoryImage)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==2 && resultCode == Activity.RESULT_OK && data != null){

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