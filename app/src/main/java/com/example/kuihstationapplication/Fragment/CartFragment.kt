package com.example.kuihstationapplication.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuihstationapplication.OrderPlacedBottomSheet
import com.example.kuihstationapplication.PayActivity
import com.example.kuihstationapplication.R
import com.example.kuihstationapplication.adapter.CartAdapter
import com.example.kuihstationapplication.databinding.FragmentCartBinding
import com.example.kuihstationapplication.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var customerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()


        binding.proceedButton.setOnClickListener {
            //get order item details
            getOrderItemDetail()

        }


        return binding.root
    }

    private fun getOrderItemDetail() {
        val orderIdReference:DatabaseReference = database.reference.child("Customer").child(customerId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        //get item quantities
        val foodQuantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(foodSnapshot in snapshot.children){
                    //get the cartItems to respective list
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    // add items details into list
                    orderItems?.foodName?.let {foodName.add(it)}
                    orderItems?.foodPrice?.let {foodPrice.add(it)}
                    orderItems?.foodDescription?.let {foodDescription.add(it)}
                    orderItems?.foodImage?.let {foodImage.add(it)}
                    orderItems?.foodIngredient?.let {foodIngredient.add(it)}

                }
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredient,foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Order Failed. Please Try Again!",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {

        if(isAdded && context != null){
            val intent = Intent(requireContext(),PayActivity::class.java)
            intent.putExtra("FoodItemName", foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantites", foodQuantities as ArrayList<Int>)
            startActivity(intent)


        }
    }

    private fun retrieveCartItems() {

        // database reference to the firebase
        database = FirebaseDatabase.getInstance()
        customerId = auth.currentUser?.uid ?: ""
        val foodReference : DatabaseReference = database.reference.child("Customer").child(customerId).child("CartItems")

        //list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()

        // fetch data from the database
        foodReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    // get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    //add cart items details to the list
                    cartItems?.foodName?.let {foodNames.add(it)}
                    cartItems?.foodPrice?.let {foodPrices.add(it)}
                    cartItems?.foodDescription?.let {foodDescriptions.add(it)}
                    cartItems?.foodImage?.let {foodImagesUri.add(it)}
                    cartItems?.foodQuantity?.let {quantity.add(it)}
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                 cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodImagesUri,
                    quantity,
                    foodIngredients)
                binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Data not Fetch", Toast.LENGTH_SHORT).show()
            }

        })

    }


    companion object {

    }
}