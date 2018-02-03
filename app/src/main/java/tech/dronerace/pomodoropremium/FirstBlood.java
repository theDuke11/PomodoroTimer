package tech.dronerace.pomodoropremium;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstBlood extends AppCompatActivity {

    Button button;
    TextView pomodoro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_blood);

        button = (Button) findViewById(R.id.buttonWork);
        pomodoro = (TextView) findViewById(R.id.text_pomodoro);

        Typeface font0 = Typeface.createFromAsset(getAssets(), "komikax.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "exo_semibold.otf");

        pomodoro.setTypeface(font0);
        button.setTypeface(font1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), choosingGround.class);
                startActivity(i);
            }
        });
    }
}

