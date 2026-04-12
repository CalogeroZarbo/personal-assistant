package com.example.llmapp

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.MappedByteBuffer

class TFLiteHelper(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val TAG = "TFLiteHelper"
    
    /**
     * Load the TensorFlow Lite model from assets
     */
    fun loadModel(modelFileName: String): Boolean {
        return try {
            val modelBuffer = loadModelFile(modelFileName)
            interpreter = Interpreter(modelBuffer)
            Log.d(TAG, "Model loaded successfully: $modelFileName")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model: ${e.message}", e)
            false
        }
    }
    
    /**
     * Load model file from assets
     */
    private fun loadModelFile(modelFileName: String): MappedByteBuffer {
        val assetFile = context.assets.openFd(modelFileName)
        val fileDescriptor = assetFile.fileDescriptor
        val startOffset = assetFile.startOffset
        val declaredLength = assetFile.declaredLength
        return fileDescriptor.channel.map(
            java.nio.channels.FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )
    }
    
    /**
     * Run inference on input data
     */
    fun runInference(inputData: FloatArray): FloatArray? {
        return try {
            if (interpreter == null) {
                Log.e(TAG, "Model not loaded")
                return null
            }
            
            // Prepare input tensor
            val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, inputData.size), TensorBuffer.FLOAT32)
            inputBuffer.floatArray = inputData
            
            // Prepare output tensor
            val outputBuffer = Array(1) { FloatArray(100) } // Adjust size based on your model
            
            // Run inference
            interpreter?.run(inputBuffer.buffer, outputBuffer)
            
            Log.d(TAG, "Inference completed successfully")
            outputBuffer[0]
        } catch (e: Exception) {
            Log.e(TAG, "Error during inference: ${e.message}", e)
            null
        }
    }
    
    /**
     * Close the interpreter to free resources
     */
    fun close() {
        interpreter?.close()
        interpreter = null
        Log.d(TAG, "Interpreter closed")
    }
    
    /**
     * Get model input size
     */
    fun getInputSize(): Int {
        return interpreter?.let { 
            val inputShape = it.getInputTensor(0).shape()
            inputShape.reduce { acc, i -> acc * i }
        } ?: 0
    }
    
    /**
     * Get model output size
     */
    fun getOutputSize(): Int {
        return interpreter?.let { 
            val outputShape = it.getOutputTensor(0).shape()
            outputShape.reduce { acc, i -> acc * i }
        } ?: 0
    }
}