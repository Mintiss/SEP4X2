package sep4x2.android.SharedSensors;

import org.joda.time.DateTime;

public class CO2 {

    String updateTime;
    double co2;

    public CO2(double co2,String updateTime) {
        this.updateTime = updateTime;
        this.co2= co2;
    }

    public DateTime getTime() {
        return DateTime.parse(updateTime);
    }

    public double getCo2metric() {
        return co2;
    }

}
