package com.stuart_harrison.petrolpricesparser;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RSSParserClass {

    private static String urlString = "";
    private static String DEBUG_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<PetrolPrices><Fuel type=\"Super Unleaded\">" +
            "<Highest units=\"p\">139.9</Highest>" +
            "<Average units=\"p\">115.8</Average>" +
            "<Lowest units=\"p\">104.9</Lowest>" +
            "</Fuel><Fuel type=\"Unleaded\">" +
            "<Highest units=\"p\">124.9</Highest>" +
            "<Average units=\"p\">106.7</Average>" +
            "<Lowest units=\"p\">99.7</Lowest>" +
            "</Fuel><Fuel type=\"LRP\">" +
            "<Highest units=\"p\">117.9</Highest>" +
            "<Average units=\"p\">118.9</Average>" +
            "<Lowest units=\"p\">117.9</Lowest>" +
            "</Fuel><Fuel type=\"Premium Diesel\">" +
            "<Highest units=\"p\">143.9</Highest>" +
            "<Average units=\"p\">121.6</Average>" +
            "<Lowest units=\"p\">112.9</Lowest>" +
            "</Fuel><Fuel type=\"Diesel\">" +
            "<Highest units=\"p\">129.9</Highest>" +
            "<Average units=\"p\">109.3</Average>" +
            "<Lowest units=\"p\">103.7</Lowest>" +
            "</Fuel><Fuel type=\"LPG\">" +
            "<Highest units=\"p\">61.9</Highest>" +
            "<Average units=\"p\">52.4</Average>" +
            "<Lowest units=\"p\">49.7</Lowest>" +
            "t</Fuel></PetrolPrices>";

    public RSSParserClass(String incomingURL) {
        urlString = incomingURL;
    }

    public ArrayList<FuelType> parseFuelFeed() {
        ArrayList<FuelType> fuelData = new ArrayList<FuelType>();
        try {
            String fuelName = "", highest = "", lowest = "", average = "";
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            String stringToParse = DEBUG_STRING;
            xpp.setInput(new StringReader(stringToParse));
            int eventType = xpp.getEventType();
            Boolean flag = false;
            while (!flag) {
                if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("fuel")) {
                        Log.i("RSSParserClass", "Got: " + fuelName);
                        fuelData.add(new FuelType(fuelName, highest, lowest, average));
                        eventType = xpp.next();
                        continue;
                    }
                    if (xpp.getName().equalsIgnoreCase("PetrolPrices")) {
                        flag = true;
                    }
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("fuel")) {
                        fuelName = xpp.getAttributeValue(null, "type");
                    }
                    if (xpp.getName().equalsIgnoreCase("highest")) {
                        highest = xpp.nextText();
                    }
                    if (xpp.getName().equalsIgnoreCase("average")) {
                        average = xpp.nextText();
                    }
                    if (xpp.getName().equalsIgnoreCase("lowest")) {
                        lowest = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }
            Log.d("RSSParserClass", "Finished Loop");
        }
        catch (Exception e) {
            Log.e("RSSParserClassPA", "Error: " + e.getMessage());
        }
        return fuelData;
    }

    private String downloadStream() {
        String stream = null;
        try {
            InputStream in = null;
            URL link = new URL(urlString);
            Log.d("RSSParserClassDL", "URL: " + urlString);
            HttpURLConnection connection = (HttpURLConnection)link.openConnection();
            in = connection.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            for (int count; (count = in.read(buffer)) != -1; ) {
                output.write(buffer, 0, count);
            }

            byte[] feed = output.toByteArray();
            stream = new String(feed, "UTF-8");
        }
        catch (Exception e) {
            Log.e("RSSParserClassDL", "Error: " + e.getMessage());
        }
        Log.i("RSSParserClass", "Got String: " + stream);
        return stream;
    }

    private String downloadStream2() throws IOException {
        String result = "";
        InputStream anInStream = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        // Checks the connection to the stream
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            // Opens the connection
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            // Checks that the connection is Ok
            if (response == HttpURLConnection.HTTP_OK) {
                // if connection is ok it opens the stream
                anInStream = httpConn.getInputStream();
                InputStreamReader in= new InputStreamReader(anInStream);
                BufferedReader bin= new BufferedReader(in);

                // Reads the data from the XML stream
                bin.readLine(); // Throws away the header
                String line;
                while (( (line = bin.readLine())) != null) {
                    result = result + "\n" + line;
                }
            }
        } catch (Exception ex){
            throw new IOException("Error connecting");
        }

        // Returns the result as a string value
        return result;
    }
}
