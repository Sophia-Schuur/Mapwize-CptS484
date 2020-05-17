package com.example.mapwizetest

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.TextView

class AudioRoomInputActivity : AppCompatActivity() {

    private var REQUEST_SPEECH_RECOGNIZER: Int = 3000
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_room_input)
        textView = findViewById(R.id.result)
        startSpeechRecognizer()
    }

    private fun startSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_SPEECH_RECOGNIZER){
            if(resultCode == Activity.RESULT_OK){
                textView!!.text = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.get(0)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("room", data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.get(0) )
                startActivity(intent)
            }
        }
    }
}
