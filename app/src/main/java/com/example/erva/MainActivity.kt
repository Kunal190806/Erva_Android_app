package com.example.erva

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The main entry point of the app. This activity has no UI and its only purpose
 * is to launch the LoginActivity and then finish itself.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Directly launch the Login screen.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // Finish this activity so it is removed from the back stack.
        finish()
    }
}
