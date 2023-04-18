package com.example.traveltimer2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class markerClickActivity extends AppCompatActivity {

    TextView editText,textOfSeekBar;
    String textForAddress;
    SeekBar seekBar;
    Intent intent;
    Statement st;
    String url ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
//        reff= FirebaseDatabase.getInstance().getReference().child("Data");
        intent=getIntent();

         url = getApplicationContext().getString(R.string.create);

        textForAddress = "Address : "+intent.getStringExtra("Address_massage");

        editText=(TextView) findViewById(R.id.textViewForAddress);
        editText.setText(textForAddress);
        seekBar=findViewById(R.id.seekBar);
        textOfSeekBar=findViewById(R.id.textViewSeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textOfSeekBar.setText("" + progress);
                textOfSeekBar.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void addAlarm(View view) {
        Intent intentOfSetAlarm = new Intent(markerClickActivity.this, MainActivity.class);
        String location = intent.getStringExtra("Address_massage");
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {

            Geocoder geocoder = new Geocoder(markerClickActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Address obj = addressList.get(0);
//            Data data=new Data(location,1);
//            reff.push().setValue(location+"0");

            //Insert Data into my table

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//            String url="";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                 new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response)
                    {
                        //responce code
                        if(response.equals("success")){
//                            Toast.makeText(getApplicationContext(),"Data INserted",Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals("failed")){
//                            Toast.makeText(getApplicationContext(),"Data Error",Toast.LENGTH_SHORT).show();
                        }
                        else{
//                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            Log.e(
                                    "Error",response
                            );
                        }

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
                        paramV.put("address", location);
                        paramV.put("flag", "1");
                        paramV.put("userid", "1");
                        paramV.put("radious",seekBar.getProgress()+"");
                        return paramV;
                    }
                };
                    queue.add(stringRequest);



        }
            startActivity(intentOfSetAlarm);

    }

    public void addNotification(View view) {
        Intent intentOfSetNotification = new Intent(markerClickActivity.this, MainActivity.class);
        String location = intent.getStringExtra("Address_massage");
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {

            Geocoder geocoder = new Geocoder(markerClickActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Address obj = addressList.get(0);
//            Data data=new Data(location,0);
//            reff.push().setValue(location+"1");

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//            String url="";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response)
                        {
                            //responce code
                            if(response.equals("success")){
//                                Toast.makeText(getApplicationContext(),"Data INserted",Toast.LENGTH_SHORT).show();
                            }
                            else if(response.equals("failed")){
//                                Toast.makeText(getApplicationContext(),"Data Error",Toast.LENGTH_SHORT).show();
                            }
                            else{
//                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                                Log.e(
                                        "Error",response
                                );
                            }

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
                    paramV.put("address", location);
                    paramV.put("flag", "2");
                    paramV.put("userid", "1");
                    paramV.put("radious",seekBar.getProgress()+"");
                    return paramV;
                }
            };
            queue.add(stringRequest);
            startActivity(intentOfSetNotification);
        }
    }
}