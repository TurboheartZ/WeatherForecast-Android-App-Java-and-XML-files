package com.zhangxy.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {

    public String city_name_passer = null;
    public String degree_passer = null;
    private CallbackManager callbackManager;
    private CallbackManager callbackManager2;
    private LoginButton loginButton;
    public ShareDialog shareDialog;
    public String weatherinfo_fb = "";
    public String weathericon_fb = "";
    public String cityandstate = "";
    public int loginflag = 0;
    public String latitude = "";
    public String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Innitialize Facebook API
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        callbackManager2 = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ResultActivity");

        Intent intent = getIntent();

        String retrievedData = intent.getStringExtra("MESSAGE");
        String degreePasser = intent.getStringExtra("degree");
        String cityNamePasser = intent.getStringExtra("cityname");
        city_name_passer = cityNamePasser;
        degree_passer = degreePasser;
        cityandstate = cityNamePasser;
        final String data = retrievedData;

        try {
            JSONObject jsonData = new JSONObject(retrievedData);
            latitude = String.valueOf(jsonData.getDouble("latitude"));
            longitude = String.valueOf(jsonData.getDouble("longitude"));
            renderInterface(jsonData, cityNamePasser, degreePasser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btn_moredetails = (Button) findViewById(R.id.btn_moredetails);
        btn_moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDetailsTask newtask = new ShowDetailsTask();
                newtask.execute(data);
            }
        });

        Button btn_map = (Button)findViewById(R.id.btn_viewmap);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMapTask newtask = new ShowMapTask();
                newtask.execute(data);
            }
        });

        ImageButton btn_fb = (ImageButton) findViewById(R.id.btn_facebook);
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_fb();
            }
        });


        //Set callback functions for facebook login
        loginflag = 0;
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginflag = 1;
                shareWeather(shareDialog);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result shareresult) {
                // App code
                if(shareresult.getPostId()!=null){
                    Toast.makeText(ResultActivity.this,"Facebook Post Successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ResultActivity.this,"Post Cancelled",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        callbackManager2.onActivityResult(requestCode, resultCode, data);
    }

    public void renderInterface (JSONObject json, String cityNamePasser, String degreePasser){

        //Initialize values
        String summary = null;
        String icon = null;
        String imgsrc = null;
        String precipitation = null;
        String winddegree = degreePasser.equals("°F")?"mph":"m/s";
        String visibilitydegree = degreePasser.equals("°F")?"mi":"km";
        String sunrise = null;
        String sunset = null;
        int temp = 0;
        int temp_max = 0;
        int temp_min = 0;
        double pre = 0;
        int chanceOfRain = 0;
        double windSpeed = 0;
        double dewPoint = 0;
        int humidity = 0;
        double visibility = 0;
        //Initialize json object handles
        JSONObject currently = null;
        JSONObject daily = null;
        JSONArray data = null;
        JSONObject zero = null;
        try {
            currently = json.getJSONObject("currently");
            daily = json.getJSONObject("daily");
            data = daily.getJSONArray("data");
            zero = data.getJSONObject(0);
            summary = currently.getString("summary");
            icon = currently.getString("icon");
            temp = currently.getInt("temperature");
            temp_max = zero.getInt("temperatureMax");
            temp_min = zero.getInt("temperatureMin");
            pre = currently.getDouble("precipIntensity");
            chanceOfRain = (int)currently.getDouble("precipProbability") * 100;
            windSpeed = (double)Math.round(currently.getDouble("windSpeed") * 100)/100;
            dewPoint = (double)Math.round(currently.getDouble("dewPoint")*100)/100;
            humidity = (int)Math.round(currently.getDouble("humidity")*100);
            visibility = (double)Math.round(currently.getDouble("visibility")*100)/100;
            sunrise = zero.getString("sunriseTime");
            sunset = zero.getString("sunsetTime");

            weatherinfo_fb = summary+", "+temp+degreePasser;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Dispatch the values
        TextView text_summary = (TextView) findViewById(R.id.weather_summary);
        text_summary.setText(summary +" in\t" + cityNamePasser);

        ImageView image_icon = (ImageView) findViewById(R.id.weather_image);
        switch(icon)
        {
            case "clear-day":
                imgsrc="clear";
                break;

            case "clear-night":
                imgsrc="clear_night";
                break;

            case "rain":
                imgsrc="rain";
                break;

            case "snow":
                imgsrc="snow";
                break;

            case "sleet":
                imgsrc="sleet";
                break;

            case "wind":
                imgsrc="wind";
                break;

            case "fog":
                imgsrc="fog";
                break;

            case "cloudy":
                imgsrc="cloudy";
                break;

            case "partly-cloudy-day":
                imgsrc="cloud_day";
                break;

            case "partly-cloudy-night":
                imgsrc="cloud_night";
                break;

            default: break;
        }
        int icon_id = getResources().getIdentifier("com.zhangxy.weatherforecast:drawable/"+imgsrc , null, null);
        image_icon.setImageResource(icon_id);
        weathericon_fb = imgsrc;

        TextView text_temp = (TextView) findViewById(R.id.weather_temp);
        text_temp.setText(String.valueOf(temp));

        TextView text_degree = (TextView) findViewById(R.id.weather_degree);
        text_degree.setText(degreePasser);

        TextView text_temp_lowandhigh = (TextView) findViewById(R.id.weather_temp_lowandhigh);
        text_temp_lowandhigh.setText("L:"+String.valueOf(temp_min)+"° | H:"+String.valueOf(temp_max)+"°");

        TextView text_precipitation = (TextView) findViewById(R.id.weather_precipitation_value);
        if(pre>=0&&pre<0.002){
            precipitation="None";
        }
        else if(pre>=0.002&&pre<0.017){
            precipitation="Very Light";
        }
        else if(pre>=0.017&&pre<0.1){
            precipitation="Light";
        }
        else if(pre>=0.1&&pre<0.4){
            precipitation="Moderate";
        }
        else {
            precipitation="Heavy";
        }
        text_precipitation.setText(precipitation);

        TextView text_rain = (TextView) findViewById(R.id.weather_chanceofrain_value);
        text_rain.setText(String.valueOf(chanceOfRain)+" %");

        TextView text_windSpeed = (TextView) findViewById(R.id.weather_windspeed_value);
        text_windSpeed.setText(String.valueOf(windSpeed)+" "+winddegree);

        TextView text_dewPoint = (TextView) findViewById(R.id.weather_dewpoint_value);
        text_dewPoint.setText(String.valueOf(dewPoint)+" "+degreePasser);

        TextView text_humidity = (TextView) findViewById(R.id.weather_humidity_value);
        text_humidity.setText(String.valueOf(humidity)+" %");

        TextView text_visibility = (TextView) findViewById(R.id.weather_visibility_value);
        text_visibility.setText(String.valueOf(visibility)+" "+visibilitydegree);

        TextView text_sunrise = (TextView) findViewById(R.id.weather_sunrise_value);
        text_sunrise.setText(sunrise);

        TextView text_sunset = (TextView) findViewById(R.id.weather_sunset_value);
        text_sunset.setText(sunset);


    }

    public class ShowDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Performed on Background Thread
            String json_string = params[0];
            return  json_string;
        }

        @Override
        protected void onPostExecute(String result) {
            // Done on UI Thread
            String Msg = result;
            if(!result.equals("")){
                Intent intent = new Intent(ResultActivity.this, DetailsActivity.class);
                intent.putExtra("MESSAGE",Msg);
                intent.putExtra("cityname",city_name_passer);
                intent.putExtra("degree",degree_passer);
                startActivity(intent);
            }
        }

    }


    public class ShowMapTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Performed on Background Thread
            String json_string = params[0];
            return  json_string;
        }

        @Override
        protected void onPostExecute(String result) {
            // Done on UI Thread
            String Msg = result;
            if(!result.equals("")){
                Intent intent = new Intent(ResultActivity.this, MapActivity.class);
                intent.putExtra("MESSAGE",Msg);
                intent.putExtra("cityname",city_name_passer);
                intent.putExtra("degree",degree_passer);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        }

    }

    public void click_fb(){
        final LoginButton button = (LoginButton) findViewById(R.id.login_button);
        if(loginflag == 0){
            button.callOnClick();
        }
        else if(loginflag == 1){
            shareWeather(shareDialog);
        }
    }

    public void shareWeather(ShareDialog shareDialog){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in " + cityandstate)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setContentDescription(weatherinfo_fb)
                    .setImageUrl(Uri.parse("http://www-scf.usc.edu/~xinyizha/homework/hw8/img/"+weathericon_fb+".png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }


}
