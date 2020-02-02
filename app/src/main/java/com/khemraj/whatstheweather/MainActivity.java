package com.khemraj.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView textView;


    public void getWeather(View view){

        textView.setText("");
        if((cityName.getText().toString()).equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter any Valid City Name..!",Toast.LENGTH_SHORT).show();
        }else {
            try {
                DownloadTask task = new DownloadTask();

                task.execute("https://openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.editText);
        textView = findViewById(R.id.weatherInfo);

    }

    class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           try {
               JSONObject jsonObject = new JSONObject(s);
               String weather = jsonObject.getString("weather");
               String temperature = jsonObject.getString("main");
               JSONObject jsonObject1 = new JSONObject(temperature);
               String temp = jsonObject1.getString("temp");
               String description = null;
               String temp_min = jsonObject1.getString("temp_min");
               String temp_max = jsonObject1.getString("temp_max");


               JSONArray jsonArray = new JSONArray(weather);

               for(int i=0; i<jsonArray.length() ;i++) {

                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    description = jsonObject2.getString("description").toUpperCase();

               }

               if(!(description.equals("") && temp_max.equals("") && temp_min.equals(""))) {
                   textView.setText(" Weather Conditions =>>  " + description + "\n\n" + "Current Temperature =>>  " + temp + " Deg " + "\n\n" + "Min Temperature =>>  " + temp_min + " Deg"
                   + "\n\n" + "Max Temperature =>>  " + temp_max + " Deg");
               }else {
                   Toast.makeText(getApplicationContext(),"Please Enter any Valid City Name..!",Toast.LENGTH_SHORT).show();
               }


           }catch (Exception e){
               e.printStackTrace();
               Toast.makeText(getApplicationContext(),"Please Enter any Valid City Name..!",Toast.LENGTH_SHORT).show();
           }


        }
    }
}
