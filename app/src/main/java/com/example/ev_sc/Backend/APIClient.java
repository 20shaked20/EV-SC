package com.example.ev_sc.Backend;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;

/**
 * This class is responsible for handling the Client for the server.
 * we're doing here Get/Post http requests to receive or send data to the server.
 */
public class APIClient {

    final String TAG = "API";

    private final OkHttpClient client = new OkHttpClient();

    /**
     * This is a get request sent to the server
     *
     * @param url      the route we chose ( function to activate on the server side )
     * @param callback a callback function
     */
    public void sendGetRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
        Log.d(TAG, "Sent GET request: " + request);
    }

    /**
     * This is a post request we send to the server in order to upload data to the database.
     * we're sending the data as a Map and the server will parse it.
     *
     * @param url      the route we chose ( function to activate on the server side )
     * @param postData the data we wish to upload
     * @param callback a callback function
     */
    public void sendPostRequest(String url, Map<String, Object> postData, Callback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(postData);
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
        Log.d(TAG, "Sent POST request: " + request);

    }
}

