package sep4x2.android.network.GETResponses;

public class SensorResponse {

   int metricsID;
   int roomID;
   double temperature;
   double humidity;
   double cO2;
   double noise;

    public int getMetricsID() {
        return metricsID;
    }

    public int getRoomID() {
        return roomID;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getCo2() {
        return cO2;
    }

    public double getNoise() {
        return noise;
    }

    public String getUpdateTime() {
        return lastUpdated;
    }

    String lastUpdated;


    public SensorResponse(int metricsId, int roomID, double temperature, double humidity, double cO2, double noise, String lastUpdated) {
        this.metricsID = metricsId;
        this.roomID = roomID;
        this.temperature = temperature;
        this.humidity = humidity;
        this.cO2 = cO2;
        this.noise = noise;
        this.lastUpdated = lastUpdated;
    }

}
