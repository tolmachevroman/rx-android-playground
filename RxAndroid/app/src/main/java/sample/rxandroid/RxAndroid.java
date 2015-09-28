package sample.rxandroid;

import android.app.Application;

import sample.rxandroid.network.RestApi;

/**
 * Created by romantolmachev on 25/9/15.
 */
public class RxAndroid extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestApi.initialize();
    }
}
