package com.example.newsapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class MainActivity : AppCompatActivity() {
    lateinit var uri:Uri

    lateinit var storegeref: StorageReference
    lateinit var refrence :DatabaseReference
    val TAG = "MainActivity"
  lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storegeref= FirebaseStorage.getInstance().reference
        refrence = FirebaseDatabase.getInstance().reference

        binding.btnSelectImg.setOnClickListener {
            var intent= Intent(ACTION_GET_CONTENT)
            intent.type="image/*"
            startActivityForResult(intent,25)
        }
        binding.btnSubmit.setOnClickListener {
            var ref = storegeref.child("images/${uri.lastPathSegment}.jpg")
            var uploadTask =ref.putFile(uri)

            var uriTask = uploadTask.continueWithTask {task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    var dowloadUri  = task.result

                    var key = refrence.root.push().key
                    var title=binding.edtTitle.text.toString()
                    var description = binding.edtDescription.text.toString()
                    var data = Newsmodel(title,description,key.toString(),dowloadUri.toString())

                    Toast.makeText(this,"Data Add succesfully",Toast.LENGTH_SHORT).show()
                    binding.run {
                        edtTitle.setText("")
                        edtDescription.setText("")
                        btnSelectImg.setImageResource(0)
                    }


                }
else {
                }
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_OK){
            if (requestCode == 25){
                uri = data?.data!!
                Log.e(TAG, "onActivityResult: ================="+uri.lastPathSegment )
            }
        }
    }




    }
