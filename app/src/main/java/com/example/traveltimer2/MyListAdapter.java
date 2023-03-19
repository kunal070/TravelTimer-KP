package com.example.traveltimer2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> items;
    private String url = "http://192.168.132.19/CRUDAPI/delete.php";

    public MyListAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.single_alarm, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_alarm, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.list_item_text);
        Button button = (Button) rowView.findViewById(R.id.list_item_delete);

        textView.setText(items.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
//            String url="";
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
                        paramV.put("address", textView.getText().toString());
                        return paramV;
                    }
                };
                queue.add(stringRequest);
                items.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
