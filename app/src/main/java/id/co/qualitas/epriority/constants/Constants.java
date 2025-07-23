package id.co.qualitas.epriority.constants;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class Constants {
    public static final long LONG_400 = 1000;
    public static final long LONG_3000 = 2000;
    public static final long MAX_NON_MOVEMENT = 1720976400000L;
    public static final String DECIMAL_PATTERN = "#,###,###,###,###,###.#";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int REQUEST_STORAGE_CODE = 6;
    public static final int REQUEST_NOTIFICATION_CODE = 8;
    public static final int REQUEST_LOCATION_CODE = 7;
    public static final String FRAGMENT_DIALOG = "dialog";
    public static final String UNKNOWN_RESOURCE = "Unknown Resource";
    public static final String NO_CONNECTION = "no connection";
    public static final String AUTHORIZATION_LOGIN = "Basic V0VCX0NMSUVOVDpXRUJfQ0xJRU5U";
    public static final String BEARER = "Bearer ";
    public static final String HTTP_HEADER_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String OAUTH_TOKEN_PATH = "/oauth/token";
    public static final String OAUTH_GOOGLE_TOKEN_PATH = "/oauth/google";
    public static final String API_LOG_OUT = "/api/v1/mobile/user/logout";
    public static final String API_EDIT_PROFILE = "/api/v1/mobile/user/editProfile";
    public static final String API_CHANGE_PASSWORD = "api/v1/mobile/user/changePassword";
    public static final String API_GET_EMPLOYEE_DETAIL = "/api/v1/mobile/user/getEmployeeDetail";
    public static final String API_SIGN_UP = "/api/v1/mobile/user/createUserCustomer";
    public static final String API_GET_APK = "/api/v1/mobile/user/getAPK";
    public static final String API_CHECK_VERSION = "/api/v1/mobile/user/getAPKVersion";
    public static final String API_FORGET_PASSWORD = "/api/v1/mobile/email/sendCode";//email
    public static final String API_FORGET_PASSWORD_CONFIRM_CODE = "/api/v1/mobile/email/confirmationCode";//email, code
    public static final String API_RESET_PASSWORD = "/api/v1/mobile/user/resetPassword";//email, newPassword, confirmPassword
    public static final String API_GET_FLIGHT_INFORMATION = "/v1/flights";
    //customer
    public static final String API_ON_GOING_CUSTOMER_TRIPS = "/api/v1/mobile/customer/trips/ongoing";
    public static final String API_LIST_AGENT = "/api/v1/mobile/customer/available-agents";
    public static final String API_CREATE_TRIPS = "/api/v1/mobile/customer/trips";
    public static final String API_DETAIL_TRIPS = "/api/v1/mobile/customer/trips/";
    //agent
    public static final String API_ON_GOING_AGENT_BOOKINGS = "/api/v1/mobile/agent/bookings/ongoing";
    public static final String API_PENDING_AGENT_BOOKINGS = "/api/v1/mobile/agent/bookings/pending";
    public static final String API_STATS = "/api/v1/mobile/home/stats";
    public static final String API_LIST_PACKAGE = "/api/v1/mobile/package/details";
    public static final String API_LIST_COUNTRIES = "/api/v1/dropdown/country";
    public static final String API_LIST_FLIGHT_CLASS = "/api/v1/dropdown/flight-class";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CLIENT_ID = "|000";
    public static final String GRANT_TYPE = "grant_type=password&";
    public static final String _GRANT_TYPE = "|000&password=";
    public static final String QUESTION_MARK = "?";
    public static final String TOKEN = "TOKEN";
    public static final String ERROR_LOGIN = "errorLogin";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_LOGIN = "keyLogin";
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String IS_TOKEN = "IsToken";
    public static final String KEY_TOKEN = "key_token";
    public static final String IS_USER = "isUser";
    public static final String KEY_USER = "keyUser";
    public static final String IS_COOKIES = "isCookies";
    public static final String KEY_COOKIES = "keyCookies";
    public static final String IS_BOARDING = "isBoarding";
    public static final String KEY_BOARDING = "keyBoarding";
    public static final String IS_MOVEMENT = "isMovement";
    public static final String KEY_MOVEMENT = "keyMovement";
    public static final String IS_CART = "isCart";
    public static final String KEY_CART = "keyCart";
    public static final String KEY_USER_ID = "key_userid";
    public static final String KEY_PASSWORD = "key_password";
    public static final String IS_REMEMBER_ME = "IsRememberMe";
    public static final String BACKGROUND_EXCEPTION = "BACKGROUND_EXCEPTION";
    public static final String FOLDER_NAME = "/ZECatalog";
    public static final String PATTERN_DECIMAL = "#,###,###,###,###,###.#######";
    public static final String KEY_URL = "key_url";
    public static final String IS_URL = "IsUrl";
    public static final String BOOKING_DETAIL = "Booking detail";
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_LIMIT = "20";
    public static final String DEFAULT_LIMIT_ALL = "99999";
    public static final String ARRIVAL = "arrival";
    public static final String DEPARTURE = "departure";
    public static final String TYPE_TAB = "type tab";
    public static final String EMAIL_FORGOT_PASSWORD = "email forgot password";
    public static final String DATA_CREATE_TRIPS = "data create trips";
    public static final String DATA_PASSENGER = "data passenger";
    public static final String TRIP_HEADER = "trip header";
    public static final String QR_DATA = "QR Data";
    public static final String TRIP_DETAILS = "Trip Details";

    //PRD
//    public static String BASE_URL = "http://192.168.137.202:3443";
//    public static String BASE_URL = "http://192.168.1.20:3443";
    public static String BASE_URL = "http://70.153.16.18:3443";
    public static String IP = "";

    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String DATA_NOT_FOUND = "Data not found";
    public static final String REGIISTERID = "Register ID";
    public static final String PATTERN_DATE_3 = "yyyy-MM-dd";

    public static final String DATE_PATTERN_1 = "dd.MM.yyyy";
    public static final String DATE_PATTERN_2 = "yyyy-MM-dd";
    public static final String DATE_PATTERN_3 = "EEE, dd MMM yyyy";
    public static final String DATE_PATTERN_4 = "dd MMM yyyy";
    public static final String DATE_PATTERN_5 = "dd/MM/yyyy HH:mm";//11/12/2020 13:49
    public static final String DATE_PATTERN_6 = "dd/MM/yyyy HH:mm:ss";//11/12/2020 13:49
    public static final String DATE_PATTERN_7 = "EEE, dd MMM yyyy HH:mm";//11/12/2020 13:49
    public static final String DATE_PATTERN_8 = "dd/MM/yyyy";
    public static final String DATE_PATTERN_9 = "HH:mm";
    public static final String DATE_PATTERN_10 = "MMM dd yyyy hh:mm a";
    public static final String DATE_PATTERN_11 = "dd MMM yy hh:mm a";
    public static final String DATE_PATTERN_12 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_13 = "HH:mm:ss";


}
