package sep4x2.android.ui.temperature;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import sep4x2.android.R;
import sep4x2.android.ui.local_database.Entity.SensorData;
import sep4x2.android.ui.local_database.Entity.TemperatureData;

public class TemperatureFragment extends Fragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //Barchart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //Linechart
    ArrayList<Entry> yValues = new ArrayList<>();

    ArrayList<TemperatureModel> TimeArrayList = new ArrayList<>();

    //For the drop down
    private Spinner spinner;
    private static final String[] paths = {"Today","Weekly"};
    public String string;
    public int nr;

    private TemperatureViewModel temperatureViewModel;
    private LineChart lineChart;
    private Switch aSwitch;


    //FROM DB
    private List<SensorData>temperatureList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        temperatureViewModel = new ViewModelProvider(this).get(TemperatureViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_temperature, container, false);

        final AdapterView.OnItemSelectedListener listener = this;


        temperatureList=temperatureViewModel.getTemperatureData();

        //Switch
        aSwitch = root.findViewById(R.id.char_switch);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(aSwitch.isChecked())
                {
                   // aSwitch.setVisibility(isVisible(1));
                    //barChart.setVisibility(isVisible(0);
                }else {

                }

            }
        });

        
        //LineChart
        lineChart = (LineChart) root.findViewById(R.id.temperatureLinewchart);

        yValues.add(new Entry(0,(float) 22.5));
        yValues.add(new Entry(0,(float) 25.1));
        yValues.add(new Entry(0,(float) 13.0));
        yValues.add(new Entry(0,(float) 22.0));
        yValues.add(new Entry(0,(float) 22.9));

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        LineDataSet set1 = new LineDataSet(yValues,"Data set1");

        set1.setFillAlpha(50);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        //Barchart
        barChart = root.findViewById(R.id.TemperatureBarChart);

        //spinner

        spinner = root.findViewById(R.id.spinnerTemperature);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        temperatureList = temperatureViewModel.getTemperatureData();
        Log.i("TEST FOR TEMP ON CEATE STUFF",temperatureList.toString());

        return root;
    }


    private void SetBarchart(int num) {
        barEntries =new ArrayList<>();
        labelsname = new ArrayList<>();

        if(num == 1) {
            //instead of getTime it has to be changed to getHours in the hours case
             //   labelsname.add((temperatureViewModel.getTemperatureData().getValue().get(0).getUpdateTime()));
               // barEntries.add(new BarEntry(0,(float)temperatureViewModel.getTemperatureData().getValue().get(0).getTemperature()));

            for (int i = 0; i < temperatureList.size(); i++) {
                labelsname.add(temperatureList.get(i).getUpdateTime());
                barEntries.add(new BarEntry(0, (int) temperatureList.get(i).getTemperature()));
            }
        } else {
            for (int i = 0; i < temperatureList.size(); i++) {
                String day = "Today";
                double co2 =  temperatureList.get(i).getTemperature();

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


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
