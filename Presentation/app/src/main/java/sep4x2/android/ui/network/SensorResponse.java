package sep4x2.android.ui.network;

import sep4x2.android.ui.temperature.TemperatureModel;

class SensorResponse {

   private int metricsID;
   private double humidity;
   private double temperature;
   private double noise;
   private double co2;
   private String time;

   public TemperatureModel getTemperature()
   {
       return new TemperatureModel(time,temperature);
   }

}
