package sep4x2.android.ui.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SensorAPI {

    @GET("v2/5ece8e703000006d00ea1312")
    Call<SensorResponse> getSensorData();


}
