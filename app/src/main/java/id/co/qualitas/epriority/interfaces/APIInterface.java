package id.co.qualitas.epriority.interfaces;


import java.util.Map;

import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.model.LoginResponse;
import id.co.qualitas.epriority.model.SignUp;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    @POST
    Call<LoginResponse> login(@Header("Authorization") String authorization, @Header("Content-Type") String content_type, @Url String url);

    @POST(Constants.OAUTH_TOKEN_PATH)
    Call<LoginResponse> getToken(@Body SignUp body);

    @POST(Constants.OAUTH_GOOGLE_TOKEN_PATH)
    Call<LoginResponse> getGoogleToken(@Body Map<String, Object> body);

    @POST(Constants.API_GET_EMPLOYEE_DETAIL)
    Call<WSMessage> getEmployeeDetail(@Body User body);

    @POST(Constants.API_EDIT_PROFILE)
    Call<WSMessage> editProfile(@Body User body);

    @POST(Constants.API_CHANGE_PASSWORD)
    Call<WSMessage> changePassword(@Body User body);

    @GET(Constants.API_GET_APK)
    Call<WSMessage> getAPK();

    @GET(Constants.API_CHECK_VERSION)
    Call<WSMessage> getVersion();

    @GET(Constants.API_LOG_OUT)
    Call<WSMessage> logOut();

    @POST(Constants.API_SIGN_UP)
    Call<WSMessage> signUp(@Body SignUp body);

    @POST(Constants.API_FORGET_PASSWORD)
    Call<WSMessage> forgetPassword(@Body SignUp body);

    @POST(Constants.API_FORGET_PASSWORD_CONFIRM_CODE)
    Call<WSMessage> forgetPasswordConfirmCode(@Body SignUp body);

    @POST(Constants.API_RESET_PASSWORD)
    Call<WSMessage> resetPassword(@Body SignUp body);

    //cust
    @GET(Constants.API_GET_FLIGHT_INFORMATION)
    Call<WSMessage> getFlightInformation(@Query("access_key") String access_key, @Query("flight_iata") String flight_iata);

    @POST(Constants.API_ON_GOING_CUSTOMER_TRIPS)
    Call<WSMessage> getOnGoingCustomerTrips(@Body TripRequest tripRequestRequest);

    @GET(Constants.API_ON_GOING_AGENT_BOOKINGS)
    Call<WSMessage> getOnGoingAgentBookings(@Query("offset") String offset, @Query("limit") String limit);

    @GET(Constants.API_PENDING_AGENT_BOOKINGS)
    Call<WSMessage> getPendingAgentBookings(@Query("offset") String offset, @Query("limit") String limit);

    @GET(Constants.API_STATS)
    Call<WSMessage> getStats(@Query("date") String date);//2025-07-16

    @GET(Constants.API_LIST_PACKAGE)
    Call<WSMessage> getListPackage(@Query("tripType") String tripType);//?tripType=arrival

    @GET(Constants.API_LIST_AGENT)
    Call<WSMessage> getListAgent(@Query("date") String date, @Query("offset") String offset, @Query("limit") String limit);//?date=2025-07-18&offset=0&limit=10

    @POST(Constants.API_LIST_COUNTRIES)
    Call<WSMessage> getListCountries(@Body TripRequest tripRequestRequest);

    @POST(Constants.API_LIST_FLIGHT_CLASS)
    Call<WSMessage> getListFlightClass(@Body TripRequest tripRequestRequest);

    @POST(Constants.API_CREATE_TRIPS)
    Call<WSMessage> createTrips(@Body TripsResponse tripRequestRequest);

    @GET(Constants.API_CREATE_TRIPS + "{id}")
    Call<WSMessage> getDetailTrips(@Path("id") String id);
    @POST(Constants.API_BOOKING + "{id}/accept")
    Call<WSMessage> acceptBooking(@Path("id") String id);
    @POST(Constants.API_BOOKING + "{id}/decline")
    Call<WSMessage> declineBooking(@Path("id") String id);
    @GET(Constants.API_BOOKING + "{id}")
    Call<WSMessage> getBookingDetails(@Path("id") String id);
}
