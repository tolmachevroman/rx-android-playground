package sample.rxandroid.network;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by romantolmachev on 25/9/15.
 */
public interface RestApiDefinitions {

    @GET(Urls.US_JOBS_SEARCH)
    Observable<Job> getJobs (
            @Query("query") String searchQuery
    );
}
