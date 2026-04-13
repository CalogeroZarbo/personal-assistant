package com.example.llmapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.llmapp.llm.LLMInferenceEngine
import com.example.llmapp.llm.LLMModelManager
import com.example.llmapp.llm.ModelDownloadManager
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    private val TAG = "LLMApp"
    
    private lateinit var inputEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var outputTextView: TextView
    private lateinit var inferenceManager: LLMInferenceManager
    private lateinit var modelManager: LLMModelManager
    private lateinit var inferenceEngine: LLMInferenceEngine
    private lateinit var downloadManager: ModelDownloadManager
    private val disposables = CompositeDisposable()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        inputEditText = findViewById(R.id.inputEditText)
        submitButton = findViewById(R.id.submitButton)
        outputTextView = findViewById(R.id.outputTextView)
        
        // Initialize LLM components
        modelManager = LLMModelManager(this)
        inferenceEngine = LLMInferenceEngine(this)
        downloadManager = ModelDownloadManager(this)
        inferenceManager = LLMInferenceManager(this)
        
        // Set up click listener
        submitButton.setOnClickListener {
            val input = inputEditText.text.toString()
            if (input.isNotEmpty()) {
                processInput(input)
            }
        }
        
        // Load model on startup
        loadModel()
    }
    
    private fun loadModel() {
        // Load the model in background
        disposables.add(
            inferenceManager.initializeModel("llm_model.tflite")
                .subscribe(
                    { success ->
                        if (success) {
                            Log.d(TAG, "Model loaded successfully")
                            outputTextView.text = "Model loaded. Ready for inference."
                        } else {
                            Log.e(TAG, "Failed to load model")
                            outputTextView.text = "Error: Failed to load model"
                        }
                    },
                    { error ->
                        Log.e(TAG, "Error loading model: ${error.message}", error)
                        outputTextView.text = "Error: ${error.message}"
                    }
                )
        )
    }
    
    private fun processInput(input: String) {
        Log.d(TAG, "Processing input: $input")
        outputTextView.text = "Processing: $input\n\nRunning inference..."
        
        // Run inference in background
        disposables.add(
            inferenceManager.runInference(input)
                .subscribe(
                    { response ->
                        Log.d(TAG, "Inference completed: $response")
                        outputTextView.text = "Input: $input\n\nResponse: $response"
                    },
                    { error ->
                        Log.e(TAG, "Error during inference: ${error.message}", error)
                        outputTextView.text = "Error: ${error.message}"
                    }
                )
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        inferenceManager.close()
        inferenceEngine.close()
    }
}