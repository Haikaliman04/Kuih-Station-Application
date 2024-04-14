package com.example.kuihstationapplication.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kuihstationapplication.adapter.BuyAgainAdapter
import com.example.kuihstationapplication.databinding.FragmentStatusBinding
import com.example.kuihstationapplication.model.OrderDetails
import com.example.kuihstationapplication.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit  var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var customerId : String
    private var listOfOrderItem:MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(layoutInflater,container,false)
        // inflate layout for this fragment
        // initialize firebase auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //retrieve and display the user order history
        retrieveBuyHistory()

        //recent buy button click
        binding.recentbuyItem.setOnClickListener{
            seeItemsRecentBuy()
        }
        binding.receivedButton.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    //function to see items recent buy
    private fun seeItemsRecentBuy() {
        val intent = Intent(requireContext(), recentOrderItems::class.java)
        intent.putExtra("RecentBuyOrderItem", ArrayList(listOfOrderItem))
        startActivity(intent)
    }


    //function to retrieve items buy history
    private fun retrieveBuyHistory() {
        binding.recentbuyItem.visibility = View.VISIBLE
        customerId = auth.currentUser?.uid?:""

        val buyItemReference : DatabaseReference = database.reference.child("Customer").child(customerId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }

                //display the most recent order
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()){
                    setDataInRecentBuyItem()

                    //setup to recyclerView with previous order details
                    setPreviousBuyItemsRecyclerView()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //function to display the recent order details
    private fun setDataInRecentBuyItem() {
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                buyAgainFoodName.text = it.foodNames?.firstOrNull()?:""
                buyAgainFoodPrice.text = it.foodPrices?.firstOrNull()?:""
                val image = it.foodImages?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)

                val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
                Log.d("","setDataInRecentBuyItem: $isOrderIsAccepted")
                if (isOrderIsAccepted){
                    orderStatus.background.setTint(Color.GREEN)
                    receivedButton.visibility = View.VISIBLE
                }

            }
        }
    }

    //function to setup recyclerView with the recent order details
    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()
        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOrderItem[i].foodPrices?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOrderItem[i].foodImages?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)

                    }
                }
                val rv = binding.BuyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdapter(
                    buyAgainFoodName,
                    buyAgainFoodPrice,
                    buyAgainFoodImage,
                    requireContext())
                rv.adapter = buyAgainAdapter
            }
        }
    }

}