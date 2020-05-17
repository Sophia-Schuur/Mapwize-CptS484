package com.example.mapwizetest

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val keys = preferenceScreen.sharedPreferences.all

            for ((key) in keys) {
                val pref = findPreference<Preference>(key)

                if (pref is EditTextPreference) {
                    val textPreference = pref as EditTextPreference?
                    pref.summary = textPreference!!.text
                }
            }
        }
        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
        }
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            val pref = findPreference<Preference>(key)

            if (pref is EditTextPreference) {
                val textPreference = pref as EditTextPreference?
                pref.summary = textPreference!!.text
            }
        }

    }
}