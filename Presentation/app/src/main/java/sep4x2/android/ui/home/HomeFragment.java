package sep4x2.android.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import sep4x2.android.R;
import sep4x2.android.local_database.Entity.SensorData;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private View view;
    private HomeViewModel homeViewModel;
    private TextView temperature, humidity, co2, noise, redBox, greenBox, yellowBox;
    private ProgressBar loading;
    private Button refreshButton;



    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.updateData();

        temperature = view.findViewById(R.id.home_temperature);
        humidity = view.findViewById(R.id.home_humidity);
        co2 = view.findViewById(R.id.home_co2);
        noise = view.findViewById(R.id.home_noise);
        loading = view.findViewById(R.id.home_loading);
        refreshButton = view.findViewById(R.id.home_refresh_button);
        redBox = view.findViewById(R.id.home_red_cube);
        greenBox = view.findViewById(R.id.home_green_cube);
        yellowBox = view.findViewById(R.id.home_yellow_cube);

        redBox.setBackgroundColor(Color.RED);
        greenBox.setBackgroundColor(Color.GREEN);
        yellowBox.setBackgroundColor(Color.YELLOW);


        refreshButton.setOnClickListener(this);

        homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<SensorData>() {
            @Override
            public void onChanged(SensorData sensorData) {
                try {
                    temperature.setText(Double.toString(sensorData.getTemperature()));
                    humidity.setText(Double.toString(sensorData.getHumidity()));
                    co2.setText(Double.toString(sensorData.getCo2()));
                    noise.setText(Double.toString(sensorData.getNoise()));

                    temperature.setTextColor(Color.GREEN);
                    humidity.setTextColor(Color.GREEN);
                    co2.setTextColor(Color.GREEN);
                    noise.setTextColor(Color.GREEN);

                    if((sensorData.getTemperature() >= 21) || (sensorData.getTemperature() < 18)){
                        temperature.setTextColor(Color.YELLOW);
                        if ((sensorData.getTemperature()) >= 24 || (sensorData.getTemperature() < 16))
                            temperature.setTextColor(Color.RED);
                    }

                    if(sensorData.getHumidity() >= 40){
                        humidity.setTextColor(Color.YELLOW);
                        if (sensorData.getHumidity() >= 50)
                            humidity.setTextColor(Color.RED);
                    }

                    if(sensorData.getNoise() >= 50){
                        noise.setTextColor(Color.YELLOW);
                        if (sensorData.getNoise() >= 90)
                            noise.setTextColor(Color.RED);
                    }

                    if(sensorData.getCo2() >= 1000){
                        co2.setTextColor(Color.YELLOW);
                        if (sensorData.getCo2() >= 2000)
                            co2.setTextColor(Color.RED);
                    }
                }
                catch (NullPointerException e){
                    try {
                        loading.setVisibility(View.VISIBLE);
                        Thread.sleep(100);
                        loading.setVisibility(View.INVISIBLE);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        homeViewModel.updateData();
        Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
    }
}