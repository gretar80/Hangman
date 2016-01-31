/**
 * Gretar AEvarsson
 * gretar80@gmail.com
 * © 2016
 */

package com.example.s198586.hangman;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class Gameover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oppdaterSprak(Hangman.sprak);
        setContentView(R.layout.activity_gameover);

        Hangman.tilstand = Hangman.SpillTilstand.Tapt;

        // label for antall vinn og antall tap
        TextView antVinn = (TextView)findViewById(R.id.labelAntallVinn);
        TextView antTap = (TextView)findViewById(R.id.labelAntallTap);
        antVinn.setText(Hangman.antallVinn + "");
        antTap.setText(Hangman.antallTap + "");

        // tar i mot og viser valgt ord nar mann er gameover
        TextView tv = (TextView)findViewById(R.id.labelNoSpacing);
        tv.setText(getIntent().getStringExtra("valgtOrdFraHangman").toUpperCase());

        // store knappene
        ImageButton nyttSpillImg = (ImageButton) findViewById(R.id.nyttSpill);
        ImageButton avslutteImg = (ImageButton) findViewById(R.id.avslutte);

        nyttSpillImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Hangman.tilstand = Hangman.SpillTilstand.NyttSpill;
                finish();
            }
        });

        avslutteImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Hangman.tilstand = Hangman.SpillTilstand.Avslutte;
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hangman_aktivitet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.actionbar_nyttspill :
                Hangman.tilstand = Hangman.SpillTilstand.NyttSpill;
                finish();
                return true;
            case R.id.actionbar_notater:
                Intent intent_regler = new Intent(this,Notater.class);
                startActivity(intent_regler);
                return true;
            case R.id.actionbar_sprak :
                Hangman.tilstand = Hangman.SpillTilstand.EndreSprak;
                Hangman.sprak = Hangman.sprak.equals("nb") ? "en" : "nb";
                if(Hangman.sjekketBokstav) Hangman.antallTap++;
                finish();
                return true;
            case R.id.actionbar_avslutte :
                Hangman.tilstand = Hangman.SpillTilstand.Avslutte;
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Hvis man åpner regler i vinn-aktiviteten
        switch (Hangman.tilstand){
            // Hvis man åpner regler i vinn-aktiviteten og trykker pa nytt spill
            case NyttSpill:
                finish();
                break;
            // Hvis man åpner regler i vinn-aktiviteten og trykker pa endre sprak
            case EndreSprak:
                finish();
                break;
        }
    }

    // oppdater sprak
    void oppdaterSprak(String s){
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration cfg = new Configuration();
        cfg.locale = locale;
        getBaseContext().getResources().updateConfiguration(cfg, getBaseContext().getResources().getDisplayMetrics());
    }
}
