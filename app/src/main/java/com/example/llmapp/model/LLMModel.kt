package com.example.llmapp.model

/**
 * LLM Model interface for defining model operations
 */
interface LLMModel {
    fun loadModel(modelPath: String): Boolean
    fun runInference(input: String): String?
    fun close()
    fun getModelInfo(): ModelInfo
}

/**
 * Model information data class
 */
data class ModelInfo(
    val name: String,
    val version: String,
    val inputShape: List<Int>,
    val outputShape: List<Int>,
    val modelSize: Long
)