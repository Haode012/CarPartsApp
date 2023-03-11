package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import tarc.edu.carpartsapp.Adapter.PopularAdapter
import tarc.edu.carpartsapp.databinding.FragmentEditDeletePopularCarPartsBinding
import java.net.URL

class EditDeletePopularCarPartsFragment : Fragment() {
    
    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    
    private var _binding: FragmentEditDeletePopularCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditDeletePopularCarPartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val description =  requireArguments().getString("description").toString()
        val img_url = requireArguments().getString("img_url").toString()

        binding.editTextPopularCarPartId.setText(id)
        binding.editTextPopularCarPartName.setText(name)
        binding.editTextPopularCarPartDescription.setText(description)
        Glide.with(requireContext()).load(img_url).into(binding.imageViewPopularCarPartImage)

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

        binding.buttonEditPopularCarPart.setOnClickListener(){

            val id = binding.editTextPopularCarPartId.text.toString().trim()
            val name = binding.editTextPopularCarPartName.text.toString().trim()
            val description = binding.editTextPopularCarPartDescription.text.toString().trim()

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextPopularCarPartName.error = "Enter the car part name"
            } else if (name.length < 3) {
                binding.editTextPopularCarPartName.error =
                    "Car part name must have at least three character"
            } else if (!name.matches(Regex("^[a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !name.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextPopularCarPartName.error = "Car part name must start with three letter"
            }else if(description.isEmpty()){
                binding.editTextPopularCarPartDescription.error = "Enter the car part description"
            } else if (description.length < 3) {
                binding.editTextPopularCarPartDescription.error =
                    "Car part description must have at least three character"
            } else if (!description.matches(Regex("^[a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z].*$")) || !description.matches(Regex("^[a-zA-Z][a-zA-Z][a-zA-Z].*$"))) {
                binding.editTextPopularCarPartDescription.error = "Car part description must start with three letter"
            }else {
                editData(id, name, description, imageUri!!)
            }
        }

        binding.buttonDeletePopularCarPart.setOnClickListener() {

            val id = binding.editTextPopularCarPartId.text.toString().trim()

                deleteData(id)
        }
    }

    private fun editData(id: String, name: String, description: String, uri: Uri) {
        try{
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$id"
        hashMap["name"] = "$name"
        hashMap["description"] = "$description"
        val ref = FirebaseDatabase.getInstance().getReference("PopularCarParts").child(id)
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
        } catch (e: Exception){

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
            val ref = FirebaseDatabase.getInstance().getReference("PopularCarParts").child(id)
            ref.removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
                }

        binding.editTextPopularCarPartId.setText("")
        binding.editTextPopularCarPartName.setText("")
        binding.editTextPopularCarPartDescription.setText("")
        Glide.with(requireContext()).clear(binding.imageViewPopularCarPartImage)
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==2 && resultCode == Activity.RESULT_OK && data != null){

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