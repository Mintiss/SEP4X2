package sep4x2.android.network;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FireBaseMessagingService extends FirebaseMessagingService {

    SensorDataClient sensorDataClient;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sensorDataClient= SensorDataClient.getInstance(getApplication());
        sensorDataClient.updateSensorData();
        Log.d(TAG, "Sensors updated");
    }
}
