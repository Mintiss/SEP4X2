package sep4x2.android.ui.temperature;

public class TemperatureTemporaryValues {

    String time;
    double temperature;



    public double getTemperature() {
        return temperature;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {return time;}


    public TemperatureTemporaryValues( String time,double temperature) {
        this.temperature=temperature;
        this.time=time;
    }
}
