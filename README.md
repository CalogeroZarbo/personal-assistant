# Local LLM Inference Android App

This Android application demonstrates TensorFlow Lite integration for local Large Language Model (LLM) inference. The app allows users to input prompts and receive responses using on-device inference, without requiring internet connectivity.

## Features

- **TensorFlow Lite Integration**: Uses TensorFlow Lite for efficient on-device inference
- **Local Inference**: All processing happens on the device
- **Async Processing**: Uses RxJava for non-blocking inference operations
- **Model Management**: Handles model loading, inference execution, and resource cleanup

## Architecture

### Key Components

1. **TFLiteHelper**: Core TensorFlow Lite integration class
   - Handles model loading from assets
   - Manages interpreter lifecycle
   - Performs inference operations

2. **LLMInferenceManager**: High-level inference manager
   - Manages model lifecycle
   - Handles input preprocessing
   - Processes output postprocessing
   - Provides async inference operations

3. **MainActivity**: User interface
   - Text input for prompts
   - Display of inference results
   - Model loading and inference execution

## Implementation Details

### TensorFlow Lite Integration

The app uses TensorFlow Lite with the following dependencies:
- `org.tensorflow:tensorflow-lite:2.13.0`
- `org.tensorflow:tensorflow-lite-support:0.4.4`
- `org.tensorflow:tensorflow-lite-metadata:0.4.4`

### Model Loading

Models are loaded from the `assets` directory. The implementation assumes:
- Model files are placed in `app/src/main/assets/`
- Model files are in `.tflite` format
- Proper preprocessing and postprocessing functions are implemented

### Inference Process

1. **Input Preprocessing**: Text input is converted to numerical format
2. **Model Execution**: TensorFlow Lite interpreter runs inference
3. **Output Postprocessing**: Numerical output is converted back to text format

## Usage

1. Build and install the Android application
2. The app will automatically load the model on startup
3. Enter a prompt in the input field
4. Tap "Submit" to run inference
5. View the response in the output area

## Requirements

- Android SDK 24+
- TensorFlow Lite dependencies included in build.gradle
- Model file named `llm_model.tflite` in assets directory

## Future Improvements

- Add support for different model formats
- Implement proper tokenization for text processing
- Add model download functionality
- Implement caching for better performance
- Add support for different inference configurations