package tarc.edu.carpartsapp.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tarc.edu.carpartsapp.databinding.FragmentAdminUpdateDeliveryStatusBinding

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
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        val inputData = args?.get("orderId")
        val orderId = binding.textViewOrderId

        orderId.text = inputData.toString()

    }

}