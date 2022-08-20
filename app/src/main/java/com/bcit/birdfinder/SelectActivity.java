package com.bcit.birdfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_LAT = "BIRD_FINDER.SELECT.DATA_LAT";
    public static final String EXTRA_DATA_LONG = "BIRD_FINDER.SELECT.DATA_LONG";
    public static final String EXTRA_DATA_RANGE = "BIRD_FINDER.SELECT.DATA_RANGE";
    public static final String EXTRA_DATA_SPECIES_CODE = "BIRD_FINDER.SELECT.DATA_SPECIES_CODE";

    public double data_latitude;
    public double data_longitude;
    public int data_range;


    RequestQueue queue;

    RecyclerView select_rv_birds;
    public ArrayList<Bird> bird_list;
    Button map_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        map_button = (Button) findViewById(R.id.select_button_map);
        select_rv_birds = findViewById(R.id.select_recyler_birds);

        bird_list = new ArrayList<Bird>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent incoming_intent = getIntent();
        data_latitude = incoming_intent.getDoubleExtra(MapsActivity.EXTRA_DATA_LAT, 49.0);
        data_longitude = incoming_intent.getDoubleExtra(MapsActivity.EXTRA_DATA_LONG, 122.0);
        data_range = incoming_intent.getIntExtra(MapsActivity.EXTRA_DATA_RANGE, 25);

        System.out.println(data_latitude);
        System.out.println(data_longitude);
        System.out.println(data_range);

        bird_list.clear();

        try {
            sendReq(data_latitude, data_longitude, data_range);
            System.out.println("Error Check");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendReq(double latitude, double longitude, double range) throws JSONException {

        queue = Volley.newRequestQueue(this);
        ArrayList<Bird> result_list = new ArrayList<Bird>();

        String url = String.format("https://api.ebird.org/v2/data/obs/geo/recent?lat=%.2f&lng=%.2f&dist=%.2f&", latitude, longitude, range);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray json = new JSONArray(response);
                    //For every result
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject temp = json.getJSONObject(i);

                        //Create a bird and add it to our Resultlist
                        Bird tempBird = new Bird(temp.getString("sciName"), temp.getString("comName"), temp.getString("speciesCode"));
                        result_list.add(tempBird);
                    }

                    BirdAdapter adapter = new BirdAdapter(result_list);

                    adapter.setOnAdapterItemListener(new OnBirdAdapterItemListener() {
                        @Override
                        public void OnClick(Bird bird) {
                            Intent intent = new Intent(SelectActivity.this, InfoActivity.class);
                            intent.putExtra(EXTRA_DATA_SPECIES_CODE, bird.birdCode);
                            startActivity(intent);
                        }
                    });

                    select_rv_birds.setAdapter(adapter);
                    select_rv_birds.setLayoutManager(new LinearLayoutManager(SelectActivity.this));
                    displayBirds(result_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("error:", volleyError.toString());

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "authorization_code");
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-eBirdApiToken", "8f83qa1ivquf");
                headers.put("Dist", String.valueOf(range));
                return headers;
            }
        };

        queue.add(postRequest);
    }

    public void displayBirds (ArrayList<Bird> birdListParam){
        for (Bird b : birdListParam) {
            System.out.println(b);
        }
    }

    public void GoToMap(View view) {
        finish();
    }
}