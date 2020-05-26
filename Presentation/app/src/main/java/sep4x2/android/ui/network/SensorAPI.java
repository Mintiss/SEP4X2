package sep4x2.android.ui.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SensorAPI {

    @GET("api/metrics")
    Call<MetricsResponse> getAllMetrics();


}
