package com.example.aanchalsharma.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String APP_ID ="YOUR API KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button1;
        button1 = findViewById(R.id.button1);

        

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView) findViewById(R.id.textview1);
                String units = "metric";
               // double lat = 40.712774, lon = -74.006091;
                /*EditText editText1 = findViewById(R.id.et1);
                EditText editText2 = findViewById(R.id.et2);
                double lat = (Double.parseDouble(editText1.getText().toString()));
                double lon = (Double.parseDouble(editText2.getText().toString()));*/

                EditText editText3 = findViewById(R.id.et3);
                String city = editText3.getText().toString();

               // String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                 //       lat, lon, units, APP_ID);

                String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=%s",city,units,APP_ID);

                new GetWeatherTask(textView).execute(url);
            }
        });


    }
    public static List<String> LIST = new ArrayList<String>();

    private class GetWeatherTask extends AsyncTask<String, Void, List<String>> {

        private TextView textView;

        public GetWeatherTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            /*String weather = "UNDEFINED";
            String weathermin = "UNDEFINED";
            String weathermax = "UNDEFINED";*/
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                LIST.add(String.valueOf(main.getDouble("temp")));
                LIST.add(String.valueOf(main.getDouble("temp_min"))) ;
                LIST.add(String.valueOf(main.getDouble("temp_max")));



                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return LIST;
        }

        @Override
        protected void onPostExecute(List <String> LIST) {
         //   textView.setText("Current Weather: " + temp);


            StringBuilder builder = new StringBuilder();
            builder.append("Temp   "+"Min Temp   "+"Max Temp"+ "\n");
            for (String details : LIST) {
                builder.append(details + "         ");
            }

            textView.setText(builder.toString());
            LIST.clear();

        }
    }
}
