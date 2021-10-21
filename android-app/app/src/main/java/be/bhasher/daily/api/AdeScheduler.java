package be.bhasher.daily.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class AdeScheduler {
    public static final AdeScheduler adeScheduler = new AdeScheduler();

    private static final String DATA_URL = "https://ade-scheduler.info.ucl.ac.be/classroom/data";

    private final ArrayList<Classroom> classrooms = new ArrayList<>();

    public AdeScheduler(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(DATA_URL);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());

                    BufferedReader reader = new BufferedReader(isr);
                    String response = reader.lines().collect(Collectors.joining());

                    JSONArray jsonArray = new JSONObject(response).getJSONArray("classrooms");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        classrooms.add(new Classroom(
                                jsonObject.getString("name").split("\\(")[0].trim(),
                                jsonObject.getString("address"),
                                jsonObject.getString("latitude"),
                                jsonObject.getString("longitude")
                        ));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Nullable
    public String search(String name){
        if(name == null) return null;
        for(Classroom classroom: this.classrooms){
            if(classroom.name.equalsIgnoreCase(name)){
                return classroom.address;
            }
        }

        for(Classroom classroom: this.classrooms){
            if(classroom.name.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))){
                return classroom.address;
            }
        }
        return null;
    }

    static class Classroom{
        public final String name;
        public final String address;
        public final String latitude;
        public final String longitude;

        public Classroom(String name, String address, @Nullable String latitude, @Nullable String longitude){
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @NonNull
        @Override
        public String toString(){
            return "name=" + this.name + ";" + "address=" + this.address + ";" + "latitude=" + this.latitude + ";" + "longitude=" + this.longitude + ";";
        }
    }
}
