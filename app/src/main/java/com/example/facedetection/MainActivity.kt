package com.example.facedetection

import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var t1: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.gobutton)
        button.setOnClickListener {
            try {
                doDetection()
            } catch (e: Exception) {
                Toast.makeText(this, "Ooooh there be an error", Toast.LENGTH_SHORT).show()
            }
        }
        t1 = TextToSpeech(this, this)
    }

    private fun doDetection() {
        val imageStream = resources.openRawResource(R.raw.faces)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val faceView: FaceView = findViewById(R.id.faceview)
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val detector = FaceDetection.getClient(highAccuracyOpts)

        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                val view: TextView = findViewById(R.id.facesdetectedtextview)
                val text = (faces.size.toString() + " faces detected!")
                view.text = (text)
                faceView.setContent(bitmap, faces)
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Face Detection Error", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onInit(status: Int) {
    }


}