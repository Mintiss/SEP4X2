package sep4x2.android.SharedSensors;

import androidx.room.Ignore;

import com.google.gson.Gson;

import java.util.ArrayList;

public class WeeklyConverter {
    private int productID;
    private String metrics;
    private String startDate;
    private String endDate;
    private int weekNo;
    private int year;

    @Ignore
    private ArrayList<ArrayList<Double>> listOfMetrics;

    public WeeklyConverter(int productID, String metrics, String startDate, String endDate, int weekNo, int year) {
        Gson gson=new Gson();

        this.productID = productID;
        this.metrics = metrics;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekNo = weekNo;
        this.year = year;

        this.listOfMetrics=gson.fromJson(metrics,ArrayList.class);

    }

    public ArrayList<Double> getWeeklyTemperature(){
        ArrayList<Double> tempList= new ArrayList<>();

        for (int i = 0; i < listOfMetrics.size(); i++) {
            tempList.add(listOfMetrics.get(i).get(1));
        }

        return tempList;
    }

    public ArrayList<Double> getWeeklyHumidity(){
        ArrayList<Double> humidList= new ArrayList<>();

        for (int i = 0; i < listOfMetrics.size(); i++) {
            humidList.add(listOfMetrics.get(i).get(0));
        }

        return humidList;
    }

    public ArrayList<Double> getWeeklyCO2(){
        ArrayList<Double> co2List= new ArrayList<>();

        for (int i = 0; i < listOfMetrics.size(); i++) {
            co2List.add(listOfMetrics.get(i).get(3));
        }

        return co2List;
    }

    public ArrayList<Double> getWeeklyNoise(){
        ArrayList<Double> noiseList= new ArrayList<>();

        for (int i = 0; i < listOfMetrics.size(); i++) {
            noiseList.add(listOfMetrics.get(i).get(2));
        }

        return noiseList;
    }



    public int getProductID() {
        return productID;
    }

    public void setProductID(int roomID) {
        this.productID = roomID;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
