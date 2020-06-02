package sep4x2.android.ui.co2;

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
import sep4x2.android.SharedSensors.CO2;


public class Co2Fragment extends Fragment {


    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;
    private TextView message;
    private TextView recCo2;


    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //DB
    private List<CO2> co2;
    private List<Double> weeklyCo2;

    private Co2ViewModel co2ViewModel;


    //LineChart
    ArrayList<Entry> hourEnum = new ArrayList<>();

    ArrayList<Entry> dayEnum = new ArrayList<>();

    //Barchart
    ArrayList<CO2TemporaryValues> co2HourArray = new ArrayList<>();
    ArrayList<CO2TemporaryValues> co2DayArray = new ArrayList<>();


    //Spinner
    private Spinner spinnerweek;
    private static final String[] paths = {"Week 22", "Week 23", "Week 24", "Week 25", "Week 26", "Week 27"};

    private Spinner spinnerchange;
    private static final String[] changepath = {"Today", "Week"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        co2ViewModel =
                ViewModelProviders.of(this).get(Co2ViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_co2, container, false);

        //Bad solution

        weeklyCo2 = co2ViewModel.getWeeklyData(18).getWeeklyCO2();
        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();

         //LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineChartco2);

        FillHourEbumy();
        fillDayEnum();

        Collections.sort(hourEnum, new EntryXComparator());
        Collections.sort(dayEnum, new EntryXComparator());

        setLinechart(0);


        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barchartco2);

        fillHoursAndCo2();


        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();

        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);


        //TextView----------------------------------------------------------------------------------------------------

        message = root.findViewById(R.id.text_co2);

       double lastCo2 =  co2.get(co2.size()-1).getCo2();

       if(lastCo2 < 1000)
       {
           message.setText("Co2 level considered ideal. Good job!");
       }else if(lastCo2>1000)
       {
           message.setText("Co2 level considered harmful. May cause headaches and sleepiness. Solution: Open the window.");
       }else if(lastCo2 >= 40000)
       {
        message.setText("Co2 level is extremely harmful. You most likely dead. :( ");
       }

       recCo2 = root.findViewById(R.id.textrecCo2);

       recCo2.setText("The recommended CO2 is between 400 and 1000ppm.");





        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switchco2);


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


        // Spinner for changing between chars based on time
        spinnerchange = root.findViewById(R.id.spinnerco2change);

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

        //Spinner based on the week days and to choose from them

        spinnerweek = root.findViewById(R.id.spinnerco2week);

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
                    case "Week 22":
                        // assigning div item list defined in XML to the div Spinner
                        dayEnum.clear();
                        weeklyCo2 = co2ViewModel.getWeeklyData(18).getWeeklyCO2();

                        Log.i("SENSOR DATA", "" + weeklyCo2.toString());
                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week 23":
                        dayEnum.clear();
                           weeklyCo2 = co2ViewModel.getWeeklyData(19).getWeeklyCO2();
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();

                        fillWithNewData();
                        fillDayEnum();

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 24":
                        dayEnum.clear();
                        //    weeklyCo2 = co2ViewModel.getWeeklyData(date.getWeekOfWeekyear()-2).getWeeklyCO2();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 25":
                        dayEnum.clear();
                        //     weeklyCo2 = co2ViewModel.getWeeklyData(date.getWeekOfWeekyear()-3).getWeeklyCO2();


                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 26":
                        dayEnum.clear();
                        //     weeklyCo2 = co2ViewModel.getWeeklyData(date.getWeekOfWeekyear()-4).getWeeklyCO2();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);
                        break;
                    case "Week 27":
                        //       weeklyCo2 = co2ViewModel.getWeeklyData(-5).getWeeklyCO2();
                        Toast.makeText(getContext(), "6, with no data", Toast.LENGTH_SHORT).show();

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
            set1 = new LineDataSet(hourEnum, "Data hours");


        } else {

            set1 = new LineDataSet(dayEnum, "Data day");

        }
        set1.setFillAlpha(250);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        set1.setColor(Color.parseColor("#008577"));
        set1.setCircleColor(Color.BLACK);
        set1.setCircleColorHole(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setDrawValues(false);


        Description description = new Description();
        description.setText("ppm");
        lineChart.setDescription(description);

        YAxis y = lineChart.getAxisLeft();
        y.setLabelCount(5);
        y.setAxisMinValue(0);

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDrawBorders(true);

        lineChart.invalidate();


    }

    private void SetBarchart(int num) {

        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();

        if (num == 1) {
            for (int i = 0; i < co2.size(); i++) {
                barEntries.add(new BarEntry(i, (float) co2.get(i).getCo2()));
                labelsname.add(String.valueOf(co2.get(i).getTime().getHourOfDay()));
            }

        } else {
            for (int i = 0; i < co2DayArray.size(); i++) {
                String day = co2DayArray.get(i).getTime();
                double co2 = co2DayArray.get(i).getCo2();


                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "CO2 in ppm(parts in million)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("ppm");
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

    private void fillHoursAndCo2() {
        co2HourArray.clear();

        co2 = co2ViewModel.getCo2Data();

        for (int i = 0; i < co2.size(); i++) {
            co2HourArray.add(new CO2TemporaryValues(String.valueOf(co2.get(i).getTime().getHourOfDay()), co2.get(i).getCo2()));
        }


    }

    private void fillWithNewData() {
        co2DayArray.clear();
        co2DayArray.add(new CO2TemporaryValues("monday", weeklyCo2.get(0)));
        co2DayArray.add(new CO2TemporaryValues("tuesday", weeklyCo2.get(1)));
        co2DayArray.add(new CO2TemporaryValues("wednesday", weeklyCo2.get(2)));
        co2DayArray.add(new CO2TemporaryValues("thursday", weeklyCo2.get(3)));
        co2DayArray.add(new CO2TemporaryValues("friday", weeklyCo2.get(4)));
        co2DayArray.add(new CO2TemporaryValues("saturday", weeklyCo2.get(5)));
        co2DayArray.add(new CO2TemporaryValues("sunday", weeklyCo2.get(6)));
    }


    private void FillHourEbumy() {

        hourEnum.clear();


        co2 = co2ViewModel.getCo2Data();

        for (int i = 0; i < co2.size(); i++) {

            hourEnum.add(new Entry(i, (float) co2.get(i).getCo2()));

        }


    }

    private void fillDayEnum() {

        dayEnum.clear();

        double x = weeklyCo2.get(0);
        float monday = (float) x;
        double a = weeklyCo2.get(1);
        float tuesday = (float) a;
        double t = weeklyCo2.get(2);
        float wednesday = (float) t;
        double d = weeklyCo2.get(3);
        float thursday = (float) d;
        double k = weeklyCo2.get(4);
        float friday = (float) k;
        double z = weeklyCo2.get(5);
        float saturday = (float) z;
        double l = weeklyCo2.get(6);
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