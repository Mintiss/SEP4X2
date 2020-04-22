package sep4x2.android.ui.gallery;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

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

import sep4x2.android.CarbonEmissionPerDayHours;
import sep4x2.android.R;

public class GalleryFragment extends Fragment  {

    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsname;

    ArrayList<CarbonEmissionPerDayHours> carbonEmissionPerDayHoursArrayList = new ArrayList<>();

    private GalleryViewModel galleryViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
       final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

                barChart = root.findViewById(R.id.CO2BarChart);

                //Creates new objects for bare entries arraylist and label arraylist

                barEntries =new ArrayList<>();
                labelsname = new ArrayList<>();

                fillHoursAndCO2valuess();

                for(int i=0; i< carbonEmissionPerDayHoursArrayList.size(); i++)
                {
                    String hour = carbonEmissionPerDayHoursArrayList.get(i).getHours();
                    int co2 = carbonEmissionPerDayHoursArrayList.get(i).getCo2metric();

                    barEntries.add(new BarEntry(i,co2));
                    labelsname.add(hour);
                }

                BarDataSet barDataSet = new BarDataSet(barEntries,"Daily CO2 in metric");
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

                barChart.animateY(600);
                barChart.invalidate();

            }
        });





        return root;
    }

    private void fillHoursAndCO2valuess()
    {
        carbonEmissionPerDayHoursArrayList.clear();
        carbonEmissionPerDayHoursArrayList.add(new CarbonEmissionPerDayHours("8am",12));
        carbonEmissionPerDayHoursArrayList.add(new CarbonEmissionPerDayHours("9am",12));
        carbonEmissionPerDayHoursArrayList.add(new CarbonEmissionPerDayHours("10am",15));
        carbonEmissionPerDayHoursArrayList.add(new CarbonEmissionPerDayHours("11am",19));
        carbonEmissionPerDayHoursArrayList.add(new CarbonEmissionPerDayHours("1pm",23));
    }



}