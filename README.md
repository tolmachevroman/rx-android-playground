## Sample Android app using RxJava and RxAndroid

This project is an example of RxJava+RxAndroid usage. 

*In my opinion, while RxJava is a great tool and concept, it's usage in Android is far from obvious. ContentProviders use Observer pattern already when requery data on change, and we rarely need some complicated filtering in mobile apps. That said, I do believe that knowing RxAndroid can simplify life in some cases.* 

So let's say we want to search through the jobs (teacher, nurse etc) per state (California, Ohio etc.) available via [US Jobs search API](http://search.digitalgov.gov/developer/jobs.html#using-the-api). In the simplest case we need an input field and US states list, and we want to load jobs on either query or state change: 

![First screenshot](https://cloud.githubusercontent.com/assets/560815/10376673/89b996f6-6dd5-11e5-8bc2-5afdacfee2c5.png)

![Second screenshot](https://cloud.githubusercontent.com/assets/560815/10376674/8ae36c5a-6dd5-11e5-86ab-f76f3ad27045.png)

RxJava is about streams of events. In our case, events can be query update, or another state selected, which then should perform API request with given query/state, and show the results:

User typed query string **or** US state selected -> API request -> process and show the results

1) RxAndroid (as for October '15) provides useful shortcuts to listen to UI changes and emit different `Observable`, depending on what UI element it is. Using `.combineLatest()` we can listen to multuple UI widgets even if just one of it emits events at one given moment.

-> 2) We then receive `SearchViewQueryTextEvent` or `AdapterViewSelectionEvent` events and emit `String` which we'll use with API request

-> 3) Perform API request, which emits `Observable<List<Job>>`

-> 4) We then extract single `Observable<Job>` out of the `Observable<List<Job>>` and finally pass it to the Subscriber

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

[ReactiveX Operators](http://reactivex.io/documentation/operators.html)

[ReactiveX Tutorials](http://reactivex.io/tutorials.html)
