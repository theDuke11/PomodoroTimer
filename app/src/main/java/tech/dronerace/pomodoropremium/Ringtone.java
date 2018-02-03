package tech.dronerace.pomodoropremium;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by theduke on 7/21/17.
 */

public class Ringtone extends Service {

    MediaPlayer alarmPlayer;
    boolean isRunning;
    private String decide;
    private int[] custom;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //get info on weather the reset was pressed and convert it
        //to int so you can see weather you play or stop the music
        String state = intent.getExtras().getString("extra");
        decide = intent.getExtras().getString("extraN");
        custom = intent.getExtras().getIntArray("extraA");
        assert state != null; //prevents app from crashing; make sure 'state' is not in 'null' state
        switch (state) {
            case "soundOn":
                startId = 1;
                break;
            case "soundOff":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        //we pressed reset or ('soundOff' ->extra) then cut it off
        if(startId == 0 ) {
            if(alarmPlayer != null) {
                alarmPlayer.release();
                isRunning = false;
            }
        }
        //we pressed nothing and is automatically ('soundOn' ->extra) so let it play
        else if(startId == 1) {

                alarmPlayer = MediaPlayer.create(this, R.raw.imperial_alarm);
                alarmPlayer.start();
                isRunning = true;

                notification();

        }

        return START_NOT_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notification() {
        NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent_notify = new Intent(this.getApplicationContext(), SecondBlood.class);
        if (decide != null) {
            switch (decide) {
                case "button25":
                    intent_notify.putExtra("buttonClicked", "button25");
                    break;
                case "button35":
                    intent_notify.putExtra("buttonClicked", "button35");
                    break;
                case "button45":
                    intent_notify.putExtra("buttonClicked", "button45");
                    break;
            }
        }
        if (custom != null) {
            intent_notify.putExtra("buttonClicked", custom);
        }
        PendingIntent pendingIntent_notify = PendingIntent.getActivity(this, 0, intent_notify, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notify_popup = new Notification.Builder(this)
                .setContentTitle("Swish!!")
                .setContentText("Block Done")
                .setContentIntent(pendingIntent_notify)
                .setSmallIcon(R.drawable.notification_icn)
                .setTicker("Block Done")
                .setAutoCancel(true)
                .build();
        notify_popup.flags |= Notification.FLAG_AUTO_CANCEL;

        notify_manager.notify(0, notify_popup);
    }
}
