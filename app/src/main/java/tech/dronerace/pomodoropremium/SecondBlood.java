package tech.dronerace.pomodoropremium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by theduke on 7/21/17.
 */

public class SecondBlood extends AppCompatActivity {

    private String seconds;
    private String minutes;
    private String milliseconds;

    private long remainedTime = 0;
    private long elapsedTime = 0;
    private long startTime;
    private long breakTime;
    private long startTimeInitialG = startTime;
    private Handler tHandler = new Handler();
    private final int REFRESH_RATE = 100;
    private boolean stopped = false;
    private long startioni;
    private boolean breakON = false;
    private int[] customValue = null;
    private String optionValue = null;
    private int blockCount = 0;

    private LinearLayout linearLayout;
    private ImageView imageView_block1;
    private ImageView imageView_block2;
    private ImageView imageView_block3;
    private ImageView imageView_block4;
    private ImageView imageView_block5;
    private LinearLayout linearLayout2;
    private ImageView imageView_block6;
    private ImageView imageView_block7;
    private ImageView imageView_block8;
    private ImageView imageView_block9;
    private ImageView imageView_block10;
    private ImageView progressImage;

    SharedPreferences savedData;
    AlarmManager am;
    private boolean memento = false;
    private boolean keyBack = false;



    //Main Timer
    private Runnable startTimer = new Runnable() {
        @Override
        public void run() {
            startTimer_2();
            remainedTime = startTime - elapsedTime;
            updateTimer(remainedTime);
            tHandler.postDelayed(this, REFRESH_RATE);

            if(remainedTime <= 0) {
                tHandler.removeCallbacks(startTimer);
                countDone();
            }
        }
    };

    //Check-Up Timer
    private void startTimer_2() {
        elapsedTime = System.currentTimeMillis() - startioni;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_blood);
        createTextViews();

        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        savedData = getSharedPreferences("sharedInfo", MODE_PRIVATE);
        blockCount = savedData.getInt("countingBlocks", 0);
        memento = savedData.getBoolean("didItHappen", false);
        savedData.edit().remove("countingBlocks").apply();
        savedData.edit().remove("didItHappen").apply();

        showStartButton();


        Bundle optionChosen = getIntent().getExtras();

        customValue = optionChosen.getIntArray("buttonClicked");
        optionValue = optionChosen.getString("buttonClicked");

        Intent i = new Intent(SecondBlood.this, Alarm.class);
        i.putExtra("extra", "soundOff");
        PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        sendBroadcast(i);

        if (optionValue != null) {

            switch (optionValue) {
                case "button25":
                    startTimeInitialG = 1500000;
                    startTime = 1500000;
                    breakTime = 300000;
                    break;

                case "button35":
                    startTimeInitialG = 2100000;
                    startTime = 2100000;
                    breakTime = 600000;

                    break;

                case "button45":
                    startTimeInitialG = 2700000;
                    startTime = 2700000;
                    breakTime = 900000;
                    break;
            }
            keyBack = false;
        }
        if (customValue != null) {
            startTimeInitialG = (customValue[0] * 60) * 1000;
            startTime = (customValue[0] * 60) * 1000;
            breakTime = (customValue[1] * 60) * 1000;
            keyBack = false;
        }


