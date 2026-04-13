package com.example.llmapp

import android.app.Application
import android.util.Log

/**
 * Main application class for LLM inference app
 */
class LLMApplication : Application() {
    private val TAG = "LLMApplication"
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "LLM Application created")
        // Initialize any global components here
    }
}