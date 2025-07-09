package com.example.e_priority.interfaces;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    @GET
    Call<ResponseBody> getImage(@Url String url);
//    @POST(Constants.API_LOGIN)
//    Call<WSMessage> login(@Body LoginRequest request);
//    @POST(Constants.API_LOGOUT)
//    Call<WSMessage> logout();
//    @POST(Constants.API_CHECK_VERSION)
//    Call<OdooWS> getLatestVersion(
//            @Body Map request
//    );
//    @GET(Constants.API_GET_ADDRESS)
//    Call<WSMessage> getAddress();
//    @GET(Constants.API_GET_USER)
//    Call<WSMessage> getUser();
//    @GET(Constants.API_GET_PRODUCT)
//    Call<WSMessage> getProduct();
//    @GET(Constants.API_GET_SEARCH_PRODUCT)
//    Call<WSMessage> getSearchProduct( @Query("search") String query);
//    @GET(Constants.API_GET_SERVICE_REQUEST)
//    Call<WSMessage> getServiceReq(@Query("limit") int limit,
//                                  @Query("offset") int offset,
//                                  @Query("search") String search
//    );
//    @GET(Constants.API_GET_LOCATION)
//    Call<WSMessage> getLocation();
//    @POST(Constants.API_CHANGE_LOCATION)
//    Call<WSMessage> changeLocation(@Body Map request);
//    @POST(Constants.API_POST_SERVICE_REQUEST)
//    Call<WSMessage> serviceRequest(@Body ServiceRequest request);
//    @POST(Constants.API_CHANGE_PASSWORD)
//    Call<WSMessage> changePassword(@Body Map request);
}
