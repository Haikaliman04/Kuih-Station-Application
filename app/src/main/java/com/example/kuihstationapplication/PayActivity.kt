package com.example.kuihstationapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuihstationapplication.databinding.ActivityPayBinding
import com.example.kuihstationapplication.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayActivity : AppCompatActivity() {
    lateinit var binding : ActivityPayBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var date:String
    private lateinit var totalAmount:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var foodItemIngredient:ArrayList<String>
    private lateinit var foodItemQuantites:ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var customerId : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        //setuserdata
        setUserData()

        //get user details from firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        foodItemQuantites = intent.getIntegerArrayListExtra("FoodItemQuantites") as ArrayList<Int>

        totalAmount = "RM" + calculateTotalAmount().toString()
        binding.totalAmount.isEnabled=false
        binding.totalAmount.setText(totalAmount)

        binding.PlaceOrderButton.setOnClickListener {
            // get data from textview
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            date = binding.date.text.toString().trim()
            if (name.isBlank()&&address.isBlank()&&phone.isBlank()&&date.isBlank()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }



        }

        binding.imageBtn.setOnClickListener {
            finish()
        }

    }

    private fun placeOrder() {
        customerId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            customerId,
            name,
            foodItemName,
            foodItemPrice,
            foodItemImage,
            foodItemQuantites,
            address,
            totalAmount,
            phone,
            date,
            time,
            itemPushKey,
            false,
            false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = OrderPlacedBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)

        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("Customer").child(customerId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("Customer").child(customerId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for( i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntVale = if (lastChar == '$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemQuantites[i]
            totalAmount += priceIntVale * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val customer = auth.currentUser
        if(customer!= null){
            val customerId = customer.uid
            val customerReference = databaseReference.child("Customer").child(customerId)

            customerReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("customerName").getValue(String::class.java)?:""
                        val addresses = snapshot.child("customerAddress").getValue(String::class.java)?:""
                        val phones = snapshot.child("customerPhone").getValue(String::class.java)?:""
                        val dates = snapshot.child("customerDate").getValue(String::class.java)?:""

                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                            date.setText(dates)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}