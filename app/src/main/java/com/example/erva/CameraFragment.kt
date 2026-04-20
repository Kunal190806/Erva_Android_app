package com.example.erva

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.erva.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            binding.previewImageView.setImageURI(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra("imageUri")?.let {
                imageUri = Uri.parse(it)
                binding.previewImageView.setImageURI(imageUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.previewButton.setOnClickListener {
            val intent = Intent(requireActivity(), CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }

        binding.analyzeButton.setOnClickListener {
            imageUri?.let {
                val intent = Intent(requireActivity(), AnalysisResultActivity::class.java).apply {
                    putExtra("imageUri", it.toString())
                    putExtra("model", "model_color.tflite")
                }
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
