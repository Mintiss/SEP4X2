package sep4x2.android.ui.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static sep4x2.android.ui.network.NetworkConfig.BASE_URL;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static SensorAPI sensorAPI = retrofit.create(SensorAPI.class);

    public  static SensorAPI getAllMetrics() {return  sensorAPI;}
}
