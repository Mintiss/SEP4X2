package sep4x2.android.ui.temperature;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sep4x2.android.R;
import sep4x2.android.SharedSensors.Temperature;
import sep4x2.android.SharedSensors.WeeklyConverter;


public class TemperatureFragment extends Fragment {

    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;
    private TextView message;
    private  TextView recTemp;
   // private SensorData sensorData;

    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //LineChart
    ArrayList<Entry> hourEnum = new ArrayList<>();

    ArrayList<Entry> dayEnum = new ArrayList<>(7);

    //Barchart
    ArrayList<TemperatureTemporaryValues> temperatureHourArrayList = new ArrayList<>();
    ArrayList<TemperatureTemporaryValues> temperatureDayArrayList = new ArrayList<>();

    //FROM DB
    List<Temperature> temperatures;


    private List<Double> weeklyTemp;

    private TemperatureViewModel temperatureViewModel;

    //Spinner
    private Spinner spinnerweek;
    private static final String[] paths = {"Week 18", "Week 19", "Week 20", "Week 21", "Week 22"};


    private Spinner spinnerchange;
    private static final String[] changepath = {"Today", "Week"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        temperatureViewModel =
                ViewModelProviders.of(this).get(TemperatureViewModel.class);


        final View root = inflater.inflate(R.layout.fragment_temperature, container, false);

        final TextView textView = root.findViewById(R.id.text_temp);


        /*//UPDATES THE DB WITH LAST FEW WEEKS (using hardcoded data cos db)
        temperatureViewModel.updateWeeklyData(18);
        temperatureViewModel.updateWeeklyData(19);
        temperatureViewModel.updateWeeklyData(20);
        temperatureViewModel.updateWeeklyData(21);
        temperatureViewModel.updateWeeklyData(22);*/


        // solution
        weeklyTemp = temperatureViewModel.getWeeklyData(18).getWeeklyTemperature();



        //LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineCharttemp);

        FillHourEbumy();
        fillDayEnum();


        Collections.sort(hourEnum, new EntryXComparator());
        Collections.sort(dayEnum, new EntryXComparator());


        setLinechart(0);


        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barcharttemp);

