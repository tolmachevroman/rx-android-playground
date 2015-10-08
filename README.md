## Sample Android app using RxJava and RxAndroid

This project is an example of RxJava+RxAndroid usage. So let's say we want to search through the jobs (teacher, nurse etc) per state (California, Ohio etc.) available via [US Jobs search API](http://search.digitalgov.gov/developer/jobs.html#using-the-api). In the simplest case we need an input field and US states list, and we want to load jobs on either query or state change: 

![First screenshot](https://cloud.githubusercontent.com/assets/560815/10375534/6f5cc8ba-6dcf-11e5-83fc-9cffc5132015.png)

![Second screenshot](https://cloud.githubusercontent.com/assets/560815/10375535/6f5f21b4-6dcf-11e5-8053-31edc5872773.png)

RxJava is about streams of events. In our case, events can be query update, or another state selected, which then should perform API request with given query/state, and show the results:

User typed query string **or** US state selected -> API request -> process and show the results

1) RxAndroid (as for October '15) provides useful shortcuts to listen to UI changes and emit different `Observable`, depending on what UI element it is. Using `.combineLatest()` we can listen to multuple UI widgets even if just one of it emits it's `Observable`.

-> 2) On the next step we should clear previous results and emit `String` which we'll use with API request

-> 3) API request, which emits `Observable<List<Job>>`

-> 4) we extract single `Observable<Job>` out of the `Observable<List<Job>>` and finally pass it to the Subscriber

-> 5) Add `Job` one by one to the Adapter

The full Observable code is shown below.

```
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
```
We could make it more complicated, and add filtering by salary and rate interval code (per year, for example):

```
.filter(new Func1<Job, Boolean>() {
            @Override
            public Boolean call(Job job) {
                return job.getMinimum() > 50000 && job.getRateIntervalCode() == RateIntervalCode.PA;
            }
        })
```

### Useful reading:

[Reactive Programming in the Netflix API with RxJava](http://techblog.netflix.com/2013/02/rxjava-netflix-api.html)
