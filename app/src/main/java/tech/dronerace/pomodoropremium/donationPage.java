package tech.dronerace.pomodoropremium;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by theduke on 8/1/17.
 */

public class donationPage extends AppCompatActivity{



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_page);

        Typeface font = Typeface.createFromAsset(getAssets(), "exo_semibold.otf");
        TextView support = (TextView) findViewById(R.id.supportText);
        support.setTypeface(font);

        font = Typeface.createFromAsset(getAssets(), "exo_medium.otf");
        TextView bitcoin = (TextView) findViewById(R.id.bitcoinAddress);
        bitcoin.setTypeface(font);
        TextView litecoin = (TextView) findViewById(R.id.litecoinAddress);
        litecoin.setTypeface(font);
        TextView ethereum = (TextView) findViewById(R.id.ethereumAddress);
        ethereum.setTypeface(font);

        font = Typeface.createFromAsset(getAssets(), "exo_extralight.otf");
        TextView bitcoinAddress = (TextView) findViewById(R.id.B_address);
        bitcoinAddress.setTypeface(font);
        TextView litecoinAddress = (TextView) findViewById(R.id.L_address);
        litecoinAddress.setTypeface(font);
        TextView ethereumAddress = (TextView) findViewById(R.id.E_address);
        ethereumAddress.setTypeface(font);

    }
}
