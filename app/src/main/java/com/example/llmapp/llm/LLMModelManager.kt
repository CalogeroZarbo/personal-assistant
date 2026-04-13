package com.example.llmapp.llm

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Manages LLM models for local inference
 */
class LLMModelManager(private val context: Context) {
    
    companion object {
        private const val TAG = "LLMModelManager"
    }
    
    /**
     * Copies a model file from assets to internal storage
     */
    fun copyModelFromAssets(modelName: String, destinationPath: String): Boolean {
        return try {
            val inputStream: InputStream = context.assets.open(modelName)
            val outputStream = FileOutputStream(destinationPath)
            
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            
            inputStream.close()
            outputStream.close()
            
            Log.d(TAG, "Model copied successfully to $destinationPath")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error copying model from assets", e)
            false
        }
    }
    
    /**
     * Checks if a model file exists
     */
    fun isModelExists(modelPath: String): Boolean {
        val file = File(modelPath)
        return file.exists() && file.length() > 0
    }
    
    /**
     * Gets the internal storage path for models
     */
    fun getModelPath(modelName: String): String {
        return context.filesDir.absolutePath + File.separator + modelName
    }
}