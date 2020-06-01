package sep4x2.android.ui.humidity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import sep4x2.android.R;
import sep4x2.android.SharedSensors.Humidity;

public class HumidityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Barchart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;
    ArrayList<HumidityModel> humidityModelArrayList = new ArrayList<>();
    ArrayList<HumidityModel> TimeArrayList = new ArrayList<>();
    //For the drop down
    private Spinner spinner;
    private static final String[] paths = {"Daily","Weekly"};
    public String string;
    public int nr;

    //DB
    private List<Humidity> humidityList;
    private List<Double> weeklyHumidity;

    private HumidityViewModel humidityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        humidityViewModel =
                ViewModelProviders.of(this).get(HumidityViewModel.class);
       final View root = inflater.inflate(R.layout.fragment_humidity, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        final AdapterView.OnItemSelectedListener listener = this;
        humidityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Barchart
        barChart = root.findViewById(R.id.HumidityBarChart);

        //DB
        this.humidityList=humidityViewModel.getHumidityData();


        //spinner

        spinner = root.findViewById(R.id.spinnerHumidity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
        return root;
    }

    private void SetBarchart(int num) {
        barEntries =new ArrayList<>();
        labelsname = new ArrayList<>();

        fillHoursAndHumidityvaluess();
        fillDaysAndHumidityvaluess2();

        if(num == 1) {
            for (int i = 0; i < humidityModelArrayList.size(); i++) {
                String hour = humidityModelArrayList.get(i).getTime();
                double co2 = humidityModelArrayList.get(i).getHumindity();

                barEntries.add(new BarEntry(i, (float)co2));
                labelsname.add(hour);
            }
        } else {
            for (int i = 0; i < TimeArrayList.size(); i++) {
                String day = TimeArrayList.get(i).getTime();
                double co2 = TimeArrayList.get(i).getHumindity();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Humidity in percentage");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Humidity");
        barChart.setDescription(description);

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);

        //Set the XAxis Format

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsname));

        //Set the positions of the labels(hours)

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsname.size());
        xAxis.setLabelRotationAngle(270);

        barChart.animateY(400);
        barChart.invalidate();
    }


    private void fillHoursAndHumidityvaluess()
    {
        humidityModelArrayList.clear();
        humidityModelArrayList.add(new HumidityModel("1pm",65.5));
        humidityModelArrayList.add(new HumidityModel("9am",12.0));
        humidityModelArrayList.add(new HumidityModel("10am",34.5));
        humidityModelArrayList.add(new HumidityModel("11am",45.0));
        humidityModelArrayList.add(new HumidityModel("1pm",23.5));
        humidityModelArrayList.add(new HumidityModel("1pm",65.5));
        humidityModelArrayList.add(new HumidityModel("9am",12.0));
        humidityModelArrayList.add(new HumidityModel("10am",34.5));
        humidityModelArrayList.add(new HumidityModel("11am",45.0));
        humidityModelArrayList.add(new HumidityModel("1pm",23.5));
        humidityModelArrayList.add(new HumidityModel("1pm",65.5));
        humidityModelArrayList.add(new HumidityModel("9am",12.0));
        humidityModelArrayList.add(new HumidityModel("10am",34.5));
        humidityModelArrayList.add(new HumidityModel("11am",45.0));
        humidityModelArrayList.add(new HumidityModel("1pm",23.5));
        humidityModelArrayList.add(new HumidityModel("1pm",65.5));
        humidityModelArrayList.add(new HumidityModel("9am",12.0));
        humidityModelArrayList.add(new HumidityModel("10am",34.5));
        humidityModelArrayList.add(new HumidityModel("11am",45.0));
        humidityModelArrayList.add(new HumidityModel("1pm",23.5));
        humidityModelArrayList.add(new HumidityModel("1pm",65.5));
        humidityModelArrayList.add(new HumidityModel("9am",12.0));
        humidityModelArrayList.add(new HumidityModel("10am",34.5));
        humidityModelArrayList.add(new HumidityModel("11am",45.0));


    }

    private void fillDaysAndHumidityvaluess2()
    {
        TimeArrayList.clear();
        TimeArrayList.add(new HumidityModel("Monday",13.5));
        TimeArrayList.add(new HumidityModel("Tuesday",25.1));
        TimeArrayList.add(new HumidityModel("Wednesday",35.0));
        TimeArrayList.add(new HumidityModel("Thursday",12.0));
        TimeArrayList.add(new HumidityModel("Friday",41.9));
    }



    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                SetBarchart(1);

                break;
            case 1:
                SetBarchart(0);

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        SetBarchart(0);
    }
}