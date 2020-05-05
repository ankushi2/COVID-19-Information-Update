package com.example.secondapp;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static Button click;
    public static TextView fetched;
    public static Button extraButton;
    public static EditText userInput;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click = (Button)findViewById(R.id.clickBtn);
        extraButton = (Button)findViewById(R.id.extraBtn);
        userInput = (EditText)findViewById(R.id.userTypeTxt);
        fetched = (TextView)findViewById(R.id.fetchedTxtView);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        final String url = "https://covid-19-statistics.p.rapidapi.com/reports?iso=USA&region_name=US&date=2020-05-04&q=";

        click.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         String userInput2 = userInput.getText() + "";

                                         StringRequest stringRequest = new StringRequest(Request.Method.GET, url + URLEncoder.encode(userInput2),
                                                 new Response.Listener<String>() {
                                                     @Override
                                                     public void onResponse(String response) {
                                                         String results = getResultList(response);
                                                         fetched.setText(results);
                                                         requestQueue.stop();
                                                     }
                                                 }, new Response.ErrorListener() {
                                             @Override
                                             public void onErrorResponse(VolleyError error) {
                                                 fetched.setText("Error encountered ..");
                                                 error.printStackTrace();
                                             }
                                         }) {
                                             @Override
                                             public Map<String, String> getHeaders() throws AuthFailureError {
                                                 HashMap<String, String> headers = new HashMap<String, String>();
                                                 headers.put("x-rapidapi-host", "covid-19-statistics.p.rapidapi.com");
                                                 headers.put("x-rapidapi-key", "77fefa7173msh5766dace0799083p18c1bajsnd30aeb500d5d");
                                                 return headers;
                                             }
                                         };

                                         requestQueue.add(stringRequest);
                                     }
                                 });

        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.cdc.gov/coronavirus/2019-ncov/index.html";
                Uri webadd = Uri.parse(url);
                Intent goToCDC = new Intent(Intent.ACTION_VIEW, webadd);
                startActivity(goToCDC);
            }
        });
    }

    public static String getResultList(String response) {
         ArrayList<String> resultList = new ArrayList<String>();
        StringBuilder resultStr = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(response);
            Iterator<String> keys = jsonObject.keys();
            List<String> listProps = new ArrayList<String>(Arrays.asList("date", "confirmed", "active", "deaths", "fatality_rate", "last_update"));

            while (keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONArray) {
                    JSONArray array = (JSONArray) jsonObject.get(key);
                    JSONObject object = (JSONObject) array.get(0);
                    resultList = printKeys(listProps, object);
                }
            }
        } catch (Exception ex){
        }
        for (String result:resultList){
            resultStr.append(result).append("\n");
        }
        return resultStr.toString();
    }

    public static ArrayList<String> printKeys(List<String> listNames, JSONObject jsonObject) throws JSONException {
        ArrayList<String> resultList = new ArrayList<String>();
        for (String name : listNames) {
            resultList.add(name + ":" + jsonObject.get(name));
        }
        return resultList;
    }
}
