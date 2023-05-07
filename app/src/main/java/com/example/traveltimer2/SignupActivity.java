package com.example.traveltimer2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private Button signupButton;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                String url="192.168.6.194/CRUDAPI/adduser.php";
                // Validate user input
                if (isValid(name, email, password)) {
                    // Create user account
                    // Navigate to home activity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response)
                                {
                                    //responce code
                                    if(response.equals("success")){
                                    }else{
                                        Log.e(
                                                "Error",response
                                        );
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    }){
                        protected Map<String, String> getParams ()
                        {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("username", email);
                            paramV.put("name", name);
                            paramV.put("password", password);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);

                } else {
                    Toast.makeText(SignupActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid(String name, String email, String password) {
        // Validate user input here
        return true;
    }
}