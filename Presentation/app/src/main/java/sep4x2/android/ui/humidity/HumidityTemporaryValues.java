package sep4x2.android.ui.humidity;

public class HumidityTemporaryValues {

    String time;
    double humidity;


    public HumidityTemporaryValues() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getHumidity() {
        return humidity;
    }



    public HumidityTemporaryValues(String time, double humindity) {
        this.time = time;
        this.humidity = humindity;
    }
}
