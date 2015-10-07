package sample.rxandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import sample.rxandroid.R;
import sample.rxandroid.network.Job;
import sample.rxandroid.network.RestApi;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.query_edit_text)
    EditText queryEditText;

    @Bind(R.id.us_states_list)
    Spinner usStatesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        final BehaviorSubject<String> query = BehaviorSubject.create(queryEditText.getText().toString());
        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                query.onNext(editable.toString());
            }
        });

        query.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .sample(2, TimeUnit.SECONDS)
                .flatMap(new Func1<String, Observable<List<Job>>>() {
                    @Override
                    public Observable<List<Job>> call(String s) {
                        return RestApi.searchJobs("nursing+jobs", usStatesList.getSelectedItem().toString());
                    }
                })
                .flatMap(new Func1<List<Job>, Observable<Job>>() {
                    @Override
                    public Observable<Job> call(List<Job> jobs) {
                        return Observable.from(jobs);
                    }
                })
                .subscribe(new Action1<Job>() {
                    @Override
                    public void call(Job job) {
                        System.out.println("Job found: " + job.getPositionTitle());
                    }
                });

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
