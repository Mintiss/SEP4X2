package sep4x2.android.ui.temperature;

import org.joda.time.DateTime;

public class TemperatureModel {

    String updateTime;
    double temperature;

    public DateTime getTime() {
        return DateTime.parse(updateTime);
    }

    public double getTemperature() {
        return temperature;
    }

    public TemperatureModel(double temperature,String updateTime) {
        this.temperature=temperature;
        this.updateTime=updateTime;
    }
}
