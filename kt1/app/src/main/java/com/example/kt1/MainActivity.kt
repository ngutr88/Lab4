package com.example.kt1

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import android.app.AlertDialog
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject

data class Question(val text: String, val options: List<String>, val correct: Int)

class MainActivity : ComponentActivity() {
    private val questions = mutableListOf<Question>()
    private val answers = mutableMapOf<Int, Int>()
    private var editing = false
    private var editIndex = -1
    private val optionIds = listOf(R.id.option_a, R.id.option_b, R.id.option_c, R.id.option_d)
    private val correctIds = listOf(R.id.correct_a, R.id.correct_b, R.id.correct_c, R.id.correct_d)
    private val preferences by lazy { getSharedPreferences("quiz", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (editing) showQuiz() else finish()
            }
        })
        loadQuestions()
        savedInstanceState?.getIntArray("answers")?.forEachIndexed { i, value ->
            if (value >= 0) answers[i] = value
        }
        if (savedInstanceState?.getBoolean("editing") == true) {
            showEditor(savedInstanceState.getInt("editIndex", -1))
        } else showQuiz()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("editing", editing)
        outState.putInt("editIndex", editIndex)
        outState.putIntArray("answers", IntArray(questions.size) { answers[it] ?: -1 })
        super.onSaveInstanceState(outState)
    }

    private fun loadQuestions() {
        val stored = preferences.getString("questions", null)
        if (stored != null) {
            try {
                val array = JSONArray(stored)
                repeat(array.length()) {
                    val q = array.getJSONObject(it)
                    val options = q.getJSONArray("options")
                    require(options.length() == 4 && q.getInt("correct") in 0..3)
                    questions.add(Question(q.getString("text"), List(4) { n -> options.getString(n) }, q.getInt("correct")))
                }
                if (questions.isNotEmpty()) return
            } catch (_: Exception) { questions.clear() }
        }
        questions.addAll(listOf(
            Question("Trong Kotlin, từ khóa nào được sử dụng để khai báo một biến không thể thay đổi?", listOf("val", "vla", "lav", "alv"), 0),
            Question("AndroidManifest.xml là tệp gì trong dự án Android?", listOf("Tệp giao diện", "Cấu hình các thành phần ứng dụng", "Mã nguồn của ứng dụng", "Dữ liệu ảnh"), 1),
            Question("Thành phần nào cho phép chọn một đáp án trong nhóm lựa chọn?", listOf("TextView", "EditText", "RadioGroup và RadioButton", "ImageView"), 2)
        ))
    }

    private fun persistQuestions() {
        val array = JSONArray()
        questions.forEach { q -> array.put(JSONObject().put("text", q.text).put("options", JSONArray(q.options)).put("correct", q.correct)) }
        preferences.edit { putString("questions", array.toString()) }
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    private fun applyInsets() {
        val root = findViewById<View>(R.id.root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val safe = insets.getInsets(WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())
            view.setPadding(dp(16) + safe.left, dp(16) + safe.top,
                dp(16) + safe.right, dp(16) + safe.bottom)
            insets
        }
        root.requestApplyInsets()
    }

    private fun showQuiz() {
        editing = false
        setContentView(R.layout.activity_main)
        applyInsets()
        val container = findViewById<LinearLayout>(R.id.questions)
        // XML supplies a readable sample for Layout Preview; runtime uses saved questions.
        container.removeAllViews()
        questions.forEachIndexed { index, question ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(16), dp(12), dp(16), dp(12))
                setBackgroundResource(R.drawable.card_background)
            }
            container.addView(card, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12) })
            card.addView(TextView(this).apply {
                text = question.text
                textSize = 16f
                setTextColor("#49454F".toColorInt())
            })
            val group = RadioGroup(this)
            question.options.forEachIndexed { optionIndex, option ->
                group.addView(RadioButton(this).apply {
                    id = View.generateViewId()
                    text = option
                    textSize = 14f
                    minHeight = dp(48)
                    setPadding(0, dp(4), 0, dp(4))
                    isChecked = answers[index] == optionIndex
                }, RadioGroup.LayoutParams(-1, -2))
            }
            group.setOnCheckedChangeListener { radioGroup, checkedId ->
                answers[index] = radioGroup.indexOfChild(radioGroup.findViewById(checkedId))
            }
            card.addView(group)
            card.addView(Button(this).apply {
                text = getString(R.string.edit_question)
                isAllCaps = false
                contentDescription = getString(R.string.edit_description, index + 1)
                setOnClickListener { showEditor(index) }
            }, LinearLayout.LayoutParams(-2, dp(48)).apply { gravity = android.view.Gravity.END })
        }
        findViewById<Button>(R.id.add).setOnClickListener { showEditor(-1) }
        findViewById<Button>(R.id.submit).setOnClickListener {
            val unanswered = questions.indices.filter { answers[it] !in 0..3 }
            if (unanswered.isNotEmpty()) {
                AlertDialog.Builder(this).setTitle(R.string.notice)
                    .setMessage(getString(R.string.missing_answers, unanswered.joinToString(", ") { (it + 1).toString() }))
                    .setPositiveButton(R.string.close, null).show()
            } else {
                val score = questions.indices.count { answers[it] == questions[it].correct }
                val details = questions.mapIndexed { i, q ->
                    getString(R.string.result_detail, i + 1, getString(if (answers[i] == q.correct) R.string.right else R.string.wrong), q.options[q.correct])
                }.joinToString("\n\n")
                AlertDialog.Builder(this).setTitle(getString(R.string.result_title, score, questions.size))
                    .setMessage(details).setPositiveButton(R.string.close, null)
                    .setNeutralButton(R.string.retry) { _, _ -> answers.clear(); showQuiz() }.show()
            }
        }
    }

    private fun showEditor(index: Int) {
        editing = true
        editIndex = index
        setContentView(R.layout.editor)
        applyInsets()
        val questionField = findViewById<EditText>(R.id.question)
        val fields = optionIds.map { findViewById<EditText>(it) }
        val correctGroup = findViewById<RadioGroup>(R.id.correct)
        if (index >= 0) {
            findViewById<TextView>(R.id.editor_title).text = getString(R.string.edit_question)
            findViewById<Button>(R.id.save).text = getString(R.string.save_changes)
            val question = questions[index]
            questionField.setText(question.text)
            fields.forEachIndexed { i, field -> field.setText(question.options[i]) }
            correctGroup.check(correctIds[question.correct])
        }
        findViewById<Button>(R.id.cancel).setOnClickListener {
            (listOf(questionField) + fields).forEach { field ->
                field.text.clear()
                field.error = null
            }
            correctGroup.clearCheck()
            questionField.requestFocus()
            Toast.makeText(this, R.string.cleared, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.save).setOnClickListener {
            val allFields = listOf(questionField) + fields
            val emptyFields = allFields.filter { it.text.toString().isBlank() }
            allFields.forEach { it.error = null }
            if (emptyFields.isNotEmpty()) {
                emptyFields.forEach { it.error = getString(R.string.required) }
                emptyFields.first().requestFocus()
                Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val correct = correctIds.indexOf(correctGroup.checkedRadioButtonId)
            if (correct == -1) {
                Toast.makeText(this, R.string.choose_correct, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val question = Question(questionField.text.toString().trim(), fields.map { it.text.toString().trim() }, correct)
            if (index < 0) questions.add(question) else { questions[index] = question; answers.remove(index) }
            persistQuestions()
            showQuiz()
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
        }
    }

}
