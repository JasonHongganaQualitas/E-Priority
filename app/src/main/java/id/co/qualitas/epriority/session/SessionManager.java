package id.co.qualitas.epriority.session;

import android.content.Context;
import android.content.SharedPreferences;

import id.co.qualitas.epriority.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    SharedPreferences sharedPreferencesLogin, sharedPreferencesUser, prefURL, prefToken, prefCart, prefCookies, prefBoarding, prefRememberMe;
    SharedPreferences.Editor editorLogin, editorUser, editorURL, editorToken, editorCart, editorCookies, editorBoarding, editorRememberMe;
    String SHARED_PREF_NAME = "session";
    String SHARED_PREF_URL = "url";
    String SHARED_PREF_CART = "CART";
    String SHARED_PREF_TOKEN = "token";
    String SHARED_PREF_USER = "user";
    String SHARED_PREF_COOKIES = "Cookies";
    String SHARED_PREF_BOARDING = "boarding";
    String PREF_REMEMBER_ME = "remember_me";

    public SessionManager(Context context){
        sharedPreferencesLogin = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        prefURL = context.getSharedPreferences(SHARED_PREF_URL, Context.MODE_PRIVATE);
        prefCart = context.getSharedPreferences(SHARED_PREF_CART, Context.MODE_PRIVATE);
        prefToken = context.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        sharedPreferencesUser = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        prefCookies = context.getSharedPreferences(SHARED_PREF_COOKIES, Context.MODE_PRIVATE);
        prefBoarding = context.getSharedPreferences(SHARED_PREF_BOARDING, Context.MODE_PRIVATE);
        prefRememberMe = context.getSharedPreferences(PREF_REMEMBER_ME, Context.MODE_PRIVATE);
        editorLogin = sharedPreferencesLogin.edit();
        editorURL = prefURL.edit();
        editorCart = prefCart.edit();
        editorToken = prefToken.edit();
        editorUser = sharedPreferencesUser.edit();
        editorCookies = prefCookies.edit();
        editorBoarding = prefBoarding.edit();
        editorRememberMe = prefRememberMe.edit();
    }

    public void createLoginSession(String ex) {
        editorLogin.clear();
        editorLogin.commit();
        editorLogin.putBoolean(Constants.IS_LOGIN, true);
        editorLogin.putString(Constants.KEY_LOGIN, ex);
        editorLogin.commit();
    }

    public void createUserSession(String ex) {
        editorUser.clear();
        editorUser.commit();
        editorUser.putBoolean(Constants.IS_USER, true);
        editorUser.putString(Constants.KEY_USER, ex);
        editorUser.commit();
    }

    public void createCookiesSession(String ex) {
        editorCookies.clear();
        editorCookies.commit();
        editorCookies.putBoolean(Constants.IS_COOKIES, true);
        editorCookies.putString(Constants.KEY_COOKIES, ex);
        editorCookies.commit();
    }

    public void createBoardingSession(String ex) {
        editorBoarding.clear();
        editorBoarding.commit();
        editorBoarding.putBoolean(Constants.IS_BOARDING, true);
        editorBoarding.putString(Constants.KEY_BOARDING, ex);
        editorBoarding.commit();
    }

    public void createTokenSession(String token) {
        // Clearing all data from Shared Preferences
        editorToken.clear();
        editorToken.commit();
        // Storing email in pref
        editorToken.putBoolean(Constants.IS_TOKEN, true);
        editorToken.putString(Constants.KEY_TOKEN, token);
        // commit changes
        editorToken.commit();
    }

    public void createUrlSession(String url) {
        editorURL.clear();
        editorURL.commit();
        editorURL.putBoolean(Constants.IS_URL, true);
        editorURL.putString(Constants.KEY_URL, url);
        editorURL.commit();
    }

    public void createCart(String data) {
        editorCart.clear();
        editorCart.commit();
        editorCart.putBoolean(Constants.IS_CART, true);
        editorCart.putString(Constants.KEY_CART, data);
        editorCart.commit();
    }

    public void createRememberMeSession(String userID, String password) {
        editorRememberMe.clear();
        editorRememberMe.commit();
        editorRememberMe.putBoolean(Constants.IS_REMEMBER_ME, true);
        editorRememberMe.putString(Constants.KEY_USER_ID, userID);
        editorRememberMe.putString(Constants.KEY_PASSWORD, password);
        editorRememberMe.commit();
    }

    public void logoutUser() {
        editorLogin.clear();
        editorLogin.commit();
        editorLogin.putBoolean(Constants.IS_LOGIN, false);
    }

    public void clearCookies() {
        editorCookies.clear();
        editorCookies.commit();
        editorCookies.putBoolean(Constants.IS_COOKIES, false);
    }

    public void clearBoarding() {
        editorBoarding.clear();
        editorBoarding.commit();
        editorBoarding.putBoolean(Constants.IS_BOARDING, false);
    }

    public void clearUrl() {
        editorURL.clear();
        editorURL.commit();
        editorURL.putBoolean(Constants.IS_URL, false);
    }

    public void clearCart() {
        editorCart.clear();
        editorCart.commit();
        editorCart.putBoolean(Constants.IS_CART, false);
    }
    public void clearToken() {
        editorToken.clear();
        editorToken.commit();
        editorToken.putBoolean(Constants.IS_TOKEN, false);
    }

    public void clearUser() {
        editorUser.clear();
        editorUser.commit();
        editorUser.putBoolean(Constants.IS_USER, false);
    }
    public void clearRememberMe() {
        editorRememberMe.clear();
        editorRememberMe.commit();
        editorRememberMe.putBoolean(Constants.IS_REMEMBER_ME, false);
    }

    public boolean isLoggedIn() {
        return sharedPreferencesLogin.getBoolean(Constants.IS_LOGIN, false);
    }

    public boolean isUser() {
        return sharedPreferencesUser.getBoolean(Constants.IS_USER, false);
    }

    public boolean isToken() {
        return prefToken.getBoolean(Constants.IS_TOKEN, false);
    }

    public boolean isUrlEmpty() {
        return prefURL.getBoolean(Constants.IS_URL, false);
    }

    public boolean isCart() {
        return prefCart.getBoolean(Constants.IS_CART, false);
    }

    public boolean isCookies() {
        return prefCookies.getBoolean(Constants.IS_COOKIES, false);
    }

    public boolean isBoarding() {
        return prefBoarding.getBoolean(Constants.IS_BOARDING, false);
    }
    public boolean isRememberMe() {
        return prefRememberMe.getBoolean(Constants.IS_REMEMBER_ME, false);
    }

    public Map<String, String> getLoginDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.KEY_LOGIN, sharedPreferencesLogin.getString(Constants.KEY_LOGIN, null));
        return user;
    }

    public Map<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.KEY_USER, sharedPreferencesUser.getString(Constants.KEY_USER, null));
        return user;
    }

    public Map<String, String> getUrl() {
        HashMap<String, String> url = new HashMap<>();
        url.put(Constants.KEY_URL, prefURL.getString(Constants.KEY_URL, null));
        return url;
    }

    public Map<String, String> getCart() {
        HashMap<String, String> cart = new HashMap<>();
        cart.put(Constants.KEY_CART, prefCart.getString(Constants.KEY_CART, null));
        return cart;
    }

    public Map<String, String> getToken() {
        HashMap<String, String> token = new HashMap<String, String>();
        token.put(Constants.KEY_TOKEN, prefToken.getString(Constants.KEY_TOKEN, null));
        return token;
    }

    public Map<String, String> getCookies() {
        HashMap<String, String> cookies = new HashMap<String, String>();
        cookies.put(Constants.KEY_COOKIES, prefCookies.getString(Constants.KEY_COOKIES, null));
        return cookies;
    }
    public Map<String, String> getBoarding() {
        HashMap<String, String> boarding = new HashMap<String, String>();
        boarding.put(Constants.KEY_BOARDING, prefBoarding.getString(Constants.KEY_BOARDING, null));
        return boarding;
    }

    public Map<String, String> getRememberMe() {
        HashMap<String, String> rememberMe = new HashMap<>();
        rememberMe.put(Constants.KEY_USER_ID, prefRememberMe.getString(Constants.KEY_USER_ID, null));
        rememberMe.put(Constants.KEY_PASSWORD, prefRememberMe.getString(Constants.KEY_PASSWORD, null));
        return rememberMe;
    }
}
