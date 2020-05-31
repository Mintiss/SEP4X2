package sep4x2.android.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import sep4x2.android.R;
import sep4x2.android.local_database.Entity.SensorData;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {


    private View view;
    private HomeViewModel homeViewModel;
    private TextView temperature, humidity, co2, noise;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //homeViewModel.sensorDataClient.updateSensorData();

        temperature = view.findViewById(R.id.home_temperature);
        humidity = view.findViewById(R.id.home_humidity);
        co2 = view.findViewById(R.id.home_co2);
        noise = view.findViewById(R.id.home_noise);

        homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<SensorData>() {
            @Override
            public void onChanged(SensorData sensorData) {
                temperature.setText(Double.toString(sensorData.getTemperature()));
                humidity.setText(Double.toString(sensorData.getHumidity()));
                co2.setText(Double.toString(sensorData.getCo2()));
                noise.setText(Double.toString(sensorData.getNoise()));
            }
        });




        return view;
    }


}