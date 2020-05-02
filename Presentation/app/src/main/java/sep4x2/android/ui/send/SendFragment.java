package sep4x2.android.ui.send;

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



public class SendFragment extends Fragment implements  AdapterView.OnItemSelectedListener
{

    //Barchart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;
    ArrayList<NoiseModel> NoiseModelArrayList = new ArrayList<>();
    ArrayList<NoiseModel> TimeArrayList = new ArrayList<>();
    //For the drop down
    private Spinner spinner;
    private static final String[] paths = {"Daily","Weekly"};
    public String string;
    public int nr;

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        final AdapterView.OnItemSelectedListener listener = this;
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Barchart
        barChart = root.findViewById(R.id.NoiseBarChart);

        //spinner

        spinner = root.findViewById(R.id.spinnerNoise);
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
            for (int i = 0; i < NoiseModelArrayList.size(); i++) {
                String hour = NoiseModelArrayList.get(i).getTime();
                double co2 = NoiseModelArrayList.get(i).getNoise();

                barEntries.add(new BarEntry(i, (float)co2));
                labelsname.add(hour);
            }
        } else {
            for (int i = 0; i < TimeArrayList.size(); i++) {
                String day = TimeArrayList.get(i).getTime();
                double co2 = TimeArrayList.get(i).getNoise();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Noise in dBs");
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
        NoiseModelArrayList.clear();
        NoiseModelArrayList.add(new NoiseModel(13.5,"1pm"));
        NoiseModelArrayList.add(new NoiseModel(12.0,"9am"));
        NoiseModelArrayList.add(new NoiseModel(44.5,"10am"));
        NoiseModelArrayList.add(new NoiseModel(45.0,"11am"));
        NoiseModelArrayList.add(new NoiseModel(76.5,"1pm"));
    }

    private void fillDaysAndHumidityvaluess2()
    {
        TimeArrayList.clear();
        TimeArrayList.add(new NoiseModel(5.5,"Monday"));
        TimeArrayList.add(new NoiseModel(4.1,"Tuesday"));
        TimeArrayList.add(new NoiseModel(5.0,"Wednesday"));
        TimeArrayList.add(new NoiseModel(56.0,"Thursday"));
        TimeArrayList.add(new NoiseModel(3.9,"Friday"));
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

    }
}