package com.example.lab4

data class ItemClass(
    var icon: Int,
    var name: String,
    var detail: String
)

val ListItem = mutableListOf(
    ItemClass(R.drawable.anh, "Name1", "Mô tả...?"),
    ItemClass(R.drawable.anh1, "Name2", "Mô tả...?"),
    ItemClass(R.drawable.anh2, "Name3", "Mô tả...?"),
    ItemClass(R.drawable.anh3, "Name4", "Mô tả...?"),
    ItemClass(R.drawable.anh4, "Name5", "Mô tả...?")
)
