package sep4x2.android.network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


import sep4x2.android.MainActivity;
import sep4x2.android.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FireBaseMessagingService extends FirebaseMessagingService {

    SensorDataClient sensorDataClient;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sensorDataClient= SensorDataClient.getInstance(getApplication());
        sensorDataClient.updateSensorData();

        if(remoteMessage.getData().size() > 0){
            Map<String, String> data;
            data = remoteMessage.getData();
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("123","SEP4X2 Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1});
            notificationManager.createNotificationChannel(notificationChannel);
            try {

                if(Integer.parseInt(data.get("temperature")) > 25){
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Temperature Update")
                            .setContentText("Current temperature is " + data.get("temperature") + "C which might cause discomfort")
                            .setSmallIcon(R.drawable.ic_brightness_7_black_24dp)
                            .setChannelId("123")
                            .build();
                    notificationManager.notify(1, notification);
                }
                if(Integer.parseInt(data.get("humidity")) > 50){
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Humidity Update")
                            .setContentText("Current humidity is " + data.get("humidity") + "% which might cause health issues")
                            .setSmallIcon(R.drawable.ic_invert_colors_black_24dp)
                            .setChannelId("123")
                            .build();
                    notificationManager.notify(1, notification);
                }
                if(Integer.parseInt(data.get("co2")) > 1000){
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Co2 Update")
                            .setContentText("Current co2 is " + data.get("co2") + "ppm which might cause health problems")
                            .setSmallIcon(R.drawable.ic_local_florist_black_24dp)
                            .setChannelId("123")
                            .build();
                    notificationManager.notify(1, notification);
                }
                if(Integer.parseInt(data.get("noise")) > 10){
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Noise Update")
                            .setContentText("Current noise is " + data.get("noise") + "db which might cause annoyance")
                            .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                            .setChannelId("123")
                            .build();
                    notificationManager.notify(1, notification);
                }


            }
            catch (NullPointerException e){
                Log.d(TAG, "unable to get data from poke.");
            }


        }

        Log.d(TAG, "Sensors updated");
    }
}
