package com.heveamobile.setsandsteps

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

        main()
    }


    fun main() {
        Log.d(
            "HENK",
            "Start",
        )
        thread {
            blockingCode()
        }
        Log.d(
            "HENK",
            "End",
        )
    }

    fun blockingCode() {
        (1..5_000_000).map { it * it }

        Log.d(
            "HENK",
            "Blocking code finished!",
        )
    }
}