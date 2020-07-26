package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String City = "London";

    String Key = "1549e74acf9dd4caeb4cc9cda7ea00a3";




    public class DownloadJSON extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... strings) {
            URL url ;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            String result = "";
            try {
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1 ) {

                    result += (char)data;

                    data =inputStreamReader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }
    }


    public class DownloadIcon extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;



            try {
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    TextView txtCity,txtTime,txtValueFeelLike,txtValueHumidity,txtVision,txtTemp;
    ImageView imageView;
    EditText edt;
    Button btn;
    RelativeLayout rlWeather,rlRoot;

    public void Loading(View view){



        City = edt.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + City + "&units=Imperial&appid="+ Key;

        edt.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        rlWeather.setVisibility(View.VISIBLE);
        rlRoot.setBackgroundColor(Color.parseColor("#E6E6E6"));


        DownloadJSON downloadJSON = new DownloadJSON();

        try {

            String result = downloadJSON.execute(url).get();

            JSONObject jsonObject= new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp");

            String humidity = jsonObject.getJSONObject("main").getString("humidity");

            String feel_Like = jsonObject.getJSONObject("main").getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH).format(new Date(time*1000));

            txtTime.setText(sTime);
            txtCity.setText(City);
            txtVision.setText(visibility);
            txtValueFeelLike.setText(feel_Like);
            txtValueHumidity.setText(humidity);
            txtTemp.setText(temp+"°");

            String nameIcon = "10d";
            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");


            String urlIcon = "http://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";


            DownloadIcon downloadIcon = new DownloadIcon();

            Bitmap bitmap = downloadIcon.execute(urlIcon).get();

            imageView.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity= findViewById(R.id.txtCity);
        txtTime =findViewById(R.id.txtTime);
        txtValueFeelLike=findViewById(R.id.txtValueFeelLike);

        txtValueHumidity=findViewById(R.id.txtValueHumidity);
        txtVision = findViewById(R.id.txtValueVision);
        txtTemp = findViewById(R.id.txtValue);
        imageView = findViewById(R.id.imgIcon);
        btn =findViewById(R.id.btn);
        edt = findViewById(R.id.edt);
        rlWeather = findViewById(R.id.rlWeather);
        rlRoot = findViewById(R.id.rlRoot);


    }
}