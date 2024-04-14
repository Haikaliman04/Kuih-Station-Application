package com.example.kuihstationapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kuihstationapplication.databinding.ActivityDetailsBinding
import com.example.kuihstationapplication.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String?= null
    private var foodImage: String?= null
    private var foodDescription: String?= null
    private var foodIngredients: String?= null
    private var foodPrice: String?= null
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text = foodName
            detailDescription.text = foodDescription
            detailIngredients.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
        }

        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }


    }
    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""

        // Create a cart item object
        val cartItem = CartItems(foodName.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodImage.toString(),
            1)

        // Save data to cart
        database.child("Customer").child(customerId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show()
            }
    }

}