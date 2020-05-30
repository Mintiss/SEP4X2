package sep4x2.android.SharedSensors;

import org.joda.time.DateTime;

public class Noise {

    double noise;
    String updateTime;

    public DateTime getTime() {
        return DateTime.parse(updateTime);
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public void setTime(String time) {
        this.updateTime = time;
    }

    public Noise(double noise, String updateTime) {
        this.noise = noise;
        this.updateTime = updateTime;
    }
}
