package com.example.llmapp.llm

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Manages downloading of LLM models from remote sources
 */
class ModelDownloadManager(private val context: Context) {
    
    companion object {
        private const val TAG = "ModelDownloadManager"
        private const val TIMEOUT_SECONDS = 30L
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    /**
     * Download a model file from URL to local storage
     */
    fun downloadModel(url: String, destinationPath: String): Boolean {
        return try {
            val request = Request.Builder()
                .url(url)
                .build()
            
            val response = client.newCall(request).execute()
            
            if (response.isSuccessful) {
                response.body?.byteStream()?.use { inputStream ->
                    FileOutputStream(destinationPath).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d(TAG, "Model downloaded successfully to $destinationPath")
                true
            } else {
                Log.e(TAG, "Download failed with code: ${response.code}")
                false
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error downloading model", e)
            false
        }
    }
    
    /**
     * Check if model file exists and has valid size
     */
    fun isModelValid(modelPath: String): Boolean {
        val file = File(modelPath)
        return file.exists() && file.length() > 1024 // Check if file is not empty/very small
    }
}