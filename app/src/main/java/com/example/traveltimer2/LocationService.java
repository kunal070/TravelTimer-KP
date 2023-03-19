package com.example.traveltimer2;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


    public class LocationService {
        public static Location getLocationFromAddress(String address) throws IOException, JSONException {
            // Encode the address string to be used in the URL
            String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");

            // Create the URL for the API request
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress;
            URL url = new URL(apiUrl);

            // Make the API request and get the JSON response
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(conn.getInputStream());
            String jsonString = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Parse the JSON response and get the location
            JSONObject json = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");
            JSONObject result = results.getJSONObject(0);
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            Location loc = new Location("");//provider name is unnecessary
            loc.setLatitude(lat);//your coords of course
            loc.setLongitude(lng);

            return loc;
        }
    }

