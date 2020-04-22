package sep4x2.android;

public class CarbonEmissionPerDayHours {

    String hours;
    int co2metric;

    public CarbonEmissionPerDayHours(String hours, int co2metric) {
        this.hours = hours;
        this.co2metric = co2metric;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public int getCo2metric() {
        return co2metric;
    }

    public void setCo2metric(int co2metric) {
        this.co2metric = co2metric;
    }
}
