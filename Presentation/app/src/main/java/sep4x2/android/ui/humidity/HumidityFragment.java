package sep4x2.android.ui.humidity;

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
    private static final String[] paths = {"Week 22", "Week 23", "Week 24", "Week 25", "Week 26", "Week 27"};

    private Spinner spinnerchange;
    private static final String[] changepath = {"Today", "Week"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        humidityViewModel =
                ViewModelProviders.of(this).get(HumidityViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_humidity, container, false);

        //Bad solution

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

        //Database


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
                    case "Week 22":
                        // assigning div item list defined in XML to the div Spinner
                        dayEnum.clear();


                        weeklyHumidity = humidityViewModel.getWeeklyData(18).getWeeklyHumidity();

                        Log.i("SENSOR DATA", "" + weeklyHumidity.toString());

                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week 23":
                        dayEnum.clear();

                        weeklyHumidity = humidityViewModel.getWeeklyData(date.getWeekOfWeekyear() - 1).getWeeklyHumidity();

                        fillWithHumiditydays();
                        fillDayEnum();

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 24":
                        dayEnum.clear();
                        weeklyHumidity = humidityViewModel.getWeeklyData(date.getWeekOfWeekyear() - 2).getWeeklyHumidity();

                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 25":
                        dayEnum.clear();
                        weeklyHumidity = humidityViewModel.getWeeklyData(date.getWeekOfWeekyear() - 3).getWeeklyHumidity();


                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 26":
                        dayEnum.clear();

                        weeklyHumidity = humidityViewModel.getWeeklyData(date.getWeekOfWeekyear() - 4).getWeeklyHumidity();
                        fillWithHumiditydays();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);
                        break;
                    case "Week 27":

                        weeklyHumidity = humidityViewModel.getWeeklyData(date.getWeekOfWeekyear() - 5).getWeeklyHumidity();
                        fillWithHumiditydays();
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
            set1 = new LineDataSet(hourEnum, "Data hours");


        } else {
            set1 = new LineDataSet(dayEnum, "Data day");

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


        if (num == 1) {
            barEntries.add(new BarEntry(0, (float) humidity.get(0).getHumidity()));
            labelsname.add(String.valueOf(humidity.get(0).getTime().getHourOfDay()));
        } else {
            for (int i = 0; i < humidityDayArray.size(); i++) {
                String day = humidityDayArray.get(i).getTime();
                double humidity = humidityDayArray.get(i).getHumidity();

                barEntries.add(new BarEntry(i, (float) humidity));
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
        humidityHourArray.clear();

        humidity = humidityViewModel.getHumidityData();

        for (int i = 0; i < humidity.size(); i++) {
            humidityHourArray.add(new HumidityTemporaryValues(String.valueOf(humidity.get(i).getTime().getHourOfDay()), humidity.get(i).getHumidity()));
        }


    }

    private void fillWithHumiditydays() {
        humidityDayArray.clear();
        humidityDayArray.add(new HumidityTemporaryValues("monday", weeklyHumidity.get(0)));
        humidityDayArray.add(new HumidityTemporaryValues("tuesday", weeklyHumidity.get(1)));
        humidityDayArray.add(new HumidityTemporaryValues("wednesday", weeklyHumidity.get(2)));
        humidityDayArray.add(new HumidityTemporaryValues("thursday", weeklyHumidity.get(3)));
        humidityDayArray.add(new HumidityTemporaryValues("friday", weeklyHumidity.get(4)));
        humidityDayArray.add(new HumidityTemporaryValues("saturday", weeklyHumidity.get(5)));
        humidityDayArray.add(new HumidityTemporaryValues("sunday", weeklyHumidity.get(6)));
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