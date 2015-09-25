package sample.rxandroid.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.Retrofit;

/**
 * Created by romantolmachev on 25/9/15.
 */
public class RestApi {

    private static RestApiDefinitions service;

    public static void initialize() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(3, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(3, TimeUnit.MINUTES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.ENDPOINT)
                .client(okHttpClient)
                .build();

        service = retrofit.create(RestApiDefinitions.class);
    }
}