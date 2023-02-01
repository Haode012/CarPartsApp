package tarc.edu.carpartsapp.Admin.CustomerHomepageEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentSelectionBinding

class SelectionFragment : Fragment() {

    private var _binding: FragmentSelectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSelectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewSelectionBlue.setOnClickListener{
            findNavController().navigate(R.id.action_nav_selection_admin_to_nav_popular_car_parts_admin)
        }

        binding.imageViewSelectionBlack.setOnClickListener{
            findNavController().navigate(R.id.action_nav_selection_admin_to_nav_car_parts_category_admin)
        }

        binding.imageViewSelectionYellow.setOnClickListener{
            findNavController().navigate(R.id.action_nav_selection_admin_to_nav_recommended_car_parts_admin)
        }
    }
}