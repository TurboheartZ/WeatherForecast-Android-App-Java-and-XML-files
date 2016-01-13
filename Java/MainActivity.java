package com.zhangxy.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public String cityNamePasser = null;
    public String degreePasser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //When "Search" Button is pressed
        Button btnsearch = (Button) findViewById(R.id.btn_search);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the handlers
                TextView streeinput = (TextView) findViewById(R.id.input_street);
                TextView cityinput = (TextView) findViewById(R.id.input_city);
                Spinner stateinput = (Spinner) findViewById(R.id.input_state);
                RadioGroup degreeinput = (RadioGroup) findViewById(R.id.input_degree);

                //Initialize the value containers
                String streetvalue;
                String cityvalue;
                String statevalue;
                String statevalueinshort;
                String degreevalue;
                String errormsg = "";

                //Get the input values
                streetvalue = streeinput.getText().toString();
                cityvalue = cityinput.getText().toString();
                statevalue = stateinput.getSelectedItem().toString();
                statevalueinshort = getResources().getStringArray(R.array.stateinshort)[stateinput.getSelectedItemPosition()];
                RadioButton mdegree = (RadioButton) findViewById(degreeinput.getCheckedRadioButtonId());
                degreevalue = mdegree.getText().toString().equals("Fahrenheit")?"us":"si";
                degreePasser = degreevalue.equals("us")?"°F":"°C";
                cityNamePasser = cityvalue + ",\t" + statevalueinshort;
                //Set the URL
                String url = "http://zxy-app1-env.elasticbeanstalk.com/?street="+streetvalue+"&city="+cityvalue+"&state="+statevalueinshort+"&degree="+degreevalue;
                final String sendUrl = url.replace(" ","+");
                TextView text_error = (TextView)findViewById(R.id.text_response);
                if(streetvalue==null||streetvalue.equals("")||cityvalue==null||cityvalue.equals("")||statevalue.equals("State")){
                    errormsg = "";
                    if(streetvalue==null||streetvalue.equals("")){
                        errormsg+="Please Enter a Street Address\n";
                    }
                    if(cityvalue==null||cityvalue.equals("")){
                        errormsg+="Please Enter a City Name\n";
                    }
                    if(statevalue.equals("State")){
                        errormsg+="Please Enter a State";
                    }

                    text_error.setText(errormsg);
                }
                else{
                    //Start Asynchronous task

                    text_error.setText("");
                    HttpTask newtask = new HttpTask();
                    newtask.execute(sendUrl);
                }
            }
        });

        //When "Clear" Button is pressed
        Button btnclear = (Button) findViewById(R.id.btn_clear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the handlers
                TextView streeinput_clr = (TextView) findViewById(R.id.input_street);
                TextView cityinput_clr = (TextView) findViewById(R.id.input_city);
                Spinner stateinput_clr = (Spinner) findViewById(R.id.input_state);
                RadioGroup degreeinput_clr = (RadioGroup) findViewById(R.id.input_degree);

                //Start clearing the inputs
                streeinput_clr.setText("");
                cityinput_clr.setText("");
                stateinput_clr.setSelection(0);
                degreeinput_clr.check(findViewById(R.id.btn_fah).getId());
            }
        });

        //When "About" Button is pressed
        Button btnabout = (Button) findViewById(R.id.btn_about);

        ImageButton iconbutton = (ImageButton)findViewById(R.id.icon_forecast);
        iconbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forecasticon();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void act_about(View view){
        Intent intent = new Intent (this, DisplayAboutActivity.class);
        startActivity(intent);
    }

    //Neew class for Asynchronous Tasks
    public class HttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Performed on Background Thread
            String url = params[0];
            String result = null;
            GetFromHttp test = new GetFromHttp();
            try {
                JSONObject testresult = test.getJsonData(url);
                result = testresult.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Done on UI Thread
            String Msg = result;
            if(result!=null&&(!result.equals(""))){
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("MESSAGE",Msg);
                intent.putExtra("cityname",cityNamePasser);
                intent.putExtra("degree",degreePasser);
                startActivity(intent);
            }
        }

    }

    public void forecasticon(){
        Uri uri = Uri.parse("http://forecast.io"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}

