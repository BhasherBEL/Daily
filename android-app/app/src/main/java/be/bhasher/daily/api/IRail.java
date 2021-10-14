package be.bhasher.daily.api;

import android.os.StrictMode;
import android.util.JsonReader;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class IRail {
    final static private String baseUrl = "https://api.irail.be/";

    public String mapToGet(HashMap<String, String> args){
        final ArrayList<String> params = new ArrayList<>();

        args.forEach((key, value) -> params.add(key + "=" + value));

        return String.join("&", params);
    }

    public void request(APIS api, HashMap<String, String> arguments){
        this.request(api, this.mapToGet(arguments));
    }

    public void request(APIS api, String arguments){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final URL url = new URL(IRail.baseUrl + api.getPath() + arguments);

                    System.out.println(url.toString());

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());

                    BufferedReader reader = new BufferedReader(isr);
                    String response = reader.lines().collect(Collectors.joining());

                    JsonReader jsonReader = new JsonReader(isr);

                    jsonReader.nextName();

                    System.out.println(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
