package tech.dronerace.pomodoropremium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by theduke on 7/21/17.
 */

public class choosingGround extends AppCompatActivity {

    private Button b25;
    private Button b35;
    private Button b45;

    private TextView text25;
    private TextView text35;
    private TextView text45;
    private TextView choose;
    private TextView custom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosing_ground);

        b25 = (Button) findViewById(R.id.button_25id);
        b35 = (Button) findViewById(R.id.button_35id);
        b45 = (Button) findViewById(R.id.button_45id);

        Typeface font0 = Typeface.createFromAsset(getAssets(), "exo_extrabold.otf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "exo_extrabolditalic.otf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "exo_semibold.otf");
        Typeface font3 = Typeface.createFromAsset(getAssets(), "exo_semibolditalic.otf");

        choose = (TextView) findViewById(R.id.chooseTextFront);
        text25 = (TextView) findViewById(R.id.text_25id);
        text35 = (TextView) findViewById(R.id.text_35id);
        text45 = (TextView) findViewById(R.id.text_45id);
        custom = (TextView) findViewById(R.id.customBuild);

        choose.setTypeface(font0);
        text25.setTypeface(font3);
        text35.setTypeface(font3);
        text45.setTypeface(font3);
        custom.setTypeface(font2);


        Intent e = new Intent(choosingGround.this, Alarm.class);
        e.putExtra("extra", "soundOff");
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, e, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);

    }

    public void click25(View view) {

        Intent i = new Intent(view.getContext(), SecondBlood.class);
        i.putExtra("buttonClicked", "button25");
        startActivity(i);
    }

    public void click35(View view) {

        Intent i = new Intent(view.getContext(), SecondBlood.class);
        i.putExtra("buttonClicked", "button35");
        startActivity(i);
    }

    public void click45(View view) {

        Intent i = new Intent(view.getContext(), SecondBlood.class);
        i.putExtra("buttonClicked", "button45");
        startActivity(i);
    }

    public void clickCustomBuild(View view) {

        Intent i = new Intent(view.getContext(), CustomBuild.class);
        startActivity(i);
    }
}

