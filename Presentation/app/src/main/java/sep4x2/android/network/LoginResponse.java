package sep4x2.android.network;

import sep4x2.android.ui.login.LoginModel;

class LoginResponse {
    private int id;
    private String email;
    private String password;

   public LoginModel getLoginInformation()
    {
        return new LoginModel(email, password);
    }



}
