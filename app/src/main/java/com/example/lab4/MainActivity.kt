package com.example.lab4

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var rcv: RecyclerView
    lateinit var adapter: MyAdapter

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

        rcv = findViewById(R.id.rcv)
        adapter = MyAdapter(this)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
        findViewById<Button>(R.id.bt_add).setOnClickListener { showAddDialog() }

    }

    private fun showAddDialog() {
        val nameInput = EditText(this).apply {
            setHint(R.string.friend_name)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val detailInput = EditText(this).apply {
            setHint(R.string.friend_detail)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val spacing = (24 * resources.displayMetrics.density).toInt()
            setPadding(spacing, spacing / 2, spacing, 0)
            addView(nameInput)
            addView(detailInput)
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.add_friend)
            .setView(form)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.save, null)
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = nameInput.text.toString().trim()
                if (name.isEmpty()) {
                    nameInput.error = getString(R.string.name_required)
                    return@setOnClickListener
                }
                ListItem.add(ItemClass(R.drawable.anh1, name, detailInput.text.toString().trim()))
                adapter.notifyItemInserted(ListItem.lastIndex)
                rcv.smoothScrollToPosition(ListItem.lastIndex)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}

