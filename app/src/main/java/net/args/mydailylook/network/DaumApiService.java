package net.args.mydailylook.network;

import net.args.mydailylook.model.DaumSearchChannel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by arseon on 2016-10-27.
 */
public interface DaumApiService {

    @GET("local/v1/search/keyword.json")
    Call<DaumSearchChannel> daumSearch(@Query("apikey") String apikey,
                                       @Query("query") String query,
                                       @Query("location") String location,
                                       @Query("radius") String radius,
                                       @Query("page") String page);

}
