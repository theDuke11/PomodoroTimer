package tech.dronerace.pomodoropremium;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by theduke on 7/21/17.
 */

public class CustomBuild extends AppCompatActivity {

    private EditText eWork;
    private EditText eBreak;
    private Button equipButton;
    private String eWork_string;
    private String eBreak_string;
    private int eWork_int;
    private int eBreak_int;
    private TextView armory;
    private TextView quote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_build);

        eWork = (EditText) findViewById(R.id.edit_work);
        eBreak = (EditText) findViewById(R.id.edit_break);
        equipButton = (Button) findViewById(R.id.equip_button);
        armory = (TextView) findViewById(R.id.ArmoryText);
        quote = (TextView) findViewById(R.id.quote_id);

        Typeface font0 = Typeface.createFromAsset(getAssets(), "exo_semibolditalic.otf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "exo_extralightitalic.otf");
        armory.setTypeface(font0);
        eWork.setTypeface(font0);
        eBreak.setTypeface(font0);
        equipButton.setTypeface(font1);

    }

    public void clickEquip(View view) {

        eWork_string = eWork.getText().toString();
        eBreak_string = eBreak.getText().toString();

        try {
            eWork_int = Integer.parseInt(eWork_string);
            eBreak_int = Integer.parseInt(eBreak_string);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Only numbers accepted", Toast.LENGTH_LONG).show();
        }

        if (eWork_int <= 99 && eBreak_int <= 99 && eWork_int > 0 && eBreak_int > 0) {
            int array[] = {eWork_int, eBreak_int};
            Intent intent = new Intent(view.getContext(), SecondBlood.class);
            intent.putExtra("buttonClicked", array);
            startActivity(intent);
        } else if(eWork_int > 99 || eBreak_int > 99) {
            Toast.makeText(getApplicationContext(), "Wow! keep those numbers small cowboy!", Toast.LENGTH_LONG).show();
        } else if(eWork_int < 0 || eBreak_int < 0) {
            Toast.makeText(getApplicationContext(), "We want to be productive here, right?", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Let's try again! :)", Toast.LENGTH_LONG).show();
        }

    }
}
