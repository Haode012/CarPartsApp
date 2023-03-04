package tarc.edu.carpartsapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import tarc.edu.carpartsapp.databinding.FragmentCustSelectOrderCashOnDeliveryViewDeliveryTrackerBinding
import tarc.edu.carpartsapp.databinding.FragmentProfileBinding

class FragmentCustSelectOrderCashOnDeliveryViewDeliveryTracker : Fragment() {
    private var _binding: FragmentCustSelectOrderCashOnDeliveryViewDeliveryTrackerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustSelectOrderCashOnDeliveryViewDeliveryTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}