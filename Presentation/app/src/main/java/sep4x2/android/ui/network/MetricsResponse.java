package sep4x2.android.ui.network;

import sep4x2.android.ui.temperature.TemperatureModel;

class MetricsResponse {

   private int metricsID;
   private double humidity;
   private double temperature;
   private double noise;
   private double co2;

   public TemperatureModel getTemperature()
   {
       return new TemperatureModel(temperature);
   }

}
