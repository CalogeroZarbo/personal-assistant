package com.example.llmapp

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ModelUtils {
    private val TAG = "ModelUtils"
    
    /**
     * Copy model file from assets to app's internal storage
     */
    fun copyModelFromAssets(context: Context, assetFileName: String, destinationFileName: String): Boolean {
        return try {
            val assetManager = context.assets
            val inputStream: InputStream = assetManager.open(assetFileName)
            
            val outputFile = File(context.filesDir, destinationFileName)
            val outputStream = FileOutputStream(outputFile)
            
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            
            inputStream.close()
            outputStream.close()
            
            Log.d(TAG, "Model copied successfully: $destinationFileName")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error copying model from assets: ${e.message}", e)
            false
        }
    }
    
    /**
     * Check if model file exists
     */
    fun modelExists(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }
}