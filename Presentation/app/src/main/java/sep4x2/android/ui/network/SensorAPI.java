package sep4x2.android.ui.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SensorAPI {

    @GET("api/metrics")
    Call<SensorResponse> getAllMetrics();


}
