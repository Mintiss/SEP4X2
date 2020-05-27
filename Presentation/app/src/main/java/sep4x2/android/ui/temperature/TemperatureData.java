package sep4x2.android.ui.temperature;

public class TemperatureData {

    double temperature;
    String time;

    public TemperatureData(double temperature, String time) {
        this.temperature = temperature;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
