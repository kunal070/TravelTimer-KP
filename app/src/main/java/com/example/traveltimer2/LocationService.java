package com.example.traveltimer2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationService extends Service {
    private String url2 = getApplicationContext().getString(R.string.read);
    private String url = getApplicationContext().getString(R.string.delete);

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.d("LOCATION_UPDATE", latitude + "," + longitude);
                String s ="LOCATION_UPDATE : "+ latitude + "," + longitude;
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//            String url2="";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response)
                            {
                                //responce code
                                try {
                                    JSONArray jsonArray=new JSONArray(response);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        if(jsonObject.getInt("flag")==1){
                                            String address1=jsonObject.getString("Address");
                                            int distance=jsonObject.getInt("radious");
                                            double latitudein = 0;
                                            double longitudein = 0;
                                            String locationAddress = address1;
                                            Geocoder geocoder = new Geocoder(getApplicationContext());
                                            List<Address> addresses = geocoder.getFromLocationName(locationAddress, 1);
                                            if (addresses != null && !addresses.isEmpty()) {
                                                Address address = addresses.get(0);
                                                latitudein = address.getLatitude();
                                                longitudein = address.getLongitude();

                                                Log.d("TAG", "Latitude: " + latitude + ", Longitude: " + longitude);
                                            } else {
                                                Log.d("TAG", "No address found for location: " + locationAddress);
                                            }

                                            Location location1 = new Location("");
                                            location1.setLatitude(latitude);
                                            location1.setLongitude(longitude);
                                            Location location2 = new Location("");
                                            location2.setLatitude(latitudein);
                                            location2.setLongitude(longitudein);

                                            float distanceMeters = location1.distanceTo(location2);


                                            Log.e("Location Distance ",location1+" to "+location2+" is "+distanceMeters);

                                            //set Alarm if Place is Reached.

                                            if(distanceMeters<=distance){

                                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>(){
                                                            @Override
                                                            public void onResponse(String response)
                                                            {
                                                                //responce code

                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        //error code


                                                    }
                                                }){
                                                    protected Map<String, String> getParams ()
                                                    {
                                                        Map<String, String> paramV = new HashMap<>();
                                                        paramV.put("address",address1);
                                                        return paramV;
                                                    }
                                                };
                                                queue.add(stringRequest);

                                                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                                long triggerTime = SystemClock.elapsedRealtime() + 5000;
                                                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
                                            }


                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error code


                    }
                });
                queue.add(stringRequest);




            }
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by notification service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID,builder.build());
    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            String action=intent.getAction();
            if(action!=null){
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



}