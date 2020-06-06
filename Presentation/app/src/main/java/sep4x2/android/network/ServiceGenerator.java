package sep4x2.android.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static sep4x2.android.network.NetworkConfig.BASE_URL;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static SensorAPI sensorAPI = retrofit.create(SensorAPI.class);

    private static UserAPI userAPI = retrofit.create(UserAPI.class);

    //private static UserAPI userAPI = retrofit.create(UserAPI.class);

    //API for sensors
    //name will be changed after we have more than one api inside the sensor api
    public  static SensorAPI getSensorAPI() {return  sensorAPI;}

    //API for User
    public static UserAPI getUserAPI() {return userAPI;}
}
