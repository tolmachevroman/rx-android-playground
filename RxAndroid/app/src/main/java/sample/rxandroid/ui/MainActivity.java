package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;
import sample.rxandroid.network.RestApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RestApi.searchJobs("nursing+jobs+in+ny", new Callback<List<Job>>() {
//            @Override
//            public void success(List<Job> jobs, Response response) {
//                System.out.println("Jobs found: " + jobs.size());
//                for(Job job : jobs) {
//                    System.out.println(job.getId());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                System.out.println("error:" + error.getMessage());
//            }
//        });


        RestApi.searchJobs("nursing+jobs+in+ny")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Job>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Job> jobs) {
                        System.out.println("Jobs found: " + jobs.size());
                    }
                });

//        Observable.just("one", "two", "three", "four", "five")
//                .map(new Func1<String, Integer>() {
//                    @Override
//                    public Integer call(String s) {
//                        return s.length();
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("Completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer i) {
//                        System.out.println("Letters in word: " + i);
//                    }
//                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
