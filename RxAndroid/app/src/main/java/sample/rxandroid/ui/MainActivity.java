package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;
import sample.rxandroid.network.RestApi;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.query_edit_text)
    SearchView queryEditText;

    @Bind(R.id.us_states_list)
    Spinner usStatesList;

    @Bind(R.id.jobs_list)
    ListView jobsList;

    JobAdapter jobsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        jobsAdapter = new JobAdapter(this);
        jobsList.setAdapter(jobsAdapter);

        Observable.combineLatest(

                RxSearchView.queryTextChangeEvents(queryEditText),

                observeSelect(usStatesList),

                new Func2<SearchViewQueryTextEvent, String, String>() {
                    @Override
                    public String call(SearchViewQueryTextEvent searchViewQueryTextEvent, String state) {
                        return queryEditText.getQuery().toString() + "+jobs+in+" + state;
                    }
                }

        )
                .flatMap(new Func1<CharSequence, Observable<List<Job>>>() {
                    @Override
                    public Observable<List<Job>> call(CharSequence query) {
                        return RestApi.searchJobs(query.toString());
                    }
                })
                .flatMap(new Func1<List<Job>, Observable<Job>>() {
                    @Override
                    public Observable<Job> call(List<Job> jobs) {
                        return Observable.from(jobs);
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
                        System.out.println("Job found: " + job.getPositionTitle());
                    }
                });

//                .filter(new Func1<Job, Boolean>() {
//                    @Override
//                    public Boolean call(Job job) {
//                        return job.getMinimum() > 50000;
//                    }
//                })
//

    }

    public static Observable<String> observeSelect(Spinner spinner) {
        final PublishSubject<String> selectSubject = PublishSubject.create();
        // for production code, unsubscribe, UI thread assertions are needed
        // see WidgetObservable from rxandroid for example
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                selectSubject.onNext(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return selectSubject;
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
