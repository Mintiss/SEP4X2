package sep4x2.android.ui.noise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sep4x2.android.R;
import sep4x2.android.SharedSensors.Noise;
import sep4x2.android.ui.humidity.HumidityModel;


public class NoiseFragment extends Fragment {

    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;
    private RadioGroup radioGroup;

    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //LineChart
    ArrayList<Entry> yValues = new ArrayList<>();

    ArrayList<Entry> yNumber = new ArrayList<>();

    //Barchart
    ArrayList<HumidityModel> humidityModelArrayList = new ArrayList<>();
    ArrayList<HumidityModel> humidityModelArrayList2 = new ArrayList<>();

    //FROM DB
    List<Noise> noiseData;

    private NoiseViewModel noiseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noiseViewModel =
                ViewModelProviders.of(this).get(NoiseViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_noise, container, false);
        final TextView textView = root.findViewById(R.id.text_send);

        //LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineChart);

        FillHourEbumy();
        fillDayEnum();

        Collections.sort(yValues, new EntryXComparator());
        Collections.sort(yNumber, new EntryXComparator());

        setLinechart(0);


        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barchart);

        fillHoursAndHumidityvaluess();
        fillDaysAndHumidityvaluess2();

        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);

        //DB--------------------------------------------------------------------------------------------------------------
        noiseData = noiseViewModel.getNoiseData();

        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switch2);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    barChart.animate().alpha(1).setDuration(200);
                    lineChart.animate().alpha(0).setDuration(200);

                } else {

                    barChart.animate().alpha(0).setDuration(200);
                    lineChart.animate().alpha(1).setDuration(200);
                }
            }
        });


        //RadioGroup-----------------------------------------------------------------------------------------------------------------

        radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.dayradioButton:
                        Toast.makeText(getContext(), "day", Toast.LENGTH_SHORT).show();
                        SetBarchart(0);
                        setLinechart(0);
                        break;
                    case R.id.hourradioButton:
                        Toast.makeText(getContext(), "hour", Toast.LENGTH_SHORT).show();
                        setLinechart(1);
                        SetBarchart(1);
                        break;
                }
            }
        });


        return root;

    }

    private void setLinechart(int number) {




        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        LineDataSet set1;
        LineDataSet set2;

        if (number == 1) {
            set1 = new LineDataSet(yValues, "Data hours");


        } else {
            set1 = new LineDataSet(yNumber, "Data day");

        }
        set1.setFillAlpha(250);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        lineChart.invalidate();


    }

    private void SetBarchart(int num) {
        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();


        if (num == 1){
                barEntries.add(new BarEntry(0, (float) noiseData.get(0).getNoise()));
                labelsname.add(String.valueOf(noiseData.get(0).getTime().getHourOfDay()));
            }
         else {
            for (int i = 0; i < humidityModelArrayList2.size(); i++) {
                String day = humidityModelArrayList2.get(i).getTime();
                double co2 = humidityModelArrayList2.get(i).getHumindity();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Humidity in percentage");
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


    private void fillHoursAndHumidityvaluess() {
        humidityModelArrayList.clear();
        humidityModelArrayList.add(new HumidityModel("1pm", 65.5));
        humidityModelArrayList.add(new HumidityModel("9am", 12.0));
        humidityModelArrayList.add(new HumidityModel("10am", 34.5));
        humidityModelArrayList.add(new HumidityModel("11am", 45.0));
        humidityModelArrayList.add(new HumidityModel("1pm", 23.5));
        humidityModelArrayList.add(new HumidityModel("1pm", 65.5));
        humidityModelArrayList.add(new HumidityModel("9am", 12.0));
        humidityModelArrayList.add(new HumidityModel("10am", 34.5));
        humidityModelArrayList.add(new HumidityModel("11am", 45.0));
        humidityModelArrayList.add(new HumidityModel("1pm", 23.5));
        humidityModelArrayList.add(new HumidityModel("1pm", 65.5));
        humidityModelArrayList.add(new HumidityModel("9am", 12.0));
        humidityModelArrayList.add(new HumidityModel("10am", 34.5));
        humidityModelArrayList.add(new HumidityModel("11am", 45.0));
        humidityModelArrayList.add(new HumidityModel("1pm", 23.5));
        humidityModelArrayList.add(new HumidityModel("1pm", 65.5));
        humidityModelArrayList.add(new HumidityModel("9am", 12.0));
        humidityModelArrayList.add(new HumidityModel("10am", 34.5));
        humidityModelArrayList.add(new HumidityModel("11am", 45.0));
        humidityModelArrayList.add(new HumidityModel("1pm", 23.5));
        humidityModelArrayList.add(new HumidityModel("1pm", 65.5));
        humidityModelArrayList.add(new HumidityModel("9am", 12.0));
        humidityModelArrayList.add(new HumidityModel("10am", 34.5));
        humidityModelArrayList.add(new HumidityModel("11am", 45.0));


    }

    private void fillDaysAndHumidityvaluess2() {
        humidityModelArrayList2.clear();
        humidityModelArrayList2.add(new HumidityModel("monday", 16.5));
        humidityModelArrayList2.add(new HumidityModel("tuesday", 25.0));
        humidityModelArrayList2.add(new HumidityModel("wednesday", 34.5));
        humidityModelArrayList2.add(new HumidityModel("thursday", 6.0));
        humidityModelArrayList2.add(new HumidityModel("friday", 34));
        humidityModelArrayList2.add(new HumidityModel("saturday", 12));
        humidityModelArrayList2.add(new HumidityModel("sunday", 12.0));
    }

    private void FillHourEbumy() {
        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 20f));
        yValues.add(new Entry(2, 75f));
        yValues.add(new Entry(3, 12f));
        yValues.add(new Entry(4, 22.9f));
        yValues.add(new Entry(5, 60f));
        yValues.add(new Entry(6, 20f));
        yValues.add(new Entry(7, 75f));
        yValues.add(new Entry(8, 12f));
        yValues.add(new Entry(9, 22.9f));
        yValues.add(new Entry(10, 60f));
        yValues.add(new Entry(11, 20f));
        yValues.add(new Entry(12, 75f));
        yValues.add(new Entry(13, 12f));
        yValues.add(new Entry(14, 22.9f));
        yValues.add(new Entry(15, 60f));
        yValues.add(new Entry(16, 20f));
        yValues.add(new Entry(17, 75f));
        yValues.add(new Entry(18, 12f));
        yValues.add(new Entry(19, 22.9f));
        yValues.add(new Entry(20, 20f));
        yValues.add(new Entry(21, 75f));
        yValues.add(new Entry(22, 12f));
        yValues.add(new Entry(23, 22.9f));
    }

    private void fillDayEnum() {
        yNumber.add(new Entry(0, 15f));
        yNumber.add(new Entry(1, 35f));
        yNumber.add(new Entry(2, 75f));
        yNumber.add(new Entry(3, 89f));
        yNumber.add(new Entry(4, 15f));
        yNumber.add(new Entry(5, 27f));
        yNumber.add(new Entry(6, 46f));
    }


}