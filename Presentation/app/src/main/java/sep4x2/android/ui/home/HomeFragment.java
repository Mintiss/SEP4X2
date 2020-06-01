package sep4x2.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sep4x2.android.R;

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

        homeViewModel.sensorDataClient.updateSensorData();

        temperature = view.findViewById(R.id.home_temperature);
        humidity = view.findViewById(R.id.home_humidity);
        co2 = view.findViewById(R.id.home_co2);
        noise = view.findViewById(R.id.home_noise);

  /*      homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<SensorData>() {
            @Override
            public void onChanged(SensorData sensorData) {
                temperature.setText(Double.toString(sensorData.getTemperature()));
                humidity.setText(Double.toString(sensorData.getHumidity()));
                co2.setText(Double.toString(sensorData.getCo2()));
                noise.setText(Double.toString(sensorData.getNoise()));
            }
        });

   */





        return view;
    }


}