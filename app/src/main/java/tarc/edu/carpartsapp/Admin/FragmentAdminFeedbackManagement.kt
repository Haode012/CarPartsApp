package tarc.edu.carpartsapp.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentAdminDeliveryManagementBinding
import tarc.edu.carpartsapp.databinding.FragmentAdminFeedbackManagementBinding

class FragmentAdminFeedbackManagement : Fragment() {
    private var _binding: FragmentAdminFeedbackManagementBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminFeedbackManagementBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView15.setOnClickListener {
        findNavController().navigate(R.id.action_fragmentAdminFeedbackManagement_to_fragmentAdminViewFeedback)
        }

        binding.imageView16.setOnClickListener {
        findNavController().navigate(R.id.action_fragmentAdminFeedbackManagement_to_adminViewAllDeliveryFeedback)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}