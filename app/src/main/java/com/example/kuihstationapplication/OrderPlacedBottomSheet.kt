package com.example.kuihstationapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kuihstationapplication.databinding.FragmentOrderPlacedBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderPlacedBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOrderPlacedBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPlacedBottomSheetBinding.inflate(layoutInflater,container,false)

        // Handle click on WhatsApp button
        binding.whatsapp.setOnClickListener {
            // Phone number with country code
            val phoneNumber = "+601123385412"
            // Customer's name
            val customerName = arguments?.getString("customerName") ?: ""
            // Form the message
            val message = "Name: $customerName"
            // Encode the message
            val encodedMessage = Uri.encode(message)
            // Form the URI with the phone number and the message
            val uri = Uri.parse("https://wa.me/$phoneNumber/?text=$encodedMessage")
            // Create an Intent with the ACTION_VIEW action and the URI
            val intent = Intent(Intent.ACTION_VIEW, uri)
            // Start the activity with the Intent
            startActivity(intent)
        }


        binding.gohome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity3::class.java).apply {
                // Retrieve customer name from arguments
                val customerName = arguments?.getString("customerName")
                // Pass customer name as an extra
                putExtra("customerName", customerName)
            }
            startActivity(intent)
            dismiss() // Dismiss the bottom sheet after navigation
        }
        return binding.root
    }

    companion object {
        fun newInstance(customerName: String): OrderPlacedBottomSheet {
            val fragment = OrderPlacedBottomSheet()
            val args = Bundle().apply {
                putString("customerName", customerName)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
