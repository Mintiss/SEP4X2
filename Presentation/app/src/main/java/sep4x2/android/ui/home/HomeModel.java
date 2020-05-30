package sep4x2.android.ui.home;

public class HomeModel {
        private double temperatureMeasurement;


    public HomeModel(double temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }

    public double getTemperatureMeasurement() {
        return temperatureMeasurement;
    }

    public void setTemperatureMeasurement(double temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }
}
