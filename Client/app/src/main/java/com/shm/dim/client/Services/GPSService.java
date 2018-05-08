package com.shm.dim.client.Services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSService extends Service {

    private LocationListener locationListener;
    private LocationManager locationManager;
    private Intent locationUpdateIntent;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationUpdateIntent = new Intent("location_update");
                locationUpdateIntent.putExtra("latitude", location.getLatitude());
                locationUpdateIntent.putExtra("longitude", location.getLongitude());
                locationUpdateIntent.putExtra("speed", location.getSpeed());
                sendBroadcast(locationUpdateIntent);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            // Получать координаты с таймингом в 1 минуту
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }
    }

    @Override
    public void onDestroy() {
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }
}
