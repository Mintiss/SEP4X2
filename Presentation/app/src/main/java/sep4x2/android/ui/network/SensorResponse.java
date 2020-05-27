package sep4x2.android.ui.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Random;

import sep4x2.android.ui.temperature.TemperatureData;

public class SensorResponse {

   private int metricsID;
   private double temperature;
   private double humidity;
   private double co2;
   private double noise;
   private String lastUpdated;

   public SensorResponse(){
      metricsID = new Random().nextInt(10000);
      temperature = new Random().nextInt(50);
      humidity = new Random().nextInt(100);
      co2 = new Random().nextInt(5000);
      noise = new Random().nextInt(200);

      lastUpdated = "hour";
   }

   public int getMetricsID() {
      return metricsID;
   }

   public void setMetricsID(int metricsID) {
      this.metricsID = metricsID;
   }

   public MutableLiveData<TemperatureData> getTemperature() {
      temperature = (new Random().nextInt(10)+15);
      MutableLiveData<TemperatureData> temperatureDataLiveData;
      temperatureDataLiveData = new MutableLiveData<TemperatureData>();
      temperatureDataLiveData.setValue(new TemperatureData(temperature, lastUpdated));
      return temperatureDataLiveData;
   }

   public void setTemperature(double temperature) {
      this.temperature = temperature;
   }

   public double getHumidity() {
      return humidity;
   }

   public void setHumidity(double humidity) {
      this.humidity = humidity;
   }

   public double getCo2() {
      return co2;
   }

   public void setCo2(double co2) {
      this.co2 = co2;
   }

   public double getNoise() {
      return noise;
   }

   public void setNoise(double noise) {
      this.noise = noise;
   }

   public String getLastUpdated() {
      return lastUpdated;
   }

   public void setLastUpdated(String lastUpdated) {
      this.lastUpdated = lastUpdated;
   }

}
