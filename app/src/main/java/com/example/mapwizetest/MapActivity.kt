package com.example.mapwizetest

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.mapwize.mapwizesdk.api.*
import io.mapwize.mapwizeui.MapwizeFragment
import io.mapwize.mapwizeui.MapwizeFragmentUISettings
import io.mapwize.mapwizesdk.map.MapOptions
import io.mapwize.mapwizesdk.map.MapwizeMap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), MapwizeFragment.OnFragmentInteractionListener {

    private var mapwizeFragment: MapwizeFragment? = null
    private var mapwizeMap: MapwizeMap? = null

    private var toPlace: Place? = null
    private var fromPlace: Place? = null

    private var toString: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        // Uncomment and fill place holder to test MapwizeUI on your venue
        val opts = MapOptions.Builder()
            //.restrictContentToOrganization("YOUR_ORGANIZATION_ID")
            .restrictContentToVenueId("5dd98eee510edd0016a4d58b")
            .centerOnVenue("5dd98eee510edd0016a4d58b")
            //.centerOnPlace("YOUR_PLACE_ID")
            .build()

        // Uncomment and change value to test different settings configuration
        var uiSettings = MapwizeFragmentUISettings.Builder()
            //.menuButtonHidden(true)
            //.followUserButtonHidden(false)
            //.floorControllerHidden(false)
            //.compassHidden(true)
            .build()
        mapwizeFragment = MapwizeFragment.newInstance(opts, uiSettings)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(fragmentContainer.id, mapwizeFragment!!)
        ft.commit()
    }

    private fun search(query: String, toOrFrom: String) {
        mapwizeMap?.mapwizeApi?.getPlaceWithAlias(query, mapwizeMap!!.venue!!, object : ApiCallback<Place> {
            override fun onSuccess(@NonNull place: Place) {
                val uiHandler = Handler(Looper.getMainLooper())
                val runnable = {
                    if(toOrFrom == "to"){
                        toPlace = place
                    }
                    else if(toOrFrom == "from"){
                        fromPlace = place
                    }
                    //real thread safe hours (not at all this is horrible i don't care)
                    tryToGetRoute()
                }
                uiHandler.post(runnable)
            }
            override fun onFailure(p0: Throwable) {

                val uiHandler = Handler(Looper.getMainLooper())
                val runnable = {
                    alertView("Place not found!")
                }
                uiHandler.post(runnable)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val toString = intent.getStringExtra("to_string")
        if(!toString.isNullOrEmpty()) {
            this.toString = toString
        }
    }

    private fun tryToGetRoute() {
        //real safe thread locks
        if(toPlace != null && fromPlace != null) {
            mapwizeFragment?.setDirection(null, fromPlace, toPlace, true)
        }
    }

    /**
     * Fragment listener
     */
    override fun onFragmentReady(mapwizeMap: MapwizeMap) {
        this.mapwizeMap = mapwizeMap
        this.mapwizeMap?.addOnVenueEnterListener(object : MapwizeMap.OnVenueEnterListener {
            override fun onVenueWillEnter(p0: Venue) {

            }

            override fun onVenueEnter(p0: Venue) {
                val uiHandler = Handler(Looper.getMainLooper())
                val runnable = {
                    search("entrance", "from")
                    search(toString!!, "to")
                }
                uiHandler.post(runnable)
            }

        })
    }

    override fun onMenuButtonClick() {

    }

    override fun onInformationButtonClick(mapwizeObject: MapwizeObject?) {

    }

    override fun onFollowUserButtonClickWithoutLocation() {
        Log.i("Debug", "onFollowUserButtonClickWithoutLocation")
    }

    override fun shouldDisplayInformationButton(mapwizeObject: MapwizeObject?): Boolean {
        Log.i("Debug", "shouldDisplayInformationButton")

        when (mapwizeObject) {
            is Place -> return true
        }
        return false
    }

    override fun shouldDisplayFloorController(floors: MutableList<Floor>?): Boolean {
        Log.i("Debug", "shouldDisplayFloorController")
        if (floors == null || floors.size <= 1) {
            return false
        }
        return true
    }
    private fun alertView(message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Hai")
            .setMessage(message)
            .setPositiveButton("Ok"
            ) { _, _ -> }.show()
    }
}
