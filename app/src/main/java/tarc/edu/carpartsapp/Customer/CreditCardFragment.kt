package tarc.edu.carpartsapp.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentCreditCardBinding

class CreditCardFragment : Fragment() {

    private var _binding: FragmentCreditCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreditCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirmToPay.setOnClickListener{
            findNavController().navigate(R.id.action_nav_creditCard_customer_to_nav_placedOrder_customer)
        }

    }
}