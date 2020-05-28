package sep4x2.android.ui.temperature;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.LocalDatabase;
import sep4x2.android.ui.local_database.SensorDao;
import sep4x2.android.ui.network.SensorAPI;
import sep4x2.android.ui.network.ServiceGenerator;

public class TemperatureRepository {

    private SensorDao sensorDao;
    private MutableLiveData<List<SensorData>> temperatureData;

    private static TemperatureRepository instance;



/*   private TemperatureRepository(Application application)
   {
       LocalDatabase database = LocalDatabase.getInstance(application);
       sensorDao = database.sensorDao();
       temperatureData = new MutableLiveData<>();
       temperatureData.setValue(sensorDao.getTemperature().getValue());
   } */

  /*  public static synchronized TemperatureRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TemperatureRepository(application);
        }
        return instance;
    } */


  /*  public MutableLiveData<List<SensorData>> getTemperatureData() {
        return temperatureData;
    } */
}
