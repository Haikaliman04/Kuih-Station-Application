package com.example.kuihstationapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuihstationapplication.adapter.QuestionAdapter
import com.example.kuihstationapplication.adapter.QuestionData
import java.util.*

class FaqActivity : AppCompatActivity() {

    //initialize all component
    private lateinit var backButton: ImageButton

    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<QuestionData>()
    private lateinit var adapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        //declare all component
        backButton = findViewById(R.id.faqBackBtn)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        adapter = QuestionAdapter(mList)
        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            val i = Intent(this, MainActivity3::class.java)
            startActivity(i)
        }

    }

    private fun addDataToList() {
        mList.add(
            QuestionData(
                "How does the food ordering process work?",
                R.drawable.question,
                "To order food, simply browse through our menu, select the items you'd like to order, add them to your cart, proceed to checkout, and complete your order by providing delivery details."
            )
        )
        mList.add(
            QuestionData(
                "Can I schedule orders in advance?",
                R.drawable.question,
                "Yes, you can schedule orders for future delivery. During checkout, simply select the desired delivery date to schedule your order accordingly."
            )
        )
        mList.add(
            QuestionData(
                "Who can use this app?",
                R.drawable.question,
                "Our food ordering app is available for anyone with a smartphone or tablet running Android operating systems. Whether you're a busy professional, a student, a family, or anyone in need of delicious confectionary, our app is designed to cater to your needs."
            )
        )
        mList.add(
            QuestionData(
                "What payment methods do you accept?",
                R.drawable.question,
                "We only accept QR Payment for now."
            )
        )

        mList.add(
            QuestionData(
                "Is the customer data securely stored?",
                R.drawable.question,
                "Yes, customer data will be saved in Firebase."
            )
        )
    }
}