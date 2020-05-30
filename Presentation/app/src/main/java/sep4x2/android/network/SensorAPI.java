package sep4x2.android.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SensorAPI {

   @GET("api/metrics")
   Call<SensorResponse> getSensorData();

}
