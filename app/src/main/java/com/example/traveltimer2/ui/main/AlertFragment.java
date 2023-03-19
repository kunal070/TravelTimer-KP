package com.example.traveltimer2.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveltimer2.MyListAdapter;
import com.example.traveltimer2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlertFragment extends Fragment {

    private ListView listView;
    private DatabaseReference reff;
    private ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> addresses=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    public AlertFragment() {
        // Required empty public constructor
    }

    public static AlertFragment newInstance(String param1, String param2) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url =  getActivity().getApplicationContext().getString(R.string.read);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//            String url="";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response)
                    {
                        //responce code
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.getInt("flag")==2){
                                    String address=jsonObject.getString("Address");
                                    addresses.add(address);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error code


            }
        });
        queue.add(stringRequest);
        listView=getActivity().findViewById(R.id.list_item_alert);
        MyListAdapter adapter = new MyListAdapter(getActivity().getApplicationContext(), addresses);
        listView.setAdapter(adapter);
    }
}