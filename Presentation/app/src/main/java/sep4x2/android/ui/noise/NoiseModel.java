package sep4x2.android.ui.noise;

public class NoiseModel {

    double noise;
    String time;

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NoiseModel(double noise, String time) {
        this.noise = noise;
        this.time = time;
    }
}
