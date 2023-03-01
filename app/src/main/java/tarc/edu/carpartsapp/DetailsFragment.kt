package tarc.edu.carpartsapp

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
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
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

            Toast.makeText(requireContext(), "Your are required to login first", Toast.LENGTH_SHORT).show()

            var totalQuantity = binding.quantity.text.toString().trim().toInt()

            var totalPrice = price.toDouble() * totalQuantity
            var formattedPrice = String.format("%.2f", totalPrice)

            val currentDate = SimpleDateFormat("MM/dd/yyyy")
            val saveCurrentDate = currentDate.format(Calendar.getInstance().time)

            val currentTime = SimpleDateFormat("HH:mm:ss")
            val saveCurrentTime = currentTime.format(Calendar.getInstance().time)

            findNavController().navigate(R.id.action_nav_details_main_to_loginFragment
                , Bundle().apply {
                    putString("id", id)
                    putString("name", name)
                    putString("price", price)
                    putString("warranty", warranty)
                    putString("total_price", formattedPrice)
                    putString("total_quantity", totalQuantity.toString())
                    putString("img_url", img_url)
                    putString("currentDate", saveCurrentDate)
                    putString("currentTime", saveCurrentTime)
                })
        }
    }
}