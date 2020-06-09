package sep4x2.android.network;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import javax.annotation.Nullable;
import sep4x2.android.R;
import sep4x2.android.local_database.Entity.SensorData;


public class FireBaseMessagingService extends FirebaseMessagingService {

    SensorDataClient sensorDataClient;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String productID;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sensorDataClient = SensorDataClient.getInstance(getApplication());
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                productID = (documentSnapshot.getString("productId"));

                sensorDataClient.updateSensorData(productID);

                Log.i("PRODUCT ID GOT FROM FIREBASE IN MESSAG SERVIC", "" + productID);

                if (sensorDataClient.getDataForPoke().size() > 1) {
                    if ((sensorDataClient.getDataForPoke().get(sensorDataClient.getDataForPoke().size() - 1) != null) && (sensorDataClient.getDataForPoke().get(sensorDataClient.getDataForPoke().size() - 2) != null)) {

                        SensorData latestData = sensorDataClient.getDataForPoke().get(sensorDataClient.getDataForPoke().size() - 1);
                        SensorData previousData = sensorDataClient.getDataForPoke().get(sensorDataClient.getDataForPoke().size() - 2);

                        String contentTitle = "", contentText = "";
                        int iconId = 0;
                        int notificationId = new Random().nextInt(99999999);
                        boolean somethingIsWrong = false;


                        if ((latestData.getTemperature() >= previousData.getTemperature() + 10) ||
                                (latestData.getTemperature() <= previousData.getTemperature() - 10)) {
                            contentTitle = "Huge jump in temperature!";
                            contentText = "Current temperature is " + latestData.getTemperature() +
                                    "C, previously recorded temperature was " +
                                    previousData.getTemperature() + "C";
                            iconId = R.drawable.ic_brightness_7_black_24dp;
                            somethingIsWrong = true;
                        }
                        if ((latestData.getHumidity() >= previousData.getHumidity() + 10) || (latestData.getHumidity() <= previousData.getHumidity() - 10)) {
                            contentTitle = "Huge jump in humidity!";
                            contentText = "Current humidity is " + latestData.getHumidity() + "%, previously recorded humidity was " + previousData.getHumidity() + "%";
                            iconId = R.drawable.ic_invert_colors_black_24dp;
                            somethingIsWrong = true;
                        }
                        if ((latestData.getCo2() >= previousData.getCo2() + 500) || (latestData.getCo2() <= previousData.getCo2() - 500)) {
                            contentTitle = "Huge jump in co2!";
                            contentText = "Current co2 is " + latestData.getCo2() + "ppm, previously recorded co2 was " + previousData.getCo2() + "ppm";
                            iconId = R.drawable.ic_local_florist_black_24dp;
                            somethingIsWrong = true;
                        }
                        if ((latestData.getNoise() >= previousData.getNoise() + 30)) {
                            contentTitle = "Huge jump in noise!";
                            contentText = "Current noise is " + latestData.getNoise() + "db, previously recorded noise was " + previousData.getNoise() + "db";
                            iconId = R.drawable.ic_music_note_black_24dp;
                            somethingIsWrong = true;
                        }

                        if (somethingIsWrong) {
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationChannel notificationChannel =
                                    new NotificationChannel("123", "SEP4X2 Channel", NotificationManager.IMPORTANCE_DEFAULT);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{1});
                            notificationManager.createNotificationChannel(notificationChannel);

                            Notification notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(contentTitle)
                                    .setContentText(contentText)
                                    .setStyle(new Notification.BigTextStyle()
                                            .bigText(contentText))
                                    .setSmallIcon(iconId)
                                    .setChannelId("123")
                                    .build();
                            notificationManager.notify(notificationId, notification);
                            somethingIsWrong = false;
                        }
                    } else {
                        Log.i("Error", "Unable to get sensor data");
                    }
                } else {
                    Log.i("Error", "Less then 2 metrics in the local database");
                }
            }
        });
    }
}
