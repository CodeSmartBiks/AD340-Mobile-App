package com.biksapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData extends AsyncTask<Void,Void,Void> {
    String data= "";
    String dataParsed = "";
    String singleParsed="";
    @Override
    protected Void doInBackground (Void... voids)
    {
        try {
            URL url = new URL("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream= httpURLConnection.getInputStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line= bufferedReader.readLine();
                data= data + line;

            }
            JSONArray JA= new JSONArray(data);
            for (int i=0; i<JA.length();i++){
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = "Description: "+ JO.get("Description")+"\n"+
                        "Camera: "+ JO.get("ImageUrl");
                dataParsed+=singleParsed;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(Void aVoid){

        super.onPostExecute(aVoid);
    }
}
