package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.databinding.FragmentAdminDeliveryManagementBinding

class FragmentAdminDeliveryManagement : Fragment() {
    private var _binding: FragmentAdminDeliveryManagementBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminDeliveryManagementBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewAllDeliveryStatus.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAdminDeliveryManagement_to_fragmentChooseView)
        }

        binding.imageViewCreateDeliveryCash.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAdminDeliveryManagement_to_createDeliveryStatus)
        }

        binding.imageviewCreateDeliveryCredit.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminDeliveryManagement_to_fragmentCreateDeliveryStatusCreditCardPayment)
        }

        binding.imageViewDeliveryTracker.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAdminDeliveryManagement_to_fragmentAdminChooseOrderForDeliveryTracker)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}