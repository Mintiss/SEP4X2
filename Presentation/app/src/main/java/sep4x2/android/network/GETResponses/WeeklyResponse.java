package sep4x2.android.network.GETResponses;

import com.google.gson.Gson;

import java.util.ArrayList;

public class WeeklyResponse {
    private int roomID;
    private ArrayList<ArrayList<Double>> metrics;
    private String startDate;
    private String endDate;
    private int weekNo;
    private int year;

    public WeeklyResponse(int roomID, ArrayList<ArrayList<Double>> metrics, String startDate, String endDate, int weekNo, int year) {
        this.roomID = roomID;
        this.metrics = metrics;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekNo = weekNo;
        this.year = year;
    }

    public int getRoomID() {
        return roomID;
    }

    public ArrayList<ArrayList<Double>> getMetrics() {
        return metrics;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public int getYear() {
        return year;
    }
}
