package tarc.edu.carpartsapp.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentHomeAdminBinding

class HomeAdminFragment : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewCustomerHomepageEdit.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_admin_to_nav_selection_admin)
        }

        binding.imageViewCustomerViewAllEdit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_admin_to_nav_selection_view_all_admin)
        }

        binding.imageViewCustomerFeedback.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_admin_to_fragmentAdminViewFeedback)
        }

        binding.imageViewDelivery.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_admin_to_fragmentAdminDeliveryManagement)
        }
    }
}