package sep4x2.android.ui.network;

public class SensorData {

    int metricsId;
    int roomId;
    double temperature;
    double humidity;
    double co2;
    double noise;
    String updateTime;

    public SensorData(int metricsId, int roomId, double temperature, double humidity, double co2, double noise, String updateTime) {
        this.metricsId = metricsId;
        this.roomId = roomId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.noise = noise;
        this.updateTime = updateTime;
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

    public String getUpdateTime() {
        return updateTime;
    }
}
