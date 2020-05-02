package sep4x2.android.ui.co2;

public class CO2Model {

    String hours;
    double co2metric;

    public CO2Model(String hours, double co2metric) {
        this.hours = hours;
        this.co2metric = co2metric;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getCo2metric() {
        return co2metric;
    }

    public void setCo2metric(double co2metric) {
        this.co2metric = co2metric;
    }
}
