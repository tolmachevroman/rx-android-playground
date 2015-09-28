package sample.rxandroid.network;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by romantolmachev on 25/9/15.
 */
public class RestApi {

    private static RestApiDefinitions service;

    public static void initialize() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Urls.ENDPOINT)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String s) {
                        Log.d("CLIENT API", s);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create()))
                .build();

        service = restAdapter.create(RestApiDefinitions.class);
    }

//    public static Observable<List<Job>> searchJobs(String searchQuery) {
//
//        return service.getJobs(searchQuery);
//    }

    public static void searchJobs(String searchQuery, Callback<List<Job>> callback) {

        service.getJobs(searchQuery, callback);
    }
}