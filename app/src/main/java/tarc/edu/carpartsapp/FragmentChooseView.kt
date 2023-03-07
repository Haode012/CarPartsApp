package tarc.edu.carpartsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.databinding.FragmentChooseViewBinding

import java.util.*

class FragmentChooseView : Fragment() {
    private var _binding: FragmentChooseViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentChooseViewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCash.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentChooseView_to_fragmentAdminViewAllDeliveryStatus)
        }

        binding.btnCredit.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentChooseView_to_fragmentAdminViewAllDeliveryStatusCreditCard)
        }
        }




    }