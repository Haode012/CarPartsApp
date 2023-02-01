package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

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
import tarc.edu.carpartsapp.databinding.FragmentEditDeleteRecommendedCarPartsBinding

class EditDeleteRecommendedCarPartsFragment : Fragment() {

    private var reference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var _binding: FragmentEditDeleteRecommendedCarPartsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditDeleteRecommendedCarPartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val description =  requireArguments().getString("description").toString()
        val img_url = requireArguments().getString("img_url").toString()

        binding.editTextRecommendedCarPartId.setText(id)
        binding.editTextRecommendedCarPartName.setText(name)
        binding.editTextRecommendedCarPartDescription.setText(description)
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

            //validation
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            } else if (name.isEmpty()) {
                binding.editTextRecommendedCarPartName.error = "Enter the car part name"
            }else if(description.isEmpty()){
                binding.editTextRecommendedCarPartDescription.error = "Enter the car part description"
            }else {
                editData(id, name, description, imageUri!!)
            }
        }

        binding.buttonDeleteRecommendedCarPart.setOnClickListener() {

            val id = binding.editTextRecommendedCarPartId.text.toString().trim()

            deleteData(id)
        }
    }

    private fun editData(id: String, name: String, description: String, uri: Uri) {
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$id"
        hashMap["name"] = "$name"
        hashMap["description"] = "$description"
        val ref = FirebaseDatabase.getInstance().getReference("Recommended").child(id)
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
        val ref = FirebaseDatabase.getInstance().getReference("Recommended").child(id)
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