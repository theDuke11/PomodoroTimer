package tech.dronerace.pomodoropremium;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

/**
 * Created by theduke on 7/21/17.
 */

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //String soundOff_string = intent.getStringExtra("extra"); //fetch the sent off extra
        String soundOff_string = intent.getExtras().getString("extra");
        String chosenValues = intent.getExtras().getString("extraN");
        int [] customValues = intent.getExtras().getIntArray("extraA");


        Intent ringtone_service = new Intent(context, Ringtone.class);
        ringtone_service.putExtra("extra", soundOff_string); //pass the string for on/off of sound to Ringtone class
        ringtone_service.putExtra("extraN", chosenValues); //pass the string for right Block size to Ringtone class
        ringtone_service.putExtra("extraA", customValues); //pass the string for right CUSTOM Block size to Ringtone
        context.startService(ringtone_service);

        if(soundOff_string.equalsIgnoreCase("soundOn")) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        }
    }
}
