package sep4x2.android.ui.local_database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

//@Entity(tableName = "temperature_data_table")
public class TemperatureData {

    //@PrimaryKey(autoGenerate = true)
    //private int metricsId;
    //private int roomId;
    private double temperature;
    private String updateTime;

    public TemperatureData(double temperature, String updateTime) {
        this.temperature = temperature;
        this.updateTime = updateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime ) {
        this.updateTime = updateTime;
    }

}
