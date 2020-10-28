package com.example.textrecognition

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_IMAGE_CAPTURE=10
        const val TAG="TEXT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        exploretextbtn.setOnClickListener {
            openCamera()
        }
    }
    private fun openCamera(){
        val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
        catch (e:ActivityNotFoundException){
            Log.e(TAG,"{$e}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_IMAGE_CAPTURE->{
                val imageBitmap=data?.extras?.get("data") as Bitmap
                firebaseFunction(imageBitmap)
            }
        }
    }
    private fun firebaseFunction(imageBitmap: Bitmap){
        val image=FirebaseVisionImage.fromBitmap(imageBitmap)
        val detector=FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image).addOnSuccessListener {
            var text=it.text
            text.split("\n")
            textView.text=text
        }.addOnFailureListener {
            Toast.makeText(this,"$it",Toast.LENGTH_SHORT).show()
        }
    }
}