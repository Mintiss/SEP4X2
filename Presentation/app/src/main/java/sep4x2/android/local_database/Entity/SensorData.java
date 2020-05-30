package sep4x2.android.local_database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import sep4x2.android.network.SensorResponse;


@Entity(tableName = "Sensor_storage_table")
public class SensorData {

    @PrimaryKey(autoGenerate = true)
    private int metricsId;
    private int roomId;
    private double temperature;
    private double humidity;
    private double co2;
    private double noise;
    private String updateTime;


    public SensorData(int metricsId, int roomId, double temperature, double humidity, double co2, double noise, String updateTime) {
        this.metricsId = metricsId;
        this.roomId = roomId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.noise = noise;
        this.updateTime= updateTime;
    }

    public SensorData(SensorResponse response){
        this.metricsId = response.getMetricsID();
        this.roomId = response.getRoomID();
        this.temperature = response.getTemperature();
        this.humidity = response.getHumidity();
        this.co2 = response.getCo2();
        this.noise = response.getNoise();
        this.updateTime= response.getUpdateTime();
    }

    public int getMetricsId() {
        return metricsId;
    }

    public int getRoomId() {
        return roomId;
    }

    public double getTemperature() {

        return temperature;
    }

    public double getHumidity() {

        return humidity;
    }

    public double getCo2() {

        return co2;
    }

    public double getNoise() {

        return noise;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "metricsId=" + metricsId +
                ", roomId=" + roomId +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", co2=" + co2 +
                ", noise=" + noise +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getUpdateTime() {
        return updateTime;
    }

}
