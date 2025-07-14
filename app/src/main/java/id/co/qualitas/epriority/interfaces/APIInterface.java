package id.co.qualitas.epriority.interfaces;


import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.model.Employee;
import id.co.qualitas.epriority.model.LoginResponse;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIInterface {

    @POST
    Call<LoginResponse> login(@Header("Authorization") String authorization, @Header("Content-Type") String content_type, @Url String url);

    @POST(Constants.API_GET_EMPLOYEE_DETAIL)
    Call<WSMessage> getEmployeeDetail(@Body Employee body);

    @GET(Constants.API_LOG_OUT)
    Call<WSMessage> logOut();
}
