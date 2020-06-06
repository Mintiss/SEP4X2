package sep4x2.android.ui.home;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.ExecutionException;
import sep4x2.android.local_database.Entity.SensorData;
import sep4x2.android.local_database.LocalDatabase;
import sep4x2.android.local_database.SensorDao;
import sep4x2.android.network.SensorDataClient;

public class HomeRepository {

    private SensorDao sensorDao;
    private SensorDataClient sensorDataClient;
    private static HomeRepository instance;

    //FIREBASE
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private HomeRepository(Application application)
    {
        LocalDatabase database = LocalDatabase.getInstance(application);
        sensorDao = database.sensorDao();
        sensorDataClient= SensorDataClient.getInstance(application);
    }

    public static synchronized HomeRepository getInstance(Application application) {
        if (instance == null) {
            instance = new HomeRepository(application);
        }
        return instance;
    }

    public void updateData(){
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = firestore.collection("users").document(firebaseAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String productID = (documentSnapshot.getString("productId"));
                updateDataWithProduct(productID);
            }
        });
    }

    public LiveData<SensorData> getData() {

        try {
            return new getDataAsync(sensorDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDataWithProduct(String productID) {

        sensorDataClient.updateSensorData(productID);

        //Will not be hardcoded weeks when we have real data in db (now. week of year)
        sensorDataClient.updateThisWeekSensors(18,productID);
        sensorDataClient.updateThisWeekSensors(19,productID);
        sensorDataClient.updateThisWeekSensors(20,productID);
        sensorDataClient.updateThisWeekSensors(21,productID);
        sensorDataClient.updateThisWeekSensors(22,productID);
    }

    private static class getDataAsync extends AsyncTask<Void,Void,LiveData<SensorData>>
    {
        private SensorDao sensorDao;

        private getDataAsync(SensorDao sensorDao){
            this.sensorDao=sensorDao;
        }

        @Override
        protected LiveData<SensorData> doInBackground(Void... voids) {
            return sensorDao.getLastestSensorData();
        }
    }

}
