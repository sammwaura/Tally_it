package service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import ws.wolfsoft.creative.LowstockActivity;
import ws.wolfsoft.creative.R;

public class FirebaseInstanceService extends FirebaseMessagingService {
    String type = "";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() < 3){
            type = "stock";
            sendNotification(remoteMessage.getData().toString());
        }
        if (remoteMessage.getNotification() !=null){
            type = "message";
            sendNotification(remoteMessage.getNotification().getBody());
        }
        super.onMessageReceived(remoteMessage);

    }

    private void sendNotification(String messageBody) {
        String id="",message="",title="";

        if (type.equals("json")) {
            try {
                JSONObject jsonObject = new JSONObject(messageBody);
                id = jsonObject.getString("id");
                message = jsonObject.getString("message");
                title = jsonObject.getString("title");

            } catch (JSONException e) {
                //
            }
        }
            else if (type.equals("message")){
                message = messageBody;
            }

            Intent intent = new Intent(this, LowstockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle(getString(R.string.app_name));
            notificationBuilder.setContentText(message);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());
        }



    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE",s);
    }
}
