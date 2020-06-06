package sep4x2.android.local_database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import sep4x2.android.network.GETResponses.WeeklyResponse;

@Entity(tableName = "Weekly_Statistics_table")
public class WeeklyStatisticsAllData {

    private int productID;
    private String metrics;
    private String startDate;
    private String endDate;
    @PrimaryKey
    private int weekNo;
    private int year;

    public WeeklyStatisticsAllData(int productID, String metrics, String startDate, String endDate, int weekNo, int year) {
        this.metrics = metrics;
        this.productID = productID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekNo = weekNo;
        this.year = year;
    }

    public WeeklyStatisticsAllData(WeeklyResponse response)
    {
        Gson gson= new Gson();

        this.productID=response.getRoomID();
        this.metrics=gson.toJson(response.getMetrics());
        this.startDate=response.getStartDate();
        this.endDate=response.getEndDate();
        this.weekNo=response.getWeekNo();
        this.year=response.getYear();
    }

    public int getProductID() {
        return productID;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setProductID(int roomID) {
        this.productID = roomID;
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
