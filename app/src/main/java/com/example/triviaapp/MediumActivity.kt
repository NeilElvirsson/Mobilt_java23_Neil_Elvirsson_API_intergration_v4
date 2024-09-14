package com.example.triviaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.ArrayList

class MediumActivity: AppCompatActivity() {

    private val questions = mutableListOf<String>()
    private val correctAnswers = mutableListOf<String>()
    private var currentQuestionIndex = 0
    private val userAnswers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medium)

        Toast.makeText(this, "Du valde medium", Toast.LENGTH_SHORT).show()

        val questionText = findViewById<TextView>(R.id.questionText)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val trueRadioButton = findViewById<RadioButton>(R.id.trueRadioButton)
        val falseRadioButton = findViewById<RadioButton>(R.id.falseRadioButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.visibility = Button.GONE

        CoroutineScope(Dispatchers.Main).launch {
            fetchTriviaQuestions()

            if (questions.isNotEmpty()) {

                questionText.text = questions[currentQuestionIndex]
            }

        }

        radioGroup.setOnCheckedChangeListener{ _, checkedId ->

            val selectedAnswer = when (checkedId) {
                R.id.trueRadioButton -> "True"
                R.id.falseRadioButton -> "False"
                else -> ""
            }
            if(selectedAnswer.isNotEmpty()) {
                userAnswers.add(selectedAnswer)

                if(currentQuestionIndex < questions.size -1) {
                    currentQuestionIndex++
                    questionText.text = questions[currentQuestionIndex]
                    radioGroup.clearCheck()

                } else {
                    submitButton.visibility = Button.VISIBLE

                }
            }
        }

        submitButton.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putStringArrayListExtra("userAnswers", ArrayList(userAnswers))
            intent.putStringArrayListExtra("correctAnswers", ArrayList(correctAnswers))
            startActivity(intent)
        }


        //-----------------------------------------------------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private suspend fun fetchTriviaQuestions() {

        val url = "https://opentdb.com/api.php?amount=5&category=11&difficulty=medium&type=boolean"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        withContext(Dispatchers.IO) {

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {

                    val jsonResponse = response.body?.string()
                    val jsonObject = JSONObject(jsonResponse)
                    val resultsArray = jsonObject.getJSONArray("results")

                    questions.clear()
                    correctAnswers.clear()

                    for (i in 0 until resultsArray.length()) {

                        val questionObj = resultsArray.getJSONObject(i)
                        val question = questionObj.getString("question")
                        val correctAnswer = questionObj.getString("correct_answer")

                        val decodedQuestion = HtmlCompat.fromHtml(question,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString()

                        questions.add(decodedQuestion)
                        correctAnswers.add(correctAnswer)
                    }

                    withContext(Dispatchers.Main) {
                        if(questions.isNotEmpty()) {
                            findViewById<TextView>(R.id.questionText).text = questions[currentQuestionIndex]
                        }
                    }

                }


            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MediumActivity, "Failed to fetch question", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}