package sep4x2.android.ui.temperature;

public class TemperatureModel {

    String time;

    double temperature;

    public TemperatureModel(double temperature) {
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

    public TemperatureModel(String time, double temperature) {
        this.time = time;
        this.temperature = temperature;
    }
}
