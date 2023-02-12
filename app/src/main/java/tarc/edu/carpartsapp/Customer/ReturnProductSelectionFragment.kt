package tarc.edu.carpartsapp.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding
import tarc.edu.carpartsapp.databinding.FragmentReturnProductBinding

class ReturnProductSelectionFragment : Fragment() {
    private var _binding: FragmentReturnProductBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReturnProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewByCashOnDelivery.setOnClickListener{
            findNavController().navigate(R.id.action_nav_returnProduct_customer_to_nav_returnProductCashOnDelivery_customer)
        }

        binding.imageViewByCreditCard.setOnClickListener{
            findNavController().navigate(R.id.action_nav_returnProduct_customer_to_nav_returnProductCreditCard_customer)
        }

    }
}