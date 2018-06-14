package com.example.dhakadvarsha.kyakaru;


import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Math.*;


public class PlaceVisit {

    private int time;
    private double curr_latitude;
    private double curr_longitude;

    public void getPlacesToVisit(int time,double curr_latitude, double curr_longitude){
        String API_KEY = "AIzaSyCvkPVO9PN9rc1Q8-frKtGD_rMrn2fKVAk";
        String API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?query=places+near+me&rankby=distance&opennow";

        try {
            this.time=time;
            this.curr_latitude=curr_latitude;
            this.curr_longitude=curr_longitude;
            URL url = new URL(API_URL + "&location=" + curr_latitude + "," + curr_longitude  + "&key=" + API_KEY);
            new MyApi2().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    class MyApi2 extends AsyncTask<URL,Void,JSONObject>
    {


        MyApi2(){

        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            try
            {

                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) { // Success

                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    JSONObject json=new JSONObject(sb.toString());
                    JSONArray resultArray= (JSONArray) json.get("results");

                    String PlacesOpenAndNearMe[] = new String[10];
                    double destLatitude[] = new double[10];
                    double destLongitude[] = new double[10];
                    String finalPlacesByWalk[] = new String[5];
                    String finalPlacesByCar[] = new String[5];
                    int finalPlacesCarIndex = 0;
                    int finalPlacesWalkIndex = 0;
                    int PlacesOpenAndNearMe_index = 0;

                    for(int i=0;i<resultArray.length();i++) {

                        JSONObject job = (JSONObject) resultArray.get(i);
                        JSONArray types=(JSONArray)job.get("types");
                        int flag = 0;
                        for(int j=0;j<types.length();j++) {

                            String st = (String) types.get(j);
                            if (st.equals("shopping_mall")) {
                                flag = 1;
                                break;
                            }
                        }

                            if (flag == 1) //it is a shopping mall which is near me and is open now
                            {
                                String PlaceName = (String) job.get("name");
                                if (PlacesOpenAndNearMe_index == 10) {
                                    break;
                                }
                                PlacesOpenAndNearMe[PlacesOpenAndNearMe_index] = PlaceName;

                                JSONObject geometry = (JSONObject) job.get("geometry");
                                JSONObject location = (JSONObject) geometry.get("location");
                                double lat = (double) location.get("lat");
                                double lng = (double) location.get("lng");

                                destLatitude[PlacesOpenAndNearMe_index] = lat;
                                destLongitude[PlacesOpenAndNearMe_index] = lng;

                                double distance = 0;

                                double lat1 = curr_latitude;
                                double long1 = curr_longitude;
                                double lat2 = destLatitude[PlacesOpenAndNearMe_index];
                                double long2 = destLongitude[PlacesOpenAndNearMe_index];

                                //Walking
                                URL url = new URL("http://dev.virtualearth.net/REST/V1/Routes/Walking?wp.0=Eiffel%20Tower&wp.1=louvre%20museum&optmz=distance&output=xml&key=BingMapsKey");

                                double dLat = (lat2 - lat1) / 180 * PI;
                                double dLong = (long2 - long1) / 180 * PI;

                                double a = (sin(dLat / 2) * sin(dLat / 2))
                                        + (cos(lat1 / 180 * PI) * cos(lat2 / 180 * PI)
                                        * sin(dLong / 2) * sin(dLong / 2));
                                double c = 2 * atan2(sqrt(a), sqrt(1 - a));

                                //Calculate radius of earth
                                // For this you can assume any of the two points.
                                double radiusE = 6378135; // Equatorial radius, in metres
                                double radiusP = 6356750; // Polar Radius

                                //Numerator part of function
                                double nr = pow(radiusE * radiusP * cos(lat1 / 180 * PI), 2);
                                //Denominator part of the function
                                double dr = pow(radiusE * cos(lat1 / 180 * PI), 2)
                                        + pow(radiusP * sin(lat1 / 180 * PI), 2);
                                double radius = sqrt(nr / dr);

                                //Calculate distance in meters.
                                distance = radius * c;

                                double carSpeed = 11.11; //metres per second for 40kmph
                                double commuteTimeByCar = distance / carSpeed; //in seconds
                                Log.v("Distance = ", String.valueOf(distance));
                                Log.v("Time by Car = ", String.valueOf(commuteTimeByCar));

                                double walkSpeed = 0.5; //metres per second for 40kmph
                                double commuteTimeByWalk = distance / walkSpeed; //in seconds
                                Log.v("Distance = ", String.valueOf(distance));
                                Log.v("Time by Walk = ", String.valueOf(commuteTimeByWalk));


                                double totalTimeByCar = 2 * commuteTimeByCar + 3600; //3600 seconds are considered for eating
                                double totalTimeByWalk = 2 * commuteTimeByWalk + 3600; //3600 seconds are considered for eating
                                if (totalTimeByWalk <= time) {
                                    finalPlacesByWalk[finalPlacesWalkIndex] = PlaceName;
                                    finalPlacesWalkIndex++;
                                }
                                if (totalTimeByCar <= time) {
                                    finalPlacesByCar[finalPlacesCarIndex] = PlaceName;
                                    finalPlacesCarIndex++;
                                }
                                //return distance; // distance in meters
                                PlacesOpenAndNearMe_index++;


                            }
                        }

                    urlConnection.disconnect();

                } else {
                    // Error handling code goes here
                }

            }
            catch(Exception e) {

                Log.d("TESTTTTTTTTTTTTTT", e.getMessage(), e);
                return null;
            }
            return null;
        }
    }


}
