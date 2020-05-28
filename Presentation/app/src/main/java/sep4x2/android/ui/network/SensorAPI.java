package sep4x2.android.ui.network;

import retrofit2.Call;
import retrofit2.http.GET;
import sep4x2.android.ui.local_database.Entity.SensorData;

public interface SensorAPI {

<<<<<<< HEAD
   @GET("v2/5ece8e703000006d00ea1312")
   Call<SensorData> getSensorData();


=======
    @GET("api/metrics")
    Call<SensorResponse> getAllMetrics();
>>>>>>> parent of f38ccbe... broken temperature


}
