package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import sample.rxandroid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RestApi.searchJobs("nursing+jobs+in+ny")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<List<Job>, Observable<Job>>() {
//                    @Override
//                    public Observable<Job> call(List<Job> jobs) {
//                        return Observable.from(jobs);
//                    }
//                })
//                .filter(new Func1<Job, Boolean>() {
//                    @Override
//                    public Boolean call(Job job) {
//                        return job.getMinimum() > 50000;
//                    }
//                })
//                .subscribe(new Subscriber<Job>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Job job) {
//                        System.out.println("Job: " + job.getPositionTitle());
//                    }
//                });

        Observable.just(5, 2, 3, 3, 2, 1, 4)
                .distinct()
                .groupBy(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer % 2 == 0? "EVEN" : "ODD";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupedObservable<String, Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final GroupedObservable<String, Integer> objectIntegerGroupedObservable) {

                        objectIntegerGroupedObservable.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                System.out.println(objectIntegerGroupedObservable.getKey() + ": " + integer);
                            }
                        });

                    }
                });



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
