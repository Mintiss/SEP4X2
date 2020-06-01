package sep4x2.android.ui.co2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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
import sep4x2.android.SharedSensors.CO2;


public class Co2Fragment extends Fragment  {


    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;


    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //DB
    private List<CO2> co2;
    private List<Double> weeklyCo2;

    private Co2ViewModel co2ViewModel;


    //LineChart
    ArrayList<Entry> yValues = new ArrayList<>();

    ArrayList<Entry> yNumber = new ArrayList<>();

    //Barchart
    ArrayList<CO2TemporaryValues> co2ModelArrayList = new ArrayList<>();
    ArrayList<CO2TemporaryValues> co2ModelArrayList2 = new ArrayList<>();



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

        weeklyCo2 =co2ViewModel.getWeeklyData(18).getWeeklyCO2();

//LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineChartco2);

        FillHourEbumy();
        fillDayEnum();

        Collections.sort(yValues, new EntryXComparator());
        Collections.sort(yNumber, new EntryXComparator());

        setLinechart(0);



        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barchartco2);

        fillHoursAndCo2();


        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();

        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);


        //DB

        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switchco2);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    barChart.animate().alpha(1).setDuration(200);
                    lineChart.animate().alpha(0).setDuration(200);

                }else{

                

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

        spinnerweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass = parent.getItemAtPosition(position).toString();
                switch (selectedClass) {
                    case "Week 22":
                        // assigning div item list defined in XML to the div Spinner
                        yNumber.clear();
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
                        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week 23":
                        yNumber.clear();
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();

                        fillWithNewData();
                        fillDayEnum();

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 24":
                        yNumber.clear();
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
                        Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 25":
                        yNumber.clear();
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
                        Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 26":
                        yNumber.clear();
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
                        Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);
                        break;
                    case "Week 27":
                        weeklyCo2=co2ViewModel.getWeeklyData(18).getWeeklyCO2();
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
            barEntries.add(new BarEntry(0, (float)co2.get(0).getCo2()));
            labelsname.add(String.valueOf(co2.get(0).getTime().getHourOfDay()));
        }
        else {
            for (int i = 0; i < co2ModelArrayList2.size(); i++) {
                String day = co2ModelArrayList2.get(i).getTime();
                double co2 = co2ModelArrayList2.get(i).getCo2();


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

    private void fillHoursAndCo2() {
        co2ModelArrayList.clear();

        co2 = co2ViewModel.getCo2Data();

        for (int i = 0; i <co2.size() ; i++) {
            co2ModelArrayList.add(new CO2TemporaryValues(String.valueOf(co2.get(i).getTime().getHourOfDay()), co2.get(i).getCo2()));
        }




    }

    private void fillWithNewData() {
        co2ModelArrayList2.clear();
        co2ModelArrayList2.add(new CO2TemporaryValues("monday", weeklyCo2.get(0)));
        co2ModelArrayList2.add(new CO2TemporaryValues("tuesday", weeklyCo2.get(1)));
        co2ModelArrayList2.add(new CO2TemporaryValues("wednesday", weeklyCo2.get(2)));
        co2ModelArrayList2.add(new CO2TemporaryValues("thursday", weeklyCo2.get(3)));
        co2ModelArrayList2.add(new CO2TemporaryValues("friday", weeklyCo2.get(4)));
        co2ModelArrayList2.add(new CO2TemporaryValues("saturday", weeklyCo2.get(5)));
        co2ModelArrayList2.add(new CO2TemporaryValues("sunday", weeklyCo2.get(6)));
    }



    private void FillHourEbumy() {

        yValues.clear();



        co2 = co2ViewModel.getCo2Data();

        for (int i = 0; i <co2.size() ; i++) {

            yValues.add(new Entry(i, (float)co2.get(i).getCo2()));

        }




    }

    private void fillDayEnum() {

        yNumber.clear();

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

        yNumber.add(new Entry(0, monday));
        yNumber.add(new Entry(1, tuesday));
        yNumber.add(new Entry(2, wednesday));
        yNumber.add(new Entry(3, thursday));
        yNumber.add(new Entry(4, friday));
        yNumber.add(new Entry(5, saturday));
        yNumber.add(new Entry(6, sunday));
    }





}