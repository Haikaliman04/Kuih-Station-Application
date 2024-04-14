package com.example.kuihstationapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails():Serializable {
    var customerId:String?=null
    var customerName:String?=null
    var foodNames:MutableList<String>?=null
    var foodImages:MutableList<String>?=null
    var foodPrices:MutableList<String>?=null
    var foodQuantities:MutableList<Int>?=null
    var address:String?=null
    var totalPrice:String?=null
    var phoneNumber:String?=null
    var customerDate:String?=null
    var orderAccepted:Boolean=false
    var paymentReceived:Boolean=false
    var itemPushKey:String?=null
    var currentTime:Long=0

    constructor(parcel: Parcel) : this() {
        customerId = parcel.readString()
        customerName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        customerDate = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        customerId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantites: ArrayList<Int>,
        address: String,
        totalAmount : String,
        phone: String,
        date: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this(){
        this.customerId = customerId
        this.customerName = name
        this.foodNames = foodItemName
        this.foodPrices = foodItemPrice
        this.foodImages = foodItemImage
        this.foodQuantities = foodItemQuantites
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.customerDate = date
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
    }


    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(customerId)
        parcel.writeString(customerName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeString(customerDate)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}