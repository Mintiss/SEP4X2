package sep4x2.android.ui.noise;

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
import sep4x2.android.SharedSensors.Noise;


public class NoiseFragment extends Fragment {

    private LineChart lineChart;
    private BarChart barChart;
    private Switch aSwitch;
    private Spinner spinnerweek;
    private Spinner spinnerchange;
    private TextView message;
    private TextView recNoise;

    //Spinner

    private static final String[] paths = {"Week 22", "Week 23", "Week 24", "Week 25", "Week 26", "Week 27"};
    private static final String[] changepath = {"Today", "Week"};

    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    //LineChart
    ArrayList<Entry> hourEnum = new ArrayList<>();

    ArrayList<Entry> dayEnum = new ArrayList<>();

    //Barchart
    ArrayList<NoiseTemporaryValues> NoiseHourArray = new ArrayList<>();
    ArrayList<NoiseTemporaryValues> NoiseDayArrayList = new ArrayList<>();

    //FROM DB
    List<Noise> noise;
    List<Double> weeklyNoise;

    private NoiseViewModel noiseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noiseViewModel =
                ViewModelProviders.of(this).get(NoiseViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_noise, container, false);
        final TextView textView = root.findViewById(R.id.text_send);


        //Bad solution

        weeklyNoise = noiseViewModel.getWeeklyData(18).getWeeklyNoise();

        //LineChart-------------------------------------------------------------------------------------------------------------------------------------------------
        lineChart = root.findViewById(R.id.LineChartNoise);

        FillHourEbumy();
        fillDayEnum();

        Collections.sort(hourEnum, new EntryXComparator());
        Collections.sort(dayEnum, new EntryXComparator());

        setLinechart(0);


        //Barchart------------------------------------------------------------------------------------------------------------------------------------------------------------
        barChart = root.findViewById(R.id.barchartNoise);

        fillHoursAndNoise();


        SetBarchart(0);

        barChart.animate().alpha(0).setDuration(0);

        //TextView------------------------------------------------------------------

        message = root.findViewById(R.id.text_noise);

        double lastNoise = noise.get(noise.size()-1).getNoise();

        if(lastNoise < 75)
        {
            message.setText("The noise level considered average. Good job.");
        }else if(85<lastNoise)
        {
            message.setText("The noise level considered harmful. Solution: use earplugs/ stop the music.");
        }else {
                message.setText("Your place is a little bit louder than average.");
        }

        recNoise = root.findViewById(R.id.textrecNoise);

        recNoise.setText("The recommended noise is between 75 and 30 dBA.");




        //Switch----------------------------------------------------------------------------------------------------------
        aSwitch = root.findViewById(R.id.switchNoise);


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



        //Spinnerchange------------------------------------------------------------------------------------------------------
        spinnerchange = root.findViewById(R.id.spinnernoisechange);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, changepath);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerchange.setAdapter(adapter3);

        spinnerchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedClass3 = parent.getItemAtPosition(position).toString();
                switch (selectedClass3) {
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

        //Spinner Week--------------------------------------------------------------------------------------------------------------

        spinnerweek = root.findViewById(R.id.spinnernoiseweek);

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
                       weeklyNoise = noiseViewModel.getWeeklyData(noiseViewModel.getWeeklyData(18).getWeekNo()).getWeeklyNoise();

                        Log.i("SENSOR DATA", "" + weeklyNoise.toString());
                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);


                        break;

                    case "Week" :
                        dayEnum.clear();
                       weeklyNoise = noiseViewModel.getWeeklyData(date.getWeekOfWeekyear()-1).getWeeklyNoise();


                        fillWithNewData();
                        fillDayEnum();

                        SetBarchart(0);
                        setLinechart(0);
                        break;

                    case "Week 24":
                        dayEnum.clear();
                       weeklyNoise = noiseViewModel.getWeeklyData(date.getWeekOfWeekyear()-2).getWeeklyNoise();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 25":
                        dayEnum.clear();
                      weeklyNoise = noiseViewModel.getWeeklyData(date.getWeekOfWeekyear()-3).getWeeklyNoise();


                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);

                        SetBarchart(0);
                        break;

                    case "Week 26":
                        dayEnum.clear();
                     weeklyNoise = noiseViewModel.getWeeklyData(date.getWeekOfWeekyear()-4).getWeeklyNoise();

                        fillWithNewData();
                        fillDayEnum();

                        setLinechart(0);
                        SetBarchart(0);
                        break;
                    case "Week 27":
                       weeklyNoise = noiseViewModel.getWeeklyData(date.getWeekOfWeekyear()-5).getWeeklyNoise();


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

        set1.setColor(Color.parseColor("#008577"));
        set1.setCircleColor(Color.BLACK);
        set1.setCircleColorHole(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setDrawValues(false);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        Description description = new Description();
        description.setText("dBA");
        lineChart.setDescription(description);

        YAxis y = lineChart.getAxisLeft();
        y.setLabelCount(5);
        y.setAxisMinValue(0);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

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
            for (int i = 0; i <noise.size() ; i++) {
                barEntries.add(new BarEntry(i, (float) noise.get(i).getNoise()));
                labelsname.add(String.valueOf(noise.get(i).getTime().getHourOfDay()));
            }

        } else {
            for (int i = 0; i < NoiseDayArrayList.size(); i++) {
                String day = NoiseDayArrayList.get(i).getTime();
                double co2 = NoiseDayArrayList.get(i).getNoise();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Noise in decibels");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("dBA");
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


    private void fillHoursAndNoise() {
        NoiseHourArray.clear();

        noise = noiseViewModel.getNoiseData();

        for (int i = 0; i <noise.size() ; i++) {

            NoiseHourArray.add(new NoiseTemporaryValues(String.valueOf(noise.get(i).getTime().getHourOfDay()), noise.get(i).getNoise()));

        }





    }

    private void fillWithNewData() {
        NoiseDayArrayList.clear();
        NoiseDayArrayList.add(new NoiseTemporaryValues("monday", weeklyNoise.get(0)));
        NoiseDayArrayList.add(new NoiseTemporaryValues("wednesday", weeklyNoise.get(1)));
        NoiseDayArrayList.add(new NoiseTemporaryValues("thursday", weeklyNoise.get(2)));
        NoiseDayArrayList.add(new NoiseTemporaryValues("friday", weeklyNoise.get(3)));
        NoiseDayArrayList.add(new NoiseTemporaryValues("saturday", weeklyNoise.get(4)));
        NoiseDayArrayList.add(new NoiseTemporaryValues("sunday", weeklyNoise.get(5)));
    }




    private void FillHourEbumy() {

    hourEnum.clear();
    noise = noiseViewModel.getNoiseData();

        for (int i = 0; i <noise.size() ; i++) {
            hourEnum.add(new Entry(i, (float)noise.get(i).getNoise()));
        }


    }

    private void fillDayEnum() {
        dayEnum.clear();

        double x = weeklyNoise.get(0);
        float monday = (float) x;
        double a =weeklyNoise.get(1);
        float tuesday = (float) a;
        double t = weeklyNoise.get(2);
        float wednesday = (float) t;
        double d = weeklyNoise.get(3);
        float thursday = (float) d;
        double k = weeklyNoise.get(4);
        float friday = (float) k;
        double z = weeklyNoise.get(5);
        float saturday = (float) z;
        double l = weeklyNoise.get(6);
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