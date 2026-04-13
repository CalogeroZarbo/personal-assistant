package com.example.llmapp.utils

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Utility class for downloading LLM models
 */
class ModelDownloader(private val context: Context) {
    private val TAG = "ModelDownloader"
    private val httpClient = OkHttpClient()
    
    /**
     * Download model from URL to local storage
     */
    fun downloadModel(url: String, fileName: String): Boolean {
        return try {
            val request = Request.Builder()
                .url(url)
                .build()
            
            val response = httpClient.newCall(request).execute()
            
            if (response.isSuccessful && response.body != null) {
                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)
                response.body?.byteStream()?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
                outputStream.close()
                
                Log.d(TAG, "Model downloaded: $fileName")
                true
            } else {
                Log.e(TAG, "Failed to download model: ${response.code}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading model: ${e.message}", e)
            false
        }
    }
    
    /**
     * Check if model exists locally
     */
    fun isModelExists(fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }
}