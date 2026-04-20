package com.example.erva

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.erva.databinding.ActivityAnalysisResultBinding
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class AnalysisResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisResultBinding
    private var tflite: Interpreter? = null
    private var labels: List<String> = emptyList()
    private val modelName = "model_color.tflite" // Using the color model as the default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Load the model and labels safely
        try {
            tflite = Interpreter(loadModelFile(modelName))
            labels = FileUtil.loadLabels(this, "labels.txt")
        } catch (e: Exception) {
            Log.e("AnalysisActivity", "FATAL: Could not load model or labels.", e)
            Toast.makeText(this, "Error: Model or labels file not found.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Get the image URI from the intent
        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString == null) {
            Log.e("AnalysisActivity", "FATAL: No image URI provided to activity.")
            Toast.makeText(this, "Error: No image was provided.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 3. Perform analysis
        try {
            val imageUri = Uri.parse(imageUriString)
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            binding.previewImageView.setImageBitmap(bitmap)
            
            val (diseaseName, confidence) = runInference(bitmap)
            displayResult(diseaseName, confidence)

        } catch (e: Exception) {
            Log.e("AnalysisActivity", "Error during analysis.", e)
            Toast.makeText(this, "Failed to analyze the image.", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadModelFile(modelName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelName)
        return FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
        }
    }

    private fun runInference(bitmap: Bitmap): Pair<String, Float> {
        val interpreter = tflite ?: return "Unknown" to 0.0f

        // Define the image processor
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 255.0f)) // Normalize to [0,1]
            .build()

        // Pre-process the image into a TensorImage
        val tensorImage = imageProcessor.process(TensorImage(DataType.FLOAT32).apply { load(bitmap) })

        // Dynamically create the output buffer based on the model's actual output shape
        val outputTensor = interpreter.getOutputTensor(0)
        val probabilityBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType())

        // Run inference
        interpreter.run(tensorImage.buffer, probabilityBuffer.buffer.rewind())

        // Get the results
        val probabilities = probabilityBuffer.floatArray
        if (labels.size != probabilities.size) {
            Log.e("AnalysisActivity", "Model output size (${probabilities.size}) does not match labels size (${labels.size}).")
            return "Label mismatch" to 0.0f
        }

        val maxProbIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        return if (maxProbIndex != -1) {
            labels[maxProbIndex] to probabilities[maxProbIndex]
        } else {
            "Unknown" to 0.0f
        }
    }

    private fun displayResult(diseaseName: String, confidence: Float) {
        val disease = DiseaseRepository.getDisease(diseaseName)
        if (disease != null) {
            binding.problemTitle.text = disease.name
            binding.problemDescription.text = disease.description
            binding.solutionDescription.text = disease.solution
        } else {
            binding.problemTitle.text = "Unknown or Mismatch"
            binding.problemDescription.text = "Could not identify the disease. Check logs for details."
            binding.solutionDescription.text = "Please try again with a clearer picture."
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tflite?.close()
    }
}
