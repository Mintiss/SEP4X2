package sep4x2.android.ui.co2;

public class CO2TemporaryValues {

    String time;
    double co2;

    public CO2TemporaryValues(String time, double co2) {
        this.time = time;
        this.co2 = co2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String hours) {
        this.time = time;
    }

    public double getCo2() {
        return co2;
    }


}
