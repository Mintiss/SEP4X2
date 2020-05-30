package sep4x2.android.ui.noise;

import org.joda.time.DateTime;

public class NoiseModel {

    double noise;
    String time;

    public DateTime getTime() {
        return DateTime.parse(time);
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NoiseModel(double noise, String time) {
        this.noise = noise;
        this.time = time;
    }
}