        fillHoursAndTemperatures();


        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);

        //DB--------------------------------------------------------------------------------------------------------------




        //Textview---------------------------------------------------------------------------------------------------------------

        message = root.findViewById(R.id.text_temp);

        double lastTemp;


        temperatures = temperatureViewModel.getTemperatureData();


          lastTemp =   temperatures.get(temperatures.size()-1).getTemperature();

        if(lastTemp > 24)
        {
            message.setText("Temperature may cause cardiovascular risk. Solution: Please lower the temperature.");
        }else if(lastTemp <16 )
        {
            message.setText("Temperature may cause respiratory and cardiovascular risks. Solution: Turn on the heating.");
        }else {
            message.setText("The temperature is ideal. Keep up the good work!");
        }


        recTemp = root.findViewById(R.id.textRecomTemp);

        recTemp.setText("The recommended temperature is between 16 and 24 Celsius.");









        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switchTemp);


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


        //Spinner------------------------------------------------------------------------------------------------------------------


        spinnerchange = root.findViewById(R.id.spinnertempchange);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, changepath);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerchange.setAdapter(adapter2);

        spinnerchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass2 = parent.getItemAtPosition(position).toString();
                switch (selectedClass2) {
                    case "Today":
                        SetBarchart(1);
                        setLinechart(1);
                        spinnerweek.animate().alpha(0).setDuration(0);
                        spinnerweek.setEnabled(false);


                        break;
                    case "Week":
                        spinnerweek.animate().alpha(1).setDuration(0);
                        spinnerweek.setEnabled(true);

                        //Current weeks of bar chart
                        SetBarchart(0);
                        setLinechart(0);

                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Spinner weekly-----------------------------------------------------------------------------------------------------------------

        spinnerweek = root.findViewById(R.id.spinnertempweek);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerweek.setAdapter(adapter);

        spinnerweek.animate().alpha(0).setDuration(0);

        DateTime date = new DateTime();

        spinnerweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass = parent.getItemAtPosition(position).toString();
                switch (selectedClass) {
                    case "Week 18":
                        // assigning div item list defined in XML to the div Spinner
                        dayEnum.clear();

                        weeklyTemp = temperatureViewModel.getWeeklyData(18).getWeeklyTemperature();

                        FillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week 19":
                        dayEnum.clear();
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();

                        weeklyTemp=temperatureViewModel.getWeeklyData(19).getWeeklyTemperature();

                        FillWithNewData();
                        fillDayEnum();

                        Log.i("SENSOR DATA WEEK", "" + weeklyTemp.toString());

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 20":
                        dayEnum.clear();
                        Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();

                        weeklyTemp = temperatureViewModel.getWeeklyData(20).getWeeklyTemperature();

                        FillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 21":
                        dayEnum.clear();
                        Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                        weeklyTemp = temperatureViewModel.getWeeklyData(21).getWeeklyTemperature();

                        FillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 22":
                        dayEnum.clear();
                        Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();

                        weeklyTemp = temperatureViewModel.getWeeklyData(22).getWeeklyTemperature();

                        FillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return root;
    }


    private void setLinechart(int number) {


        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        LineDataSet set1;





        if (number == 1) {
            set1 = new LineDataSet(hourEnum, "Temperature");


        } else {
            set1 = new LineDataSet(dayEnum, "Temperature");

        }
        set1.setFillAlpha(250);




        set1.setColor(Color.parseColor("#008577"));
        set1.setCircleColor(Color.BLACK);
        set1.setCircleColorHole(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        Description description = new Description();
        description.setText("x-axis=Hours, y-axis=°C");
        lineChart.setDescription(description);

        YAxis y = lineChart.getAxisLeft();
        y.setLabelCount(5);
        y.setAxisMinValue(-25);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsname));


        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setNoDataText("No data");

       lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDrawBorders(true);







        lineChart.invalidate();


    }

    private void SetBarchart(int num) {
        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();


        if (num == 1) {
            for (int i = 0; i < temperatures.size(); i++) {
                barEntries.add(new BarEntry(i, (float) temperatures.get(i).getTemperature()));
                labelsname.add(String.valueOf(temperatures.get(i).getTime().getHourOfDay()));
            }

        } else {
            for (int i = 0; i < temperatureDayArrayList.size(); i++) {
                String day = temperatureDayArrayList.get(i).getTime();
                double co2 = temperatureDayArrayList.get(i).getTemperature();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Temperature");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("x-axis=Hours, y-axis=°C");
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


        barChart.getAxisRight().setDrawLabels(false);
        barChart.setDrawBorders(true);

        barChart.invalidate();
    }


    private void fillHoursAndTemperatures() {
        temperatureHourArrayList.clear();

        temperatures = temperatureViewModel.getTemperatureData();

        for (int i = 0; i < temperatures.size(); i++) {
            temperatureHourArrayList.add(new TemperatureTemporaryValues(String.valueOf(temperatures.get(i).getTime().getHourOfDay()), temperatures.get(i).getTemperature()));

        }

    }

    private void FillWithNewData() {
        temperatureDayArrayList.clear();
        temperatureDayArrayList.add(new TemperatureTemporaryValues("mon", weeklyTemp.get(0)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("tue", weeklyTemp.get(1)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("wed", weeklyTemp.get(2)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("thu", weeklyTemp.get(3)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("fri", weeklyTemp.get(4)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("sat", weeklyTemp.get(5)));
        temperatureDayArrayList.add(new TemperatureTemporaryValues("sun", weeklyTemp.get(6)));
    }


    private void FillHourEbumy() {

        hourEnum.clear();


        temperatures = temperatureViewModel.getTemperatureData();

        for (int i = 0; i < temperatures.size(); i++) {


            hourEnum.add(new Entry(i, (float) temperatures.get(i).getTemperature()));


        }


    }

    private void fillDayEnum() {

        dayEnum.clear();

        double x = weeklyTemp.get(0);
        float monday = (float) x;
        double a = weeklyTemp.get(1);
        float tuesday = (float) a;
        double t = weeklyTemp.get(2);
        float wednesday = (float) t;
        double d = weeklyTemp.get(3);
        float thursday = (float) d;
        double k = weeklyTemp.get(4);
        float friday = (float) k;
        double z = weeklyTemp.get(5);
        float saturday = (float) z;
        double l = weeklyTemp.get(6);
        float sunday = (float) l;

        dayEnum.add(new Entry(0, monday));
        dayEnum.add(new Entry(1, tuesday));
        dayEnum.add(new Entry(2, wednesday));
        dayEnum.add(new Entry(3, thursday));
        dayEnum.add(new Entry(4, friday));
        dayEnum.add(new Entry(5, saturday));
        dayEnum.add(new Entry(6, sunday));
    }


}

