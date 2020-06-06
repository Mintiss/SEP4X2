package sep4x2.android.ui.register;

import sep4x2.android.network.SensorDataClient;
import sep4x2.android.network.UserAPI;

public class RegisterRepository {

    private SensorDataClient client;

    public RegisterRepository(SensorDataClient client) {
        this.client = client;
    }

    public void postUserToDatabase(String id, String productId, String token) {
        client.createAccount(id, productId, token);
    }
}
