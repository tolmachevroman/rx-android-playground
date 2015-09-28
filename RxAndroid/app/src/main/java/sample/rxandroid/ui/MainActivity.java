package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
                .flatMap(new Func1<List<Job>, Observable<Job>>() {
                    @Override
                    public Observable<Job> call(List<Job> jobs) {
                        return Observable.from(jobs);
                    }
                })
                .filter(new Func1<Job, Boolean>() {
                    @Override
                    public Boolean call(Job job) {
                        return job.getMinimum() > 50000;
                    }
                })
                .subscribe(new Subscriber<Job>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Job job) {
                        System.out.println("Job: " + job.getPositionTitle());
                    }
                });

//        Observable.just(5, 2, 3, 3, 2, 1, 4)
//                .distinct()
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
//                        System.out.println("Item: " + i);
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
