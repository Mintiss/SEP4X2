package sep4x2.android.ui.temperature;

import org.joda.time.DateTime;

import sep4x2.android.local_database.Entity.TemperatureData;

public class TemperatureModel {

    DateTime time;
    double temperature;

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public TemperatureModel(TemperatureData temperatureDataLiveData) {
        temperature=temperatureDataLiveData.getTemperature();
        time=DateTime.parse(temperatureDataLiveData.getUpdateTime());
    }

}
