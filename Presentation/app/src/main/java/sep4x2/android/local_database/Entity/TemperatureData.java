package sep4x2.android.local_database.Entity;

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
