package sep4x2.android.ui.tools;

public class HumidityModel {

    String time;
    double humindity;

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

    public HumidityModel(String time, double humindity) {
        this.time = time;
        this.humindity = humindity;
    }
}
