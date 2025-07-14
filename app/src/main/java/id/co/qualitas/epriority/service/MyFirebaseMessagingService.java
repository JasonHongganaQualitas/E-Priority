package id.co.qualitas.epriority.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.model.Notification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    NotificationCompat.Builder summaryNotificationBuilder;
    int bundleNotificationId = 100;
    int singleNotificationId = 100;
    SharedPreferences mPrefs;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        generateNotification(getApplicationContext(), remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTag());
        Log.e("Notif E-Priority", new Gson().toJson(remoteMessage.getData()));
        String jsonInString = new Gson().toJson(remoteMessage.getData());
        Notification notification = new Gson().fromJson(jsonInString, Notification.class);
        generateNotification(getApplicationContext(), notification);
//        notificationGroup(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getTag());
//        notification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData().get("sender"));
    }

    private void generateNotification(Context context, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = getResources().getString(R.string.channelID);
        String channelName = getResources().getString(R.string.channelName);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
//        String link = "https://www.google.com.sa/maps/search/"+notification.getLatitude() + "," + notification.getLongitude();
        notificationIntent.putExtra("message", "Firebase Notification");
//        notificationIntent.putExtra("location_link", link);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.ic_logo_round);
//            mBuilder.setColor(ContextCompat.getColor(context, R.color.white));
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_logo_round);
        }
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()));

        mBuilder.setContentTitle(notification.getTitle());
        mBuilder.setContentText(notification.getBody());
        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setGroup(group);
        mBuilder.setGroupSummary(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSound(uri);


        //If you don't want all notifications to overwrite add int m to unique value
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, mBuilder.build());
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    private void generateNotification(Context context, String msg, String group) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = getResources().getString(R.string.channelID);
        String channelName = getResources().getString(R.string.channelName);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
//            mBuilder.setColor(ContextCompat.getColor(context, R.color.white));
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        }
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        mBuilder.setContentTitle(msg);
        mBuilder.setContentText(msg);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setGroup(group);
        mBuilder.setGroupSummary(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSound(uri);


        //If you don't want all notifications to overwrite add int m to unique value
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, mBuilder.build());
//        notificationManager.notify(m, summaryNotificationBuilder.build());
    }

}