        //Make a 'skip' TextView underlined and font changed and then hide it
        TextView skip_id = (TextView) findViewById(R.id.skip_id);
        Typeface font0 = Typeface.createFromAsset(getAssets(), "exo_semibolditalic.otf");
        skip_id.setTypeface(font0);
        skip_id.setPaintFlags(skip_id.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        skip_id.setVisibility(View.GONE);

        updateTimer(startTime);

        //IF we come here after the phone has been awoken from sleep via notification!
        if(memento) {
            memento = false;
            ((TextView) findViewById(R.id.timer_id)).setText("00:00");
            ((TextView) findViewById(R.id.timerMs_id)).setText(".0");
            ((TextView) findViewById(R.id.timer_id)).setTextColor(Color.parseColor("#FF3737"));
            ((TextView) findViewById(R.id.timerMs_id)).setTextColor(Color.parseColor("#FF3737"));
            ((Button) findViewById(R.id.b_break)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.skip_id)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.b_start)).setVisibility(View.GONE);
            blockDoneMethod();
        }
        drawProgress();

    }
    public void clickStart(View view) {

        hideStartButton();
        breakON = false;
        if(stopped) {

            startioni = System.currentTimeMillis();
        }
        else {
            remainedTime = startTime;
            startioni = System.currentTimeMillis();
        }

        tHandler.removeCallbacks(startTimer);
        tHandler.postDelayed(startTimer, 0);
    }

    public void clickReset(View view) {

        stopped = false;
        startTime = startTimeInitialG;

        updateTimer(startTime);

    }

    public void clickStop(View view) {
        showStartButton();
        tHandler.removeCallbacks(startTimer);
        startTime = remainedTime;
        stopped = true;
    }

    public void clickBreak(View view) {
        startTime = breakTime;
        remainedTime = breakTime;
        stopped = false;
        startioni = System.currentTimeMillis();
        tHandler.removeCallbacks(startTimer);
        tHandler.postDelayed(startTimer, 0);
    }

    private void showStartButton() {
        ((Button) findViewById(R.id.b_start)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.b_reset)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.b_stop)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.b_break)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.skip_id)).setVisibility(View.GONE);
    }

    private void hideStartButton() {
        ((Button) findViewById(R.id.b_start)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.b_stop)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.b_break)).setVisibility(View.GONE);
    }
    public void updateTimer(long millis) {
        int secs = (int) (millis/1000);
        int min = (int) ((millis/1000))/60;

        secs = secs % 60;
        seconds = String.valueOf(secs);
        if(secs == 0) {
            seconds = "00";
        }
        if(secs < 10 && secs > 0) {
            seconds = "0" + secs;
        }

        //min = min % 60; //since we want to be able to schedule even greater than 60min cap (99min Max)
        minutes = String.valueOf(min);
        if(min == 0) {
            minutes = "00";
        }
        if(min < 10 && min > 0) {
            minutes = "0" + min;
        }

        milliseconds = String.format("%1$tL", millis).substring(0, 1);

        ((TextView) findViewById(R.id.timer_id)).setText(minutes + ":" + seconds);
        ((TextView) findViewById(R.id.timerMs_id)).setText("." + milliseconds);
    }
    public void countDone() {
        ((TextView) findViewById(R.id.timer_id)).setText("00:00");
        ((TextView) findViewById(R.id.timerMs_id)).setText(".0");
        ((TextView) findViewById(R.id.timer_id)).setTextColor(Color.parseColor("#FF3737"));
        ((TextView) findViewById(R.id.timerMs_id)).setTextColor(Color.parseColor("#FF3737"));
        ((Button) findViewById(R.id.b_start)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.b_stop)).setVisibility(View.GONE);

        if (breakON) {
            ((Button) findViewById(R.id.b_reset)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.skip_id)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.b_break)).setVisibility(View.GONE);
        } else {
            ((Button) findViewById(R.id.b_break)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.skip_id)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);
        }


        //----------------Alarm && Vibration && Progress----------------\\

        final Intent i = new Intent(SecondBlood.this, Alarm.class);
        i.putExtra("extra", "soundOn");
        switch ((int)(startTimeInitialG/1000)) {
            case 3:
                i.putExtra("extraN", "button25");
                break;
            case 5:
                i.putExtra("extraN", "button35");
                break;
            case 2700:
                i.putExtra("extraN", "button45");
                break;
        }
        if (customValue != null) {
            i.putExtra("extraA", customValue);
        }

        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        }


        //NEXT: waiting for the button to be clicked so we can stop the sound and reset the view/calculus
        Button b = (Button) findViewById(R.id.b_reset);

        //Break has finished! wait for a click to reset to 'start'
        if ((findViewById(R.id.b_reset)).getVisibility() == View.VISIBLE) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateTimer(startTimeInitialG);
                    i.putExtra("extra", "soundOff");
                    PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    sendBroadcast(i);

                    clickReset(v);
                    breakON = false;

                    ((Button) findViewById(R.id.b_start)).setVisibility(View.VISIBLE);
                    ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);

                    ((TextView) findViewById(R.id.timer_id)).setTextColor(Color.WHITE);
                    ((TextView) findViewById(R.id.timerMs_id)).setTextColor(Color.WHITE);

                }
            });
        }
        //Block has finished & 'break' button is visible, +1 for count, draw and wait for click
        else if ((findViewById(R.id.b_break)).getVisibility() == View.VISIBLE) {

            blockDoneMethod();

        }
    }
    //Explained above in 'countDone'
    public void blockDoneMethod() {
        blockCount++;
        drawProgress();

        final Intent i = new Intent(SecondBlood.this, Alarm.class);
        Button breakk = (Button) findViewById(R.id.b_break);
        breakk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i.putExtra("extra", "soundOff");
                PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                sendBroadcast(i);

                clickBreak(v);
                breakON = true;

                ((Button) findViewById(R.id.b_start)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.b_stop)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.b_break)).setVisibility(View.GONE);

                TextView skip_id = (TextView) findViewById(R.id.skip_id);
                skip_id.setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.timer_id)).setTextColor(Color.WHITE);
                ((TextView) findViewById(R.id.timerMs_id)).setTextColor(Color.WHITE);
            }
        });
    }

    //if 'skip' TextView is clicked
    public void clickSkip(View view) {
        final TextView skip_id = (TextView) findViewById(R.id.skip_id);
        skip_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tHandler.removeCallbacks(startTimer);
                Intent i = new Intent(SecondBlood.this, Alarm.class);
                i.putExtra("extra", "soundOff");
                PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                sendBroadcast(i);

                clickReset(v);
                breakON = false;

                ((Button) findViewById(R.id.b_start)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.b_reset)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.b_break)).setVisibility(View.GONE);
                skip_id.setVisibility(View.GONE);

                ((TextView) findViewById(R.id.timer_id)).setTextColor(Color.WHITE);
                ((TextView) findViewById(R.id.timerMs_id)).setTextColor(Color.WHITE);
            }
        });
    }
    public void clickDonate(View view) {
        Intent intent = new Intent(view.getContext(), donationPage.class);
        startActivity(intent);
    }
    private void drawProgress() {

        int check = blockCount / 4;
        switch (check) {
            case 0:
                checkMethod(0);
                break;
            case 1:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.regularis);
                checkMethod(1);
                break;
            case 2:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.nerd);
                checkMethod(2);
                break;
            case 3:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.zen);
                checkMethod(3);
                break;
            case 4:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(4);
                break;
            case 5:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(5);
                break;
            case 6:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                imageView_block6.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(6);
                break;
            case 7:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                imageView_block6.setImageResource(R.drawable.app_block_4);
                imageView_block7.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(7);
                break;
            case 8:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                imageView_block6.setImageResource(R.drawable.app_block_4);
                imageView_block7.setImageResource(R.drawable.app_block_4);
                imageView_block8.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(8);
                break;
            case 9:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                imageView_block6.setImageResource(R.drawable.app_block_4);
                imageView_block7.setImageResource(R.drawable.app_block_4);
                imageView_block8.setImageResource(R.drawable.app_block_4);
                imageView_block9.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(9);
                break;
            case 10:
                imageView_block1.setImageResource(R.drawable.app_block_4);
                imageView_block2.setImageResource(R.drawable.app_block_4);
                imageView_block3.setImageResource(R.drawable.app_block_4);
                imageView_block4.setImageResource(R.drawable.app_block_4);
                imageView_block5.setImageResource(R.drawable.app_block_4);
                imageView_block6.setImageResource(R.drawable.app_block_4);
                imageView_block7.setImageResource(R.drawable.app_block_4);
                imageView_block8.setImageResource(R.drawable.app_block_4);
                imageView_block9.setImageResource(R.drawable.app_block_4);
                imageView_block10.setImageResource(R.drawable.app_block_4);
                progressImage.setImageResource(R.drawable.dr_strange);
                checkMethod(10);
                break;

        }
    }
    public void checkMethod(int x) {
        int check2 = blockCount % 4;
        switch (x) {
            case 0:
                switch (check2) {
                    case 1:
                        imageView_block1.setImageResource(R.drawable.app_block_1);
                        break;
                    case 2:
                        imageView_block1.setImageResource(R.drawable.app_block_2);
                        break;
                    case 3:
                        imageView_block1.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.regularis);
                        break;
                }
                break;
            case 1:
                switch (check2) {
                    case 1:
                        imageView_block2.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.nerd);
                        break;
                    case 2:
                        imageView_block2.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.nerd);
                        break;
                    case 3:
                        imageView_block2.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.nerd);
                        break;
                }
                break;
            case 2:
                switch (check2) {
                    case 1:
                        imageView_block3.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.zen);
                        break;
                    case 2:
                        imageView_block3.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.zen);
                        break;
                    case 3:
                        imageView_block3.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.zen);
                        break;
                }
                break;
            case 3:
                switch (check2) {
                    case 1:
                        imageView_block4.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block4.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block4.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 4:
                switch (check2) {
                    case 1:
                        imageView_block5.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block5.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block5.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 5:
                switch (check2) {
                    case 1:
                        imageView_block6.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block6.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block6.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 6:
                switch (check2) {
                    case 1:
                        imageView_block7.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block7.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block7.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 7:
                switch (check2) {
                    case 1:
                        imageView_block8.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block8.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block8.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 8:
                switch (check2) {
                    case 1:
                        imageView_block9.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block9.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block9.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;
            case 9:
                switch (check2) {
                    case 1:
                        imageView_block10.setImageResource(R.drawable.app_block_1);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 2:
                        imageView_block10.setImageResource(R.drawable.app_block_2);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                    case 3:
                        imageView_block10.setImageResource(R.drawable.app_block_3);
                        progressImage.setImageResource(R.drawable.dr_strange);
                        break;
                }
                break;

        }
    }
    public void createTextViews() {
        linearLayout = (LinearLayout) findViewById(R.id.productivityLayout);
        imageView_block1 = (ImageView) findViewById(R.id.imageView_block1);
        imageView_block2 = (ImageView) findViewById(R.id.imageView_block2);
        imageView_block3 = (ImageView) findViewById(R.id.imageView_block3);
        imageView_block4 = (ImageView) findViewById(R.id.imageView_block4);
        imageView_block5 = (ImageView) findViewById(R.id.imageView_block5);
        linearLayout2 = (LinearLayout) findViewById(R.id.productivityLayout2);
        imageView_block6 = (ImageView) findViewById(R.id.imageView_block6);
        imageView_block7 = (ImageView) findViewById(R.id.imageView_block7);
        imageView_block8 = (ImageView) findViewById(R.id.imageView_block8);
        imageView_block9 = (ImageView) findViewById(R.id.imageView_block9);
        imageView_block10 = (ImageView) findViewById(R.id.imageView_block10);
        progressImage = (ImageView) findViewById(R.id.progressImage);
    }

    @Override
    public void onBackPressed() {
        //Ask the user if they want to quit
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("All progress will be lost")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Stop the activity
                        tHandler.removeCallbacks(startTimer);
                        Intent e = new Intent(SecondBlood.this, Alarm.class);
                        e.putExtra("extra", "soundOff");
                        sendBroadcast(e);
                        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, e, PendingIntent.FLAG_UPDATE_CURRENT);
                        am.cancel(pi);
                        finish();
                    }
                })
                .setNegativeButton("no", null)
                .show();

    }

    @Override
    protected void onStop() {
        super.onStop();

        //if the onStop() has not been initiated cause of 'back' button, then you should call notification
        if (!keyBack) {
            memento = true;
        }
        savedData = getSharedPreferences("sharedInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("countingBlocks", blockCount);
        editor.putBoolean("didItHappen", memento);
        editor.commit();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        //cancel alarm
        tHandler.removeCallbacks(startTimer);
        final Intent i = new Intent(SecondBlood.this, Alarm.class);
        i.putExtra("extra", "soundOff");
        sendBroadcast(i);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pi);
        //reset variables
        blockCount = 0;
        memento = false;
        savedData = getSharedPreferences("sharedInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("countingBlocks", blockCount);
        editor.putBoolean("didItHappen", memento);
        editor.commit();
    }
}

