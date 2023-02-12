package tarc.edu.carpartsapp.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import tarc.edu.carpartsapp.databinding.FragmentRequestReturnProductBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RequestReturnProductFragment : Fragment() {

    private var _binding: FragmentRequestReturnProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestReturnProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val order_id = requireArguments().getString("order_id").toString()
        val order_date = requireArguments().getString("order_date").toString()
        val order_time = requireArguments().getString("order_time").toString()
        val img_url = requireArguments().getString("img_url").toString()


        binding.requestId.setText(id)
        binding.requestName.setText(name)
        binding.requestWarranty.setText(warranty)
        binding.requestOrderId.setText(order_id)
        binding.requestOrderDate.setText(order_date)
        binding.requestOrderTime.setText(order_time)
        Glide.with(requireContext()).load(img_url).into(binding.requestImage)

        binding.buttonRequestReturnProduct.setOnClickListener{

            val hashMap = HashMap<String, Any>()
            hashMap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap["id"] = "$id"
            hashMap["name"] = "$name"
            hashMap["warranty"] = "$warranty"
            hashMap["orderID"] = "$order_id"
            hashMap["orderDate"] = "$order_date"
            hashMap["orderTime"] = "$order_time"
            hashMap["img_url"] = "$img_url"

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("RequestReturnProduct")
                reference.child(FirebaseAuth.getInstance().currentUser!!.uid).child(order_id).setValue(hashMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Requested Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
}