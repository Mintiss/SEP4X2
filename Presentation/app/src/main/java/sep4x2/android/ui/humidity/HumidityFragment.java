package sep4x2.android.ui.humidity;

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
import sep4x2.android.SharedSensors.Humidity;

public class HumidityFragment extends Fragment {

    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;
    private TextView message;
    private TextView recHum;


    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //LineChart
    ArrayList<Entry> hourEnum = new ArrayList<>();

    ArrayList<Entry> dayEnum = new ArrayList<>();

    //Barchart
    ArrayList<HumidityTemporaryValues> humidityHourArray = new ArrayList<>();
    ArrayList<HumidityTemporaryValues> humidityDayArray = new ArrayList<>();

    //DB

    private List<Double> weeklyHumidity;

    private HumidityViewModel humidityViewModel;

    List<Humidity> humidity;


    //Spinner
    private Spinner spinnerweek;
    private static final String[] paths = {"Week 18", "Week 19", "Week 20", "Week 21", "Week 22"};

    private Spinner spinnerchange;
    private static final String[] changepath = {"Today", "Week"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        humidityViewModel =
                ViewModelProviders.of(this).get(HumidityViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_humidity, container, false);

        //Solution

       weeklyHumidity = humidityViewModel.getWeeklyData(18).getWeeklyHumidity();

//LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineChartHum);

        FillHourEbumy();
        fillDayEnum();

        Collections.sort(hourEnum, new EntryXComparator());
        Collections.sort(dayEnum, new EntryXComparator());

        setLinechart(0);

        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barchartHum);

        fillHoursAndHumidityvaluess();


        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);

        //TextView------------------------------------------------------------------------------------------------------

        message = root.findViewById(R.id.text_hum);

        double lastHum =  humidity.get(humidity.size()-1).getHumidity();



        if(lastHum < 45)
        {
           message.setText("Humidity is considered comfortable, but can be dry as well.");
        }else if(lastHum > 55)
        {
            message.setText("Humidity level considered high. Solution: Open a window, or call for help.");
        }else{
            message.setText("Your houses humidity level is in the recommended spectrum. Good Job!");
        }

        recHum = root.findViewById(R.id.textrecHum);

        recHum.setText("The recommended Humidity is between 45% and 55%.");



        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switchHum);


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


        //DB


        //Spinner Change-------------------------------------------------------------------------------------------------

        spinnerchange = root.findViewById(R.id.spinnerhumchange);

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


        //Spinner Week---------------------------------------------------------------------------
        spinnerweek = root.findViewById(R.id.spinnerhumweek);

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


                        weeklyHumidity = humidityViewModel.getWeeklyData(18).getWeeklyHumidity();

                        Log.i("SENSOR DATA", "" + weeklyHumidity.toString());

                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week 19":
                        dayEnum.clear();

                        weeklyHumidity = humidityViewModel.getWeeklyData(19).getWeeklyHumidity();

                        fillWithHumiditydays();
                        fillDayEnum();

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 20":
                        dayEnum.clear();
                        weeklyHumidity = humidityViewModel.getWeeklyData(20).getWeeklyHumidity();

                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 21":
                        dayEnum.clear();
                        weeklyHumidity = humidityViewModel.getWeeklyData(21).getWeeklyHumidity();


                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 22":
                        dayEnum.clear();

                        weeklyHumidity = humidityViewModel.getWeeklyData(22).getWeeklyHumidity();
                        fillWithHumiditydays();
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
        LineDataSet set2;

        if (number == 1) {
            set1 = new LineDataSet(hourEnum, "Humidity");


        } else {
            set1 = new LineDataSet(dayEnum, "Humidity");

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
        description.setText("x-axis=Hours, y-axis=%");
        lineChart.setDescription(description);


        YAxis y = lineChart.getAxisLeft();
        y.setLabelCount(5);
        y.setAxisMinValue(0);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsname));

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setNoDataText("No data");

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDrawBorders(true);

        lineChart.invalidate();


    }

    private void SetBarchart(int num) {
        barEntries = new ArrayList<>();
        labelsname = new ArrayList<>();


        if (num == 1) {
            for (int i = 0; i <humidity.size() ; i++) {
                barEntries.add(new BarEntry(i, (float) humidity.get(i).getHumidity()));
                labelsname.add(String.valueOf(humidity.get(i).getTime().getHourOfDay()));
            }

        } else {
            for (int i = 0; i < humidityDayArray.size(); i++) {
                String day = humidityDayArray.get(i).getTime();
                double humidity = humidityDayArray.get(i).getHumidity();

                barEntries.add(new BarEntry(i, (float) humidity));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Humidity");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("x-axis=Hours, y-axis=%");
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

    private void fillHoursAndHumidityvaluess() {
        humidityHourArray.clear();

        humidity = humidityViewModel.getHumidityData();

        for (int i = 0; i < humidity.size(); i++) {
            humidityHourArray.add(new HumidityTemporaryValues(String.valueOf(humidity.get(i).getTime().getHourOfDay()), humidity.get(i).getHumidity()));
        }


    }

    private void fillWithHumiditydays() {
        humidityDayArray.clear();
        humidityDayArray.add(new HumidityTemporaryValues("mon", weeklyHumidity.get(0)));
        humidityDayArray.add(new HumidityTemporaryValues("tue", weeklyHumidity.get(1)));
        humidityDayArray.add(new HumidityTemporaryValues("wed", weeklyHumidity.get(2)));
        humidityDayArray.add(new HumidityTemporaryValues("thu", weeklyHumidity.get(3)));
        humidityDayArray.add(new HumidityTemporaryValues("fri", weeklyHumidity.get(4)));
        humidityDayArray.add(new HumidityTemporaryValues("sat", weeklyHumidity.get(5)));
        humidityDayArray.add(new HumidityTemporaryValues("sun", weeklyHumidity.get(6)));
    }


    private void FillHourEbumy() {

        hourEnum.clear();

        humidity = humidityViewModel.getHumidityData();

        int x = 0;

        for (int i = 0; i < humidity.size(); i++) {
            hourEnum.add(new Entry(x, (float) humidity.get(i).getHumidity()));

            x++;
        }


    }

    private void fillDayEnum() {
        dayEnum.clear();

        double x = weeklyHumidity.get(0);
        float monday = (float) x;
        double a = weeklyHumidity.get(1);
        float tuesday = (float) a;
        double t = weeklyHumidity.get(2);
        float wednesday = (float) t;
        double d = weeklyHumidity.get(3);
        float thursday = (float) d;
        double k = weeklyHumidity.get(4);
        float friday = (float) k;
        double z = weeklyHumidity.get(5);
        float saturday = (float) z;
        double l = weeklyHumidity.get(6);
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