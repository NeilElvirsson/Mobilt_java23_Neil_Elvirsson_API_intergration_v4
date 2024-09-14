package com.example.triviaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)

        val userAnswers = intent.getStringArrayListExtra("userAnswers")
        val correctAnswers = intent.getStringArrayListExtra("correctAnswers")

        var score = 0
        if (userAnswers != null && correctAnswers != null) {
            for (i in userAnswers.indices) {

                if (userAnswers[i] == correctAnswers[i]) {
                    score++
                }
            }
        }

        val resultText = findViewById<TextView>(R.id.resultText)
        resultText.text = "Du fick $score av ${correctAnswers?.size ?: 0} rätt!"


        val backButton = findViewById<Button>(R.id.backToMainButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }









        //-------------------------------------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}