package tarc.edu.carpartsapp.Customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCarPartDetailsBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CarPartDetailsFragment : Fragment() {

    private var _binding: FragmentCarPartDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCarPartDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val description = requireArguments().getString("description").toString()
        val price = requireArguments().getString("price").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val img_url = requireArguments().getString("img_url").toString()

        binding.detailsId.setText(id)
        binding.detailsName.setText(name)
        binding.detailsDescription.setText(description)
        binding.detailsPrice.setText(price)
        binding.detailsWarranty.setText(warranty)
        Glide.with(requireContext()).load(img_url).into(binding.detailsImage)

        binding.addItem.setOnClickListener {
            var totalQuantity = binding.quantity.text.toString().trim().toInt()

            if (totalQuantity < 10) {
                totalQuantity++
                binding.quantity.text = totalQuantity.toString()
            }
        }

        binding.removeItem.setOnClickListener {
            var totalQuantity = binding.quantity.text.toString().trim().toInt()

            if (totalQuantity > 1) {
                totalQuantity--
                binding.quantity.text = totalQuantity.toString()
            }
        }

        binding.buttonAddToCart.setOnClickListener{
            var totalQuantity = binding.quantity.text.toString().trim().toInt()

            var totalPrice = price.toDouble() * totalQuantity
            var formattedPrice = String.format("%.2f", totalPrice)

            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val hashMap = HashMap<String, Any>()
            hashMap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap["id"] = "$id"
            hashMap["name"] = "$name"
            hashMap["price"] = "$price"
            hashMap["warranty"] = "$warranty"
            hashMap["total_price"] = "$formattedPrice"
            hashMap["total_quantity"] = "$totalQuantity"
            hashMap["img_url"] = "$img_url"
            hashMap["currentDate"] = "$saveCurrentDate"

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("CartItem")
                reference.child(id).setValue(hashMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}