package sep4x2.android.ui.temperature;

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

import sep4x2.android.R;

public class TemperatureFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //Barchart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;
    ArrayList<TemperatureModel> temperatureModelArrayList = new ArrayList<>();
    ArrayList<TemperatureModel> TimeArrayList = new ArrayList<>();
    //For the drop down
    private Spinner spinner;
    private static final String[] paths = {"Daily","Weekly"};
    public String string;
    public int nr;

    private TemperatureViewModel temperatureViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        temperatureViewModel =
                ViewModelProviders.of(this).get(TemperatureViewModel.class);
       final View root = inflater.inflate(R.layout.fragment_temperature, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        final AdapterView.OnItemSelectedListener listener = this;
        temperatureViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Barchart
        barChart = root.findViewById(R.id.TemperatureBarChart);

        //spinner

        spinner = root.findViewById(R.id.spinnerTemperature);
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

        fillHoursAndTemperaturevaluess();
        fillDaysAndTemperaturevaluess2();

        if(num == 1) {
            //instead of getTime it has to be changed to getHours in the hours case
            for (int i = 0; i < temperatureModelArrayList.size(); i++) {
                String hour = temperatureModelArrayList.get(i).getTime();
                double co2 = temperatureModelArrayList.get(i).getTemperature();

                labelsname.add(hour);
                barEntries.add(new BarEntry(i, (float)co2));

            }
        } else {
            for (int i = 0; i < TimeArrayList.size(); i++) {
                String day = TimeArrayList.get(i).getTime();
                double co2 = TimeArrayList.get(i).getTemperature();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Temperature in Celsius");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Temperature");
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


    private void fillHoursAndTemperaturevaluess()
    {
        temperatureModelArrayList.clear();
        temperatureModelArrayList.add(new TemperatureModel("1pm",24.5));
        temperatureModelArrayList.add(new TemperatureModel("9am",24.70));
        temperatureModelArrayList.add(new TemperatureModel("10am",25.5));
        temperatureModelArrayList.add(new TemperatureModel("11am",26.0));
        temperatureModelArrayList.add(new TemperatureModel("1pm",27.5));
    }

    private void fillDaysAndTemperaturevaluess2()
    {
        TimeArrayList.clear();
        TimeArrayList.add(new TemperatureModel("Monday",22.5));
        TimeArrayList.add(new TemperatureModel("Tuesday",25.1));
        TimeArrayList.add(new TemperatureModel("Wednesday",13.0));
        TimeArrayList.add(new TemperatureModel("Thursday",22.0));
        TimeArrayList.add(new TemperatureModel("Friday",22.9));
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
