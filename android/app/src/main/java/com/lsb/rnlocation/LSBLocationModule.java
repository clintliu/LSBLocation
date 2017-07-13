package com.lsb.rnlocation;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by liusb on 7/8/17.
 */

public class LSBLocationModule extends ReactContextBaseJavaModule implements LocationListener {

    // React Class Name as called from JS
    public static final String REACT_CLASS = "LSBLocation";
    // Unique Name for Log TAG
    public static final String TAG = LSBLocationModule.class.getSimpleName();
    // Save last Location Provided

    //The React Native Context
    ReactApplicationContext mReactContext;


    private boolean getService = false;
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;



    // Constructor Method as called in Package
    public LSBLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Save Context for later use
        mReactContext = reactContext;


    }
    private void locationServiceInitial() {
        lms = (LocationManager) mReactContext.getSystemService(mReactContext.LOCATION_SERVICE); //取得系統定位服務
        //做法一,由程式判斷用GPS_provider
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
        }
        else if ( lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標
        }
        else {
            // determin the best location manager by criteria
            Criteria criteria = new Criteria();  //資訊提供者選取標準
            bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
            location = lms.getLastKnownLocation(bestProvider);

        }

        getLocation(location);
    }



    private void getLocation(Location location) {
        if(location != null) {
            double Longitude = location.getLongitude();   //取得經度
            double Latitude = location.getLatitude();     //取得緯度


            Log.i(TAG, "LSBLocationModule::Got new location. Lng: " +Longitude+" Lat: "+Latitude);

            // Create Map with Parameters to send to JS
            WritableMap params = Arguments.createMap();
            params.putDouble("Longitude", Longitude);
            params.putDouble("Latitude", Latitude);

            // Send Event to JS to update Location
            sendEvent(mReactContext, "updateLocation", params);




        }
        else {
            Toast.makeText(mReactContext, "Get location failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /*
     * Location Callback as called by JS
     */
    @ReactMethod
    public void getCurrentLocation() {
        Log.i(TAG,"LSBLocationModule::getCurrentLocation");
        try {
            LocationManager status = (LocationManager) (mReactContext.getSystemService(Context.LOCATION_SERVICE));

            if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                locationServiceInitial();
            } else {
                Toast.makeText(mReactContext, "Please enable GPS", Toast.LENGTH_LONG).show();
                getService = true;
//                mReactContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //start gps setting activity
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "LSBLocationModule::Location services disconnected.");
        }

    }

    /*
     * Internal function for communicating with JS
     */
    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        } else {
            Log.i(TAG, "LSBLocationModule::Waiting for CatalystInstance...");
        }
    }


    @Override
    public void onLocationChanged(Location location) {  //當地點改變時

        getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(mReactContext, "GPS is disable.", Toast.LENGTH_LONG).show();
    }
}
