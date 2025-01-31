package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentAdminUpdateDeliveryStatusBinding
import tarc.edu.carpartsapp.databinding.FragmentAdminUpdateDeliveryStatusCreditCardBinding
import java.text.SimpleDateFormat
import java.util.*

class FragmentAdminUpdateDeliveryStatusCreditCard : Fragment() {
    private var _binding: FragmentAdminUpdateDeliveryStatusCreditCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =
            FragmentAdminUpdateDeliveryStatusCreditCardBinding.inflate(inflater, container, false)
        val args = this.arguments
        val inputData = args?.get("deliveryStatusId")
        var orderId = binding.outputDelivStatusId
        // val dateToday = binding.outputDateToday
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(Calendar.getInstance().time)
        val deliveryStatusMessage = binding.editTextDeliveryStatusCredit.text

        orderId.text = inputData.toString()
        //binding.outputDateToday.setText(saveCurrentDate)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnUpdateStats.setOnClickListener {
            try {
                updateStatus()
            }catch (e: NullPointerException){

            }
        }
    }

    private fun updateStatus() {
        val status = binding.editTextDeliveryStatusCredit.text.toString()
        val deliveryId = binding.outputDelivStatusId.text.toString()
        val database =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ref = database.getReference("Delivery Status(Credit Card Payment)")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    //updated here
                    for (snap2 in snap.children) {
                        val key = snap.key.toString()
                        if (snap2.child("deliveryStatusId").value.toString()
                                .equals(deliveryId)
                        ) {
                            //updated here
                            val id = snap2.key.toString()
                            //updated here
                            try {
                                ref.child(key).child(id).child("deliveryStatus").setValue(status)
//                                Toast.makeText(
//                                    context,
//                                    "Delivery Status Updated",
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
                            }catch (e: NullPointerException){}
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