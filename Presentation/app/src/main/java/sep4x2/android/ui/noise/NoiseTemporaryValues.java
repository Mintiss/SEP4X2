package sep4x2.android.ui.noise;

public class NoiseTemporaryValues {

    double noise;
    String time;

    public String getTime() {
        return time;
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

    public NoiseTemporaryValues( String time,double noise) {
        this.noise = noise;
        this.time = time;
    }
}
