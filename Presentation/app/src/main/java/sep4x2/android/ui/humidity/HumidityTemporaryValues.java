package sep4x2.android.ui.humidity;

public class HumidityTemporaryValues {

    String time;
    double humindity;
    HumidityRepository humidityRepository;

    public HumidityTemporaryValues() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getHumindity() {
        return humindity;
    }

    public void setHumindity(double humindity) {
        this.humindity = humindity;
    }

    public HumidityTemporaryValues(String time, double humindity) {
        this.time = time;
        this.humindity = humindity;
    }
}
