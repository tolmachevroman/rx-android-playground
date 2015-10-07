package sample.rxandroid.network;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by romantolmachev on 25/9/15.
 */
public interface RestApiDefinitions {

    @GET(Urls.US_JOBS_SEARCH)
    Observable<List<Job>> getJobs(
            @Query("query") String searchQuery
    );

}
