package sep4x2.android.ui.co2;

import org.joda.time.DateTime;

public class CO2Model {

    String time;
    double co2metric;

    public CO2Model(String time, double co2metric) {
        this.time = time;
        this.co2metric = co2metric;
    }

    public DateTime getHours() {
        return DateTime.parse(time);
    }

    public void setHours(String hours) {
        this.time = hours;
    }

    public double getCo2metric() {
        return co2metric;
    }

    public void setCo2metric(double co2metric) {
        this.co2metric = co2metric;
    }
}
