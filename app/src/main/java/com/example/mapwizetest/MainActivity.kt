package com.example.mapwizetest

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import io.mapwize.mapwizeui.MapwizeFragment
import io.mapwize.mapwizeui.MapwizeFragmentUISettings
import io.mapwize.mapwizesdk.api.Floor
import io.mapwize.mapwizesdk.api.MapwizeObject
import io.mapwize.mapwizesdk.api.Place
import io.mapwize.mapwizesdk.map.MapOptions
import io.mapwize.mapwizesdk.map.MapwizeMap
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var roomString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE), 0
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        val roomString = intent.getStringExtra("room")
        if(!roomString.isNullOrEmpty()) {
            this.roomString = roomString
        }
    }

    fun navigateSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun callEmergencyContact(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL)
            val number = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .getString("emergency_contact", "")
            intent.data = Uri.parse("tel:" + number!!)
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE), 0
            )
        }
    }

    fun navigateMap(view: View) {
        if(roomString.isNullOrEmpty()){
            alertView("No Room Number Input!")
        }
        else {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("to_string", roomString)
            startActivity(intent)
        }
    }

    fun requestRoomInput(view: View) {
        val intent = Intent(this, AudioRoomInputActivity::class.java)
        startActivity(intent)
    }

    private fun alertView(message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Hai")
            .setMessage(message)
            .setPositiveButton("Ok"
            ) { _, _ -> }.show()
    }
}