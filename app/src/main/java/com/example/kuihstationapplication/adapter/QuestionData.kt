package com.example.kuihstationapplication.adapter

data class QuestionData(
    val title: String,
    val logo: Int,
    val desc: String,
    var isExpandable: Boolean = false
)