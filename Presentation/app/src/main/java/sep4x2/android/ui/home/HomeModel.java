package sep4x2.android.ui.home;

import org.joda.time.DateTime;

public class HomeModel {

    private double temperatureMeasurement;
    private String time;

    public HomeModel(double temperatureMeasurement, String time) {
        this.temperatureMeasurement = temperatureMeasurement;
        this.time = time;
    }

    public double getTemperatureMeasurement() {
        return temperatureMeasurement;
    }

    public DateTime getTime() {
        return DateTime.parse(time);
    }

    public void setTemperatureMeasurement(double temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }
}
