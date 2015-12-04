package com.stuart_harrison.petrolpricesparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public LinearLayout list_view;
    public String urlStringToParser = "http://www.petrolprices.com/feeds/averages.xml";
    //public String urlStringToParser = "http://www.petrolprices.com/feeds/averages.xml?search_type=town&search_value=Glasgow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_view = (LinearLayout)findViewById(R.id.list_item);

        //Parser Stuff
        RSSParserClass parserClass = new RSSParserClass(urlStringToParser);
        ArrayList<FuelType> list = parserClass.parseFuelFeed();

        //Display Results
        for (FuelType fuel : list) {
            TextView newTextView = new TextView(this);
            newTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String value = "Name:" + fuel.Name() + " Prices:" + fuel.HighestString() + " " + fuel.AverageString() + " " + fuel.LowestString();
            Log.i("MainActivity", "Got : " + value);
            newTextView.setText(value);
            list_view.addView(newTextView);
        }
    }
}
