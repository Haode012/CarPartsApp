package tarc.edu.carpartsapp.Customer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tarc.edu.carpartsapp.R
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

        val deliveryID = requireArguments().getString("deliveryID").toString()
        val address = requireArguments().getString("address").toString()
        val id = requireArguments().getString("id").toString()
        val name = requireArguments().getString("name").toString()
        val warranty = requireArguments().getString("warranty").toString()
        val total_quantity = requireArguments().getString("total_quantity").toString()
        val order_id = requireArguments().getString("order_id").toString()
        val delivered_date = requireArguments().getString("delivered_date").toString()
        val delivered_time = requireArguments().getString("delivered_time").toString()
        val img_url = requireArguments().getString("img_url").toString()
        val status = "Pending"

        binding.requestDeliveryId.setText(deliveryID)
        binding.requestDeliveryAddress.setText(address)
        binding.requestId.setText(id)
        binding.requestName.setText(name)
        binding.requestWarranty.setText(warranty)
        binding.requestTotalQuantityShow.setText(total_quantity)
        binding.requestTotalQuantity.setText(total_quantity)
        binding.requestOrderId.setText(order_id)
        binding.requestDeliveryDate.setText(delivered_date)
        binding.requestDeliveryTime.setText(delivered_time)
        Glide.with(requireContext()).load(img_url).into(binding.requestImage)

        binding.removeQuantity.setOnClickListener {
            var totalQuantity = binding.requestTotalQuantity.text.toString().trim().toInt()

            if (totalQuantity > 1) {
                totalQuantity--
                binding.requestTotalQuantity.text = totalQuantity.toString()
            }

        }


        binding.buttonRequestReturnProduct.setOnClickListener{
            var totalQuantity = binding.requestTotalQuantity.text.toString().trim().toInt()

            val hashMap = HashMap<String, Any>()
            hashMap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap["id"] = "$id"
            hashMap["name"] = "$name"
            hashMap["warranty"] = "$warranty"
            hashMap["total_quantity"] = "$totalQuantity"
            hashMap["orderID"] = "$order_id"
            hashMap["deliveredDate"] = "$delivered_date"
            hashMap["deliveredTime"] = "$delivered_time"
            hashMap["img_url"] = "$img_url"
            hashMap["deliveryID"] = "$deliveryID"
            hashMap["deliveryAddress"] = "$address"

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("RequestReturnProduct")
                reference.child(FirebaseAuth.getInstance().currentUser!!.uid).child(order_id).child(id).setValue(hashMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Requested Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to Request", Toast.LENGTH_SHORT).show()
                    }
                }

            val hashMap2 = HashMap<String, Any>()
            hashMap2["uid"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap2["id"] = "$id"
            hashMap2["name"] = "$name"
            hashMap2["warranty"] = "$warranty"
            hashMap2["total_quantity"] = "$totalQuantity"
            hashMap2["orderID"] = "$order_id"
            hashMap2["deliveredDate"] = "$delivered_date"
            hashMap2["deliveredTime"] = "$delivered_time"
            hashMap2["img_url"] = "$img_url"
            hashMap2["deliveryID"] = "$deliveryID"
            hashMap2["deliveryAddress"] = "$address"
            hashMap2["status"] = "$status"

            val database2 = FirebaseDatabase.getInstance()
            val reference2 = database2.getReference("ReturnProductStatus")
            reference2.child(FirebaseAuth.getInstance().currentUser!!.uid).child(order_id).child(id).setValue(hashMap2).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }

            val method = requireArguments().getString("method").toString()

            if(method == "cash"){
                editData()
            } else {
                editData2()
            }

            findNavController().navigate(R.id.action_nav_requestReturnProduct_customer_to_nav_itemDelivered_customer)
        }
        }

    private fun editData2() {
        var totalQuantityShow = binding.requestTotalQuantityShow.text.toString().trim().toInt()
        var totalQuantity = binding.requestTotalQuantity.text.toString().trim().toInt()
        val deliveryId = binding.requestDeliveryId.text.toString().trim()
        val id = binding.requestId.text.toString().trim()
        var totalQuantityEdited = totalQuantityShow - totalQuantity
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val hashMap4 = HashMap<String, Any>()
        hashMap4["quantity"] = "$totalQuantityEdited"
        val ref2 = FirebaseDatabase.getInstance().getReference("Delivered Item(Credit Card Payment)")
        ref2.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    if(snap.key.toString().equals(userId.toString())) {
//                       Toast.makeText(context, snap.key.toString(), Toast.LENGTH_SHORT).show()
                        for(snap2 in snap.children) {
                            if (snap2.key.toString().equals(deliveryId.toString())) {
                                for(snap3 in snap2.children) {
                                    if (snap3.key.toString().equals((id.toString()))) {
                                        if (totalQuantityEdited != 0) {
                                            // Update the data using the updateChildren() method
                                            ref2.child(snap.key!!).child(snap2.key!!)
                                                .child(snap3.key!!).updateChildren(hashMap4)
                                        } else {
                                            ref2.child(snap.key!!).child(snap2.key!!)
                                                .child(snap3.key!!).removeValue()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun editData() {
        var totalQuantityShow = binding.requestTotalQuantityShow.text.toString().trim().toInt()
        var totalQuantity = binding.requestTotalQuantity.text.toString().trim().toInt()
        val deliveryId = binding.requestDeliveryId.text.toString().trim()
        val id = binding.requestId.text.toString().trim()
        var totalQuantityEdited = totalQuantityShow - totalQuantity
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

            val hashMap3 = HashMap<String, Any>()
            hashMap3["quantity"] = "$totalQuantityEdited"
            val ref = FirebaseDatabase.getInstance().getReference("Delivered Items")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(snap in snapshot.children){
                   if(snap.key.toString().equals(userId.toString())) {
//                       Toast.makeText(context, snap.key.toString(), Toast.LENGTH_SHORT).show()
                       for(snap2 in snap.children) {
                           if (snap2.key.toString().equals(deliveryId.toString())) {
                               for(snap3 in snap2.children) {
                                   if (snap3.key.toString().equals((id.toString()))) {
                                       if (totalQuantityEdited != 0) {
                                           // Update the data using the updateChildren() method
                                           ref.child(snap.key!!).child(snap2.key!!)
                                               .child(snap3.key!!).updateChildren(hashMap3)
                                       } else {
                                           ref.child(snap.key!!).child(snap2.key!!)
                                               .child(snap3.key!!).removeValue()
                                       }
                                   }
                               }
                           }
                       }
                   }
               }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }




  }