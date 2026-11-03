package com.example.lab4

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val friends = mutableListOf<ItemClass>()
    val friendItems: List<ItemClass>
        get() = friends
    private val friendAdapter = MyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val main = findViewById<View>(R.id.main)
        val padding = main.paddingLeft
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(padding + bars.left, padding + bars.top,
                padding + bars.right, padding + bars.bottom)
            insets
        }

        val saved = getSharedPreferences("friends", MODE_PRIVATE).getString("items", null)
        val items = JSONArray(saved ?: "[]")
        if (saved == null) friends.addAll(ListItem.map { it.copy() })
        for (index in 0 until items.length()) {
            val item = items.getJSONObject(index)
            val iconIndex = item.optInt("iconIndex", index % ListItem.size)
            val icon = ListItem.getOrElse(iconIndex) { ListItem.first() }.icon
            friends.add(ItemClass(icon, item.getString("name"), item.getString("detail")))
        }
        findViewById<RecyclerView>(R.id.rcv).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = friendAdapter
        }
        findViewById<Button>(R.id.bt_add).setOnClickListener { showAddFriendDialog() }
    }

    private fun showAddFriendDialog() {
        val name = EditText(this).apply {
            setHint(R.string.friend_name)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val detail = EditText(this).apply {
            setHint(R.string.friend_detail)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
        val fields = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val padding = (24 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding / 2, padding, 0)
            addView(name)
            addView(detail)
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.add_friend)
            .setView(fields)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.save, null)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val enteredName = name.text.toString().trim()
                if (enteredName.isEmpty()) {
                    name.error = getString(R.string.name_required)
                    return@setOnClickListener
                }
                val icon = ListItem[friends.size % ListItem.size].icon
                friends.add(ItemClass(icon, enteredName, detail.text.toString().trim()))
                friendAdapter.notifyItemInserted(friends.lastIndex)
                val items = JSONArray()
                friends.forEach { friend ->
                    items.put(JSONObject().put("name", friend.name).put("detail", friend.detail)
                        .put("iconIndex", ListItem.indexOfFirst { it.icon == friend.icon }))
                }
                getSharedPreferences("friends", MODE_PRIVATE).edit()
                    .putString("items", items.toString()).apply()
                findViewById<RecyclerView>(R.id.rcv).smoothScrollToPosition(friends.lastIndex)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}


