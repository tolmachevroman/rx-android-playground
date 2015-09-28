package sample.rxandroid.network;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by romantolmachev on 25/9/15.
 */
public interface RestApiDefinitions {

//    @GET(Urls.US_JOBS_SEARCH)
//    Observable<List<Job>> getJobs(
//            @Query("query") String searchQuery
//    );

    @Headers("Content-Type: application/json")
    @GET(Urls.US_JOBS_SEARCH)
     void getJobs(
            @Query("query") String searchQuery,
            Callback<List<Job>> callback
    );
}
