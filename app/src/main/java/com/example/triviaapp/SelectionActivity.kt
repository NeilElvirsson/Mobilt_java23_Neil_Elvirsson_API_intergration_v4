package com.example.triviaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection)


        val easyButton = findViewById<Button>(R.id.easyButton)
        val mediumButton = findViewById<Button>(R.id.mediumButton)

        easyButton.setOnClickListener {

                val intent = Intent(this, EasyActivity::class.java)
                startActivity(intent)

        }

        mediumButton.setOnClickListener {

            val intent = Intent(this, MediumActivity::class.java)
            startActivity(intent)
        }



    }
}