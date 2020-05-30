package sep4x2.android.SharedSensors;

import org.joda.time.DateTime;

import sep4x2.android.ui.humidity.HumidityRepository;

public class Humidity {

    String updateTime;
    double humidity;

    public DateTime getTime() {
        return DateTime.parse(updateTime);
    }

    public double getHumidity() {
        return humidity;
    }

    public Humidity(double humidity,String updateTime) {
        this.updateTime = updateTime;
        this.humidity = humidity;
    }
}
