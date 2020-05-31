package sep4x2.android.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sep4x2.android.network.GETResponses.SensorResponse;
import sep4x2.android.network.GETResponses.WeeklyResponse;

public interface SensorAPI {

   @GET("api/metrics")
   Call<SensorResponse> getSensorData();

   @GET("api/statistics")
   Call<WeeklyResponse> getWeeklySensorData(@Query(value = "start",encoded = true) String start,
                                            @Query(value = "end",encoded = true)String end);

}
