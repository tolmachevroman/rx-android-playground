package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.jakewharton.rxbinding.widget.AdapterViewSelectionEvent;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;
import sample.rxandroid.network.RestApi;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.query_search_view)
    SearchView querySearchView;

    @Bind(R.id.us_states_list)
    Spinner usStatesList;

    @Bind(R.id.jobs_list)
    ListView jobsList;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    JobAdapter jobsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        jobsAdapter = new JobAdapter(this);
        jobsList.setAdapter(jobsAdapter);

        Observable.combineLatest(

                RxSearchView.queryTextChangeEvents(querySearchView),

                RxAdapterView.selectionEvents(usStatesList),

                new Func2<SearchViewQueryTextEvent, AdapterViewSelectionEvent, String>() {
                    @Override
                    public String call(SearchViewQueryTextEvent searchViewQueryTextEvent, AdapterViewSelectionEvent adapterViewSelectionEvent) {

                        /**
                         * Thread: main
                         *
                         * We need to clear previously loaded jobs list and show progress bar
                         */

                        if (jobsAdapter != null) {
                            jobsAdapter.clearJobs();
                            jobsAdapter.notifyDataSetChanged();
                        }

                        progressBar.setVisibility(View.VISIBLE);

                        return searchViewQueryTextEvent.queryText() + "+jobs+in+" + adapterViewSelectionEvent.view().getSelectedItem();

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

                        /**
                         * Thread: Retrofit-Idle
                         *
                         * We need to hide progress bar and show jobs list
                         */

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.GONE);
                                jobsList.setVisibility(View.VISIBLE);

                            }
                        });

                        return Observable.from(jobs);
                    }
                })
                .subscribe(new Subscriber<Job>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(final Job job) {

                        System.out.println("Job found: " + job.getPositionTitle());

                        /**
                         * Thread: Retrofit-Idle
                         *
                         * We add jobs one by one to the list
                         */

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (jobsAdapter != null) {
                                    jobsAdapter.addJob(job);
                                    jobsAdapter.notifyDataSetChanged();
                                }

                            }
                        });

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
