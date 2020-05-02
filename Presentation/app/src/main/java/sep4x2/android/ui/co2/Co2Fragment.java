package sep4x2.android.ui.co2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import sep4x2.android.R;

public class Co2Fragment extends Fragment  implements AdapterView.OnItemSelectedListener{


    //For the BarChart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;
    ArrayList<CO2Model> CO2ModelArrayList = new ArrayList<>();
    ArrayList<CO2Model> carbonEmissionPerWeekHoursArrayList = new ArrayList<>();
    //For the drop down
    private Spinner spinner;
    private static final String[] paths = {"Daily","Weekly"};
    public String string;
    public int nr;

    private Co2ViewModel co2ViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        co2ViewModel =
                ViewModelProviders.of(this).get(Co2ViewModel.class);
       final View root = inflater.inflate(R.layout.fragment_co2, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        final AdapterView.OnItemSelectedListener listener = this;
        co2ViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

                //BarChart
                barChart = root.findViewById(R.id.CO2BarChart);

                //DropDown
                spinner = root.findViewById(R.id.spinnerCO2);
                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,paths);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(listener);

        }
        });

        return root;
    }

    private void SetBarchart(int num) {
        barEntries =new ArrayList<>();
        labelsname = new ArrayList<>();

        fillHoursAndCO2valuess();
        fillDaysAndCO2valuess2();

        if(num == 1) {
            for (int i = 0; i < CO2ModelArrayList.size(); i++) {
                String hour = CO2ModelArrayList.get(i).getHours();
                double co2 = CO2ModelArrayList.get(i).getCo2metric();

                barEntries.add(new BarEntry(i, (float)co2));
                labelsname.add(hour);
            }
        } else {
            for (int i = 0; i < carbonEmissionPerWeekHoursArrayList.size(); i++) {
                String day = carbonEmissionPerWeekHoursArrayList.get(i).getHours();
                double co2 = carbonEmissionPerWeekHoursArrayList.get(i).getCo2metric();

                barEntries.add(new BarEntry(i, (float) co2));
                labelsname.add(day);
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Daily CO2 in ppm");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Hours");
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

    private void fillHoursAndCO2valuess()
    {
        CO2ModelArrayList.clear();
        CO2ModelArrayList.add(new CO2Model("1pm",5.0));
        CO2ModelArrayList.add(new CO2Model("9am",12.0));
        CO2ModelArrayList.add(new CO2Model("10am",15.0));
        CO2ModelArrayList.add(new CO2Model("11am",19.12));
        CO2ModelArrayList.add(new CO2Model("1pm",23.5));
    }

    private void fillDaysAndCO2valuess2()
    {
        carbonEmissionPerWeekHoursArrayList.clear();
        carbonEmissionPerWeekHoursArrayList.add(new CO2Model("Monday",2.5));
        carbonEmissionPerWeekHoursArrayList.add(new CO2Model("Tuesday",4.1));
        carbonEmissionPerWeekHoursArrayList.add(new CO2Model("Wednesday",5.0));
        carbonEmissionPerWeekHoursArrayList.add(new CO2Model("Thursday",6.0));
        carbonEmissionPerWeekHoursArrayList.add(new CO2Model("Friday",3.9));
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