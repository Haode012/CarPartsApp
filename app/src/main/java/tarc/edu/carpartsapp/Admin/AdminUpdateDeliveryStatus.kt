package tarc.edu.carpartsapp.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentAdminUpdateDeliveryStatusBinding
import java.text.SimpleDateFormat
import java.util.*

class AdminUpdateDeliveryStatus : Fragment() {
    private var _binding: FragmentAdminUpdateDeliveryStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminUpdateDeliveryStatusBinding.inflate(inflater, container, false)
        val args = this.arguments
        val inputData = args?.get("orderID")
        var orderId = binding.outputDeliveryOrderId
        val dateToday = binding.outputDateToday
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)
        val deliveryStatusMessage = binding.editTextDeliveryStatus.text

        orderId.text = inputData.toString()
        binding.outputDateToday.setText(saveCurrentDate)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnUpdateNewStatus.setOnClickListener {
            updateStatus()
        }
    }

        fun updateStatus(){
            val status = binding.editTextDeliveryStatus.text.toString()
            val deliveryId = binding.outputDeliveryOrderId.text.toString()
            val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val ref = database.getReference("Delivery Status")
            ref.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        for (snap2 in snapshot.children) {
                            val key = snap.key.toString()
                            if (snap.child("deliveryStatusId").value.toString()
                                    .equals(deliveryId)
                            ) {
                                ref.child(key).child("deliveryStatus").setValue(status)
                                Toast.makeText(context, "Delivery Status Updated", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
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