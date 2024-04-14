package com.example.kuihstationapplication.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kuihstationapplication.Model
import com.example.kuihstationapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater,container,false)

        setUserData()
        binding.apply{
            name.isEnabled = false
            email.isEnabled= false
            address.isEnabled=false
            phone.isEnabled=false


        binding.editButton.setOnClickListener {
                name.isEnabled = !name.isEnabled
                email.isEnabled = !email.isEnabled
                address.isEnabled = !address.isEnabled
                phone.isEnabled = !phone.isEnabled

            }
        }

        binding.saveInformationButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()

            updateUserData(name,email,address,phone)

        }

        return(binding.root)

    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val customerId = auth.currentUser?.uid
        if (customerId!=null){
            val customerReference = database.getReference("Customer").child(customerId)

            val customerData = hashMapOf(
                "customerName" to name,
                "customerAddress" to address,
                "customerEmail" to email,
                "customerPhone" to phone

            )
            customerReference.setValue(customerData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Update Successfully",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Profile Update Failed",Toast.LENGTH_SHORT).show()

                }
        }
    }

    private fun setUserData() {
        val customerId = auth.currentUser?.uid
        if (customerId!=null){
            val customerReference = database.getReference("Customer").child(customerId)

            customerReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val customerProfile = snapshot.getValue(Model::class.java)
                        if (customerProfile!= null){
                            binding.name.setText(customerProfile.customerName)
                            binding.address.setText(customerProfile.customerAddress)
                            binding.email.setText(customerProfile.customerEmail)
                            binding.phone.setText(customerProfile.customerPhone)

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    companion object {

    }
}