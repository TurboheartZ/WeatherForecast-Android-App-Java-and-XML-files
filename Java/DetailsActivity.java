package com.zhangxy.weatherforecast;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    public JSONArray data48 = null;
    public String timezone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DetailsActivity");

        Intent intent = getIntent();

        String retrievedData = intent.getStringExtra("MESSAGE");
        String degreePasser = intent.getStringExtra("degree");
        String cityNamePasser = intent.getStringExtra("cityname");

        try {
            JSONObject jsonData = new JSONObject(retrievedData);
            renderInterface(jsonData, cityNamePasser, degreePasser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button btn_24hours = (Button) findViewById(R.id.btn_next24Hours);
        final Button btn_7days = (Button) findViewById(R.id.btn_next7Days);
        final RelativeLayout layout_24hours = (RelativeLayout) findViewById(R.id.layout_next24Hours);
        final RelativeLayout layout_7days = (RelativeLayout) findViewById(R.id.layout_next7Days);
        btn_24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_24hours.setVisibility(View.VISIBLE);
                layout_7days.setVisibility(View.INVISIBLE);
            }
        });

        btn_7days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_24hours.setVisibility(View.INVISIBLE);
                layout_7days.setVisibility(View.VISIBLE);
            }
        });
    }

    public void renderInterface(JSONObject json, String cityNamePasser, String degreePasser){

        //Set the temp degree shown in the table
        TextView text_temp_degree = (TextView) findViewById(R.id.text_temp_degree);
        text_temp_degree.setText("Temp(" + degreePasser + ")");

        //Set handlers for each JSON object for future parsing
        String img_icon = null;
        String time_in_hour = null;
        String time_in_day = null;
        long timestamp = 0;
        int temp = 0;
        JSONArray data = null;
        JSONArray data_7days = null;
        int tempMax = 0;
        int tempMin = 0;

        try {
            JSONObject hour = json.getJSONObject("hourly");
            data = hour.getJSONArray("data");
            data48 = data;
            JSONObject daily = json.getJSONObject("daily");
            data_7days = daily.getJSONArray("data");
            timezone = json.getString("timezone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject data_content = null;

        //*********Generate the 24 table rows*********
        final TableLayout layout_24_hours = (TableLayout) findViewById(R.id.layout_table);
        for(int i=1;i<=24;i++){
            TableRow tb = new TableRow(this);
            if(i%2==1){
                tb.setBackgroundColor(Color.LTGRAY);
            }
            TableLayout.LayoutParams RowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,100);
            RowParams.setMargins(0, 0, 0, 0);
            tb.setLayoutParams(RowParams);

            //Here we start parsing the JSON values for hours and fill them into the textview
            try {
                data_content = data.getJSONObject(i);
                timestamp = data_content.getLong("time");
                img_icon = getIconName(data_content.getString("icon"));
                temp = data_content.getInt("temperature");
                //Time transfer
                Date dt = new Date(timestamp*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                time_in_hour = sdf.format(dt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView newCol_1 = new TextView(this);
            TableRow.LayoutParams timeParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
            timeParams.leftMargin = 20;
            newCol_1.setLayoutParams(timeParams);
            newCol_1.setText(time_in_hour);
            newCol_1.setHeight(100);
            newCol_1.setTextSize(20);
            tb.addView(newCol_1);

            ImageView newCol_2 = new ImageView(this);
            int icon_id = getResources().getIdentifier("com.zhangxy.weatherforecast:drawable/"+img_icon , null, null);
            newCol_2.setImageResource(icon_id);
            TableRow.LayoutParams imageParams = new TableRow.LayoutParams(60, 60);
            imageParams.setMargins(0, 10, 0, 0);
            newCol_2.setLayoutParams(imageParams);
            tb.addView(newCol_2);

            TextView newCol_3 = new TextView(this);
            TableRow.LayoutParams tempParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
            tempParams.leftMargin = 50;
            newCol_3.setLayoutParams(tempParams);
            newCol_3.setText(String.valueOf(temp));
            newCol_3.setHeight(100);
            newCol_3.setTextSize(20);  //newCol_3.setBackgroundColor(DetailsActivity.this.getResources().getIdentifier("lightblue", "color", "com.zhangxy.weatherforecast"));
            tb.addView(newCol_3);

            layout_24_hours.addView(tb);
        }//Finish filling the 24 hours data

        ImageButton plusbutton = (ImageButton)findViewById(R.id.btn_plusicon);
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow plustable = (TableRow)findViewById(R.id.tb_plusicon);
                plustable.setVisibility(View.GONE);
                JSONObject data_48_content = null;
                long timestamp48 = 0;
                String img_icon48 = null;
                int temp48 = 0;
                String time_in_hour_48 = null;
                for(int i=25;i<=48;i++){
                    TableRow tb = new TableRow(DetailsActivity.this);
                    if(i%2==1){
                        tb.setBackgroundColor(Color.LTGRAY);
                    }
                    TableLayout.LayoutParams RowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,100);
                    RowParams.setMargins(0, 0, 0, 0);
                    tb.setLayoutParams(RowParams);
                    try {
                        data_48_content = data48.getJSONObject(i);
                        timestamp48 = data_48_content.getLong("time");
                        img_icon48 = getIconName(data_48_content.getString("icon"));
                        temp48 = data_48_content.getInt("temperature");
                        //Time transfer
                        Date dt = new Date(timestamp48*1000L);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                        time_in_hour_48 = sdf.format(dt);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //************************************************
                    TextView newCol_1 = new TextView(DetailsActivity.this);
                    TableRow.LayoutParams timeParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                    timeParams.leftMargin = 20;
                    newCol_1.setLayoutParams(timeParams);
                    newCol_1.setText(time_in_hour_48);
                    newCol_1.setHeight(100);
                    newCol_1.setTextSize(20);
                    tb.addView(newCol_1);

                    ImageView newCol_2 = new ImageView(DetailsActivity.this);
                    int icon_id = getResources().getIdentifier("com.zhangxy.weatherforecast:drawable/"+img_icon48 , null, null);
                    newCol_2.setImageResource(icon_id);
                    TableRow.LayoutParams imageParams = new TableRow.LayoutParams(60, 60);
                    imageParams.setMargins(0, 10, 0, 0);
                    newCol_2.setLayoutParams(imageParams);
                    tb.addView(newCol_2);

                    TextView newCol_3 = new TextView(DetailsActivity.this);
                    TableRow.LayoutParams tempParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                    tempParams.leftMargin = 50;
                    newCol_3.setLayoutParams(tempParams);
                    newCol_3.setText(String.valueOf(temp48));
                    newCol_3.setHeight(100);
                    newCol_3.setTextSize(20);  //newCol_3.setBackgroundColor(DetailsActivity.this.getResources().getIdentifier("lightblue", "color", "com.zhangxy.weatherforecast"));
                    tb.addView(newCol_3);

                    layout_24_hours.addView(tb);
                    //************************************************

                }
            }
        });


        //*********Generate the 7 days rows*********
        TableLayout layout_7_days = (TableLayout) findViewById(R.id.table_next7Days);
        for(int i=1;i<=7;i++){

            try {
                JSONObject data_7days_content = data_7days.getJSONObject(i);
                timestamp = data_7days_content.getLong("time");
                img_icon = getIconName(data_7days_content.getString("icon"));
                tempMax = data_7days_content.getInt("temperatureMax");
                tempMin = data_7days_content.getInt("temperatureMin");
                //Time transfer
                Date dt = new Date(timestamp*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd");
                sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                time_in_day = sdf.format(dt);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String colorname = "color" + String.valueOf(i);
            TableRow TB = new TableRow(this);
            TableLayout.LayoutParams newParams=new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newParams.setMargins(0, 10, 0, 0);
            TB.setLayoutParams(newParams);
            Resources res = getResources();
            int color_id = res.getIdentifier(colorname,"color",getPackageName());
            TB.setBackgroundResource(color_id);

            RelativeLayout RL = new RelativeLayout(this);
            TableRow.LayoutParams RLparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//RelativeLayout.LayoutParams.WRAP_CONTENT);
            RL.setLayoutParams(RLparams);

            TextView day_date = new TextView(this);
            day_date.setText(time_in_day);
            day_date.setTextSize(20);
            day_date.setTypeface(null, Typeface.BOLD);
            RelativeLayout.LayoutParams date_params = new RelativeLayout.LayoutParams(600,RelativeLayout.LayoutParams.WRAP_CONTENT);
            date_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            date_params.leftMargin = 10;
            day_date.setLayoutParams(date_params);
            day_date.setId(1);
            RL.addView(day_date);

            TextView day_temp = new TextView(this);
            day_temp.setText("Min: "+tempMin+degreePasser+" | Max: "+tempMax+degreePasser);
            day_temp.setTextSize(20);
            RelativeLayout.LayoutParams temp_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            temp_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            temp_params.addRule(RelativeLayout.BELOW, 1);
            temp_params.leftMargin = 10;
            day_temp.setLayoutParams(temp_params);
            day_temp.setId(2);
            RL.addView(day_temp);

            ImageView day_icon = new ImageView(this);
            int icon_id = getResources().getIdentifier("com.zhangxy.weatherforecast:drawable/"+img_icon , null, null);
            day_icon.setImageResource(icon_id);
            RelativeLayout.LayoutParams icon_params = new RelativeLayout.LayoutParams(60,60);
            icon_params.alignWithParent = true;
            icon_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            icon_params.addRule(RelativeLayout.RIGHT_OF,1);
            icon_params.topMargin = 10;
            icon_params.leftMargin = 300;
            day_icon.setLayoutParams(icon_params);
            RL.addView(day_icon);


            TB.addView(RL);
            layout_7_days.addView(TB);
        }//Finish generating 7 days
    }

    public String getIconName(String icon){
        //Function used to get the true img path
        String imgsrc = "";
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
        return imgsrc;
    }
}
