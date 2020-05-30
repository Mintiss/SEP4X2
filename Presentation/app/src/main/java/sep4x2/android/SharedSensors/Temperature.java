package sep4x2.android.SharedSensors;

import org.joda.time.DateTime;

public class Temperature {

    String updateTime;
    double temperature;

    public DateTime getTime() {
        return DateTime.parse(updateTime);
    }

    public double getTemperature() {
        return temperature;
    }

    public Temperature(double temperature, String updateTime) {
        this.temperature=temperature;
        this.updateTime=updateTime;
    }
}
