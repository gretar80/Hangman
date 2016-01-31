/**
 * Gretar AEvarsson
 * gretar80@gmail.com
 * © 2016
 */

package com.example.s198586.hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;


public class Hangman extends AppCompatActivity{

    // spillets tilstand
    enum SpillTilstand{ ForsteSpill,    // forste spill
                        Kjoring,        // spill er under kjoring
                        NyttSpill,      // nar brukeren skal starte nytt spill
                        Avslutte,       // nar brukeren vil avslutte etter at han har vunnet/tapt
                        EndreSprak,     // nar brukeren vil endre sprak
                        Tapt,           // nar brukeren har tapt spillet
                        Vunnet}         // nar brukeren har vunnet spillet
    static SpillTilstand tilstand = SpillTilstand.ForsteSpill;

    // diverse tellere
    static int antallVinn = 0;  // teller for antall vinn
    static int antallTap = 0;   // teller for antall tap
    private int antallSjanser = 7;    // antall antallSjanser igjen
    static boolean sjekketBokstav = false;   // hvis en eller flere bokstaver har blitt funnet

    // default språk
    static String sprak = "nb";

    //private Ordliste ordliste = new Ordliste(this);
    private Ordliste ordliste;
    private Ord ord = new Ord("");
    //private Ord ord;

    // knapper
    Button knapp_a;Button knapp_b;Button knapp_c;Button knapp_d;
    Button knapp_e;Button knapp_f;Button knapp_g;Button knapp_h;
    Button knapp_i;Button knapp_j;Button knapp_k;Button knapp_l;
    Button knapp_m;Button knapp_n;Button knapp_o;Button knapp_p;
    Button knapp_q;Button knapp_r;Button knapp_s;Button knapp_t;
    Button knapp_u;Button knapp_v;Button knapp_w;Button knapp_x;
    Button knapp_y;Button knapp_z;
    // norske knapper æ ø å
    Button knapp_ae;Button knapp_oe;Button knapp_aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oppdaterSprak(sprak);
        setContentView(R.layout.activity_hangman);

        // gjore knappe klare
        forberedeKnapper();
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
                if(tilstand == SpillTilstand.Kjoring)
                    antallTap++;
                nyttSpill();
                return true;
            case R.id.actionbar_notater:
                Intent intent_regler = new Intent(this,Notater.class);
                startActivity(intent_regler);
                return true;
            case R.id.actionbar_sprak :
                tilstand = SpillTilstand.EndreSprak;
                sprak = sprak.equals("nb") ? "en" : "nb";
                if(sjekketBokstav)
                    antallTap++;
                Intent i = getIntent();
                finish();
                startActivity(i);
                return true;
            case R.id.actionbar_avslutte :
                tilstand = SpillTilstand.Avslutte;
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // kjore riktige metoder avhengig av spillets tilstand
        switch (tilstand){
            case ForsteSpill:
                tilstand = SpillTilstand.Kjoring;
                oppdaterSprak(sprak);
                ordliste = new Ordliste(this);
                ord = new Ord(ordliste.generereOrd());
                nyttSpill();
                break;

            case NyttSpill:
                tilstand = SpillTilstand.Kjoring;
                nyttSpill();
                break;

            case EndreSprak:
                oppdaterSprak(sprak);
                //ordliste.endreSprak(this);
                ordliste = new Ordliste(this);
                tilstand = SpillTilstand.ForsteSpill;
                Intent i = getIntent();
                finish();
                startActivity(i);
                break;

            case Vunnet:
                tilstand = SpillTilstand.Kjoring;
                nyttSpill();
                break;

            case Tapt:
                tilstand = SpillTilstand.Kjoring;
                nyttSpill();
                break;

            case Avslutte:
                tilstand = SpillTilstand.ForsteSpill;
                finish();
                System.exit(0);
                break;

            case Kjoring:
                oppdaterVinnOgTapLabels();
                oppdaterBilde();
                oppdaterTekst(ord.getOrdMedMellomrom());
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle b) {
        b.putString("ORD",ord.getValgtOrd());
        b.putString("OMM",ord.getOrdMedMellomrom());
        b.putString("SOB",ord.getStrekOgBokstaver());
        b.putString("VOMM", ord.getValgtOrdMedMellomrom());
        b.putSerializable("TILSTAND", tilstand);
        b.putInt("ANTVINN", antallVinn);
        b.putInt("ANTTAP", antallTap);
        b.putInt("ANTSJANSER", antallSjanser);
        b.putBoolean("SJEKKET", sjekketBokstav);
        b.putString("SPRAK", sprak);
        b.putParcelable("ORDLISTE", ordliste);

        // lagre knappens tilstand
        final Button b_a = (Button)findViewById(R.id.button_a); int v_a; v_a = b_a.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_A", v_a);
        final Button b_b = (Button)findViewById(R.id.button_b); int v_b; v_b = b_b.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_B",v_b);
        final Button b_c = (Button)findViewById(R.id.button_c); int v_c; v_c = b_c.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_C", v_c);
        final Button b_d = (Button)findViewById(R.id.button_d); int v_d; v_d = b_d.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_D", v_d);
        final Button b_e = (Button)findViewById(R.id.button_e); int v_e; v_e = b_e.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_E", v_e);
        final Button b_f = (Button)findViewById(R.id.button_f); int v_f; v_f = b_f.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_F", v_f);
        final Button b_g = (Button)findViewById(R.id.button_g); int v_g; v_g = b_g.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_G", v_g);
        final Button b_h = (Button)findViewById(R.id.button_h); int v_h; v_h = b_h.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_H", v_h);
        final Button b_i = (Button)findViewById(R.id.button_i); int v_i; v_i = b_i.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_I", v_i);
        final Button b_j = (Button)findViewById(R.id.button_j); int v_j; v_j = b_j.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_J", v_j);
        final Button b_k = (Button)findViewById(R.id.button_k); int v_k; v_k = b_k.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_K", v_k);
        final Button b_l = (Button)findViewById(R.id.button_l); int v_l; v_l = b_l.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_L", v_l);
        final Button b_m = (Button)findViewById(R.id.button_m); int v_m; v_m = b_m.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_M", v_m);
        final Button b_n = (Button)findViewById(R.id.button_n); int v_n; v_n = b_n.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_N", v_n);
        final Button b_o = (Button)findViewById(R.id.button_o); int v_o; v_o = b_o.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_O", v_o);
        final Button b_p = (Button)findViewById(R.id.button_p); int v_p; v_p = b_p.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_P", v_p);
        final Button b_q = (Button)findViewById(R.id.button_q); int v_q; v_q = b_q.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_Q", v_q);
        final Button b_r = (Button)findViewById(R.id.button_r); int v_r; v_r = b_r.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_R", v_r);
        final Button b_s = (Button)findViewById(R.id.button_s); int v_s; v_s = b_s.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_S", v_s);
        final Button b_t = (Button)findViewById(R.id.button_t); int v_t; v_t = b_t.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_T", v_t);
        final Button b_u = (Button)findViewById(R.id.button_u); int v_u; v_u = b_u.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_U", v_u);
        final Button b_v = (Button)findViewById(R.id.button_v); int v_v; v_v = b_v.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_V", v_v);
        final Button b_w = (Button)findViewById(R.id.button_w); int v_w; v_w = b_w.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_W", v_w);
        final Button b_x = (Button)findViewById(R.id.button_x); int v_x; v_x = b_x.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_X", v_x);
        final Button b_y = (Button)findViewById(R.id.button_y); int v_y; v_y = b_y.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_Y", v_y);
        final Button b_z = (Button)findViewById(R.id.button_z); int v_z; v_z = b_z.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_Z", v_z);
        final Button b_ae = (Button)findViewById(R.id.button_ae); int v_ae; v_ae = b_ae.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_AE", v_ae);
        final Button b_oe = (Button)findViewById(R.id.button_oe); int v_oe; v_oe = b_oe.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_OE",v_oe);
        final Button b_aa = (Button)findViewById(R.id.button_aa); int v_aa; v_aa = b_aa.getVisibility() == View.VISIBLE ? 1 : 0; b.putInt("B_AA",v_aa);

        super.onSaveInstanceState(b);

    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        super.onRestoreInstanceState(s);
        if(s != null) {
            ord.setValgtOrd(s.getString("ORD"));
            ord.setOrdMedMellomrom(s.getString("OMM"));
            ord.setStrekOgBokstaver(s.getString("SOB"));
            ord.setValgtOrdMedMellomrom(s.getString("VOMM"));
            tilstand = (SpillTilstand) s.getSerializable("TILSTAND");
            antallVinn = s.getInt("ANTVINN");
            antallTap = s.getInt("ANTTAP");
            antallSjanser = s.getInt("ANTSJANSER");
            sjekketBokstav = s.getBoolean("SJEKKET");
            sprak = s.getString("SPRAK");
            ordliste = s.getParcelable("ORDLISTE");

            // sette knappens tilstand
            final Button b_a = (Button)findViewById(R.id.button_a); if(s.getInt("B_A") == 1) b_a.setVisibility(View.VISIBLE); else b_a.setVisibility(View.INVISIBLE);
            final Button b_b = (Button)findViewById(R.id.button_b); if(s.getInt("B_B") == 1) b_b.setVisibility(View.VISIBLE); else b_b.setVisibility(View.INVISIBLE);
            final Button b_c = (Button)findViewById(R.id.button_c); if(s.getInt("B_C") == 1) b_c.setVisibility(View.VISIBLE); else b_c.setVisibility(View.INVISIBLE);
            final Button b_d = (Button)findViewById(R.id.button_d); if(s.getInt("B_D") == 1) b_d.setVisibility(View.VISIBLE); else b_d.setVisibility(View.INVISIBLE);
            final Button b_e = (Button)findViewById(R.id.button_e); if(s.getInt("B_E") == 1) b_e.setVisibility(View.VISIBLE); else b_e.setVisibility(View.INVISIBLE);
            final Button b_f = (Button)findViewById(R.id.button_f); if(s.getInt("B_F") == 1) b_f.setVisibility(View.VISIBLE); else b_f.setVisibility(View.INVISIBLE);
            final Button b_g = (Button)findViewById(R.id.button_g); if(s.getInt("B_G") == 1) b_g.setVisibility(View.VISIBLE); else b_g.setVisibility(View.INVISIBLE);
            final Button b_h = (Button)findViewById(R.id.button_h); if(s.getInt("B_H") == 1) b_h.setVisibility(View.VISIBLE); else b_h.setVisibility(View.INVISIBLE);
            final Button b_i = (Button)findViewById(R.id.button_i); if(s.getInt("B_I") == 1) b_i.setVisibility(View.VISIBLE); else b_i.setVisibility(View.INVISIBLE);
            final Button b_j = (Button)findViewById(R.id.button_j); if(s.getInt("B_J") == 1) b_j.setVisibility(View.VISIBLE); else b_j.setVisibility(View.INVISIBLE);
            final Button b_k = (Button)findViewById(R.id.button_k); if(s.getInt("B_K") == 1) b_k.setVisibility(View.VISIBLE); else b_k.setVisibility(View.INVISIBLE);
            final Button b_l = (Button)findViewById(R.id.button_l); if(s.getInt("B_L") == 1) b_l.setVisibility(View.VISIBLE); else b_l.setVisibility(View.INVISIBLE);
            final Button b_m = (Button)findViewById(R.id.button_m); if(s.getInt("B_M") == 1) b_m.setVisibility(View.VISIBLE); else b_m.setVisibility(View.INVISIBLE);
            final Button b_n = (Button)findViewById(R.id.button_n); if(s.getInt("B_N") == 1) b_n.setVisibility(View.VISIBLE); else b_n.setVisibility(View.INVISIBLE);
            final Button b_o = (Button)findViewById(R.id.button_o); if(s.getInt("B_O") == 1) b_o.setVisibility(View.VISIBLE); else b_o.setVisibility(View.INVISIBLE);
            final Button b_p = (Button)findViewById(R.id.button_p); if(s.getInt("B_P") == 1) b_p.setVisibility(View.VISIBLE); else b_p.setVisibility(View.INVISIBLE);
            final Button b_q = (Button)findViewById(R.id.button_q); if(s.getInt("B_Q") == 1) b_q.setVisibility(View.VISIBLE); else b_q.setVisibility(View.INVISIBLE);
            final Button b_r = (Button)findViewById(R.id.button_r); if(s.getInt("B_R") == 1) b_r.setVisibility(View.VISIBLE); else b_r.setVisibility(View.INVISIBLE);
            final Button b_s = (Button)findViewById(R.id.button_s); if(s.getInt("B_S") == 1) b_s.setVisibility(View.VISIBLE); else b_s.setVisibility(View.INVISIBLE);
            final Button b_t = (Button)findViewById(R.id.button_t); if(s.getInt("B_T") == 1) b_t.setVisibility(View.VISIBLE); else b_t.setVisibility(View.INVISIBLE);
            final Button b_u = (Button)findViewById(R.id.button_u); if(s.getInt("B_U") == 1) b_u.setVisibility(View.VISIBLE); else b_u.setVisibility(View.INVISIBLE);
            final Button b_v = (Button)findViewById(R.id.button_v); if(s.getInt("B_V") == 1) b_v.setVisibility(View.VISIBLE); else b_v.setVisibility(View.INVISIBLE);
            final Button b_w = (Button)findViewById(R.id.button_w); if(s.getInt("B_W") == 1) b_w.setVisibility(View.VISIBLE); else b_w.setVisibility(View.INVISIBLE);
            final Button b_x = (Button)findViewById(R.id.button_x); if(s.getInt("B_X") == 1) b_x.setVisibility(View.VISIBLE); else b_x.setVisibility(View.INVISIBLE);
            final Button b_y = (Button)findViewById(R.id.button_y); if(s.getInt("B_Y") == 1) b_y.setVisibility(View.VISIBLE); else b_y.setVisibility(View.INVISIBLE);
            final Button b_z = (Button)findViewById(R.id.button_z); if(s.getInt("B_Z") == 1) b_z.setVisibility(View.VISIBLE); else b_z.setVisibility(View.INVISIBLE);
            final Button b_ae = (Button)findViewById(R.id.button_ae); if(s.getInt("B_AE") == 1) b_ae.setVisibility(View.VISIBLE); else b_ae.setVisibility(View.INVISIBLE);
            final Button b_oe = (Button)findViewById(R.id.button_oe); if(s.getInt("B_OE") == 1) b_oe.setVisibility(View.VISIBLE); else b_oe.setVisibility(View.INVISIBLE);
            final Button b_aa = (Button)findViewById(R.id.button_aa); if(s.getInt("B_AA") == 1) b_aa.setVisibility(View.VISIBLE); else b_aa.setVisibility(View.INVISIBLE);
        }
    }

    // start nytt spill
    void nyttSpill(){

        // endre tilstand
        tilstand = SpillTilstand.Kjoring;

        // reset sjekketBokstav
        sjekketBokstav = false;

        // vis alle knapper
        visKnapper();

        // velg nytt ord og generere strek
        ord.nyttOrd(ordliste.generereOrd());
        ord.generereStrek();

        // advarsel hvis alle ordene i ordlista har blitt brukt
        if(ordliste.getAntallValgteOrd() == ordliste.getOrdList().length) {
            // omstokke
            ordliste.stokkeOrdliste();

            // vise advarsel
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle(getResources().getString(R.string.tomOrdlisteTittel));
            ad.setMessage(getResources().getString(R.string.tomOrdlisteTekst));
            ad.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad.show();
        }

        // reset antallSjanser
        antallSjanser = 7;

        // oppdater bilde og labels
        oppdaterBilde();
        oppdaterVinnOgTapLabels();
        oppdaterTekst(ord.getOrdMedMellomrom());
    }

    // oppdater sprak
    void oppdaterSprak(String s){
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration cfg = new Configuration();
        cfg.locale = locale;
        getBaseContext().getResources().updateConfiguration(cfg, getBaseContext().getResources().getDisplayMetrics());
    }

    // oppdatere antall vinn/tap labelene
    void oppdaterVinnOgTapLabels(){
        TextView antVinn = (TextView)findViewById(R.id.labelAntallVinn);
        TextView antTap = (TextView)findViewById(R.id.labelAntallTap);
        antVinn.setText(antallVinn + "");
        antTap.setText(antallTap + "");
    }

    // sjekk bokstav og oppdatere bilde eller erstatte strek "_"
    void sjekkBokstav(char bokstav){
        sjekketBokstav = true;

        if(ord.bokstavFinnes(bokstav)){
            ord.settInnBokstav(bokstav);
            oppdaterTekst(ord.getOrdMedMellomrom());
        }
        else
            henge();

        // sjekk hvis ordet har blitt funnet
        if(ord.getValgtOrd().toLowerCase().equals(ord.getStrekOgBokstaver().toLowerCase()))
            vinnSpill();

        // sjekk hvis sjansene er ferdig
        if(antallSjanser == 0)
            gameover();
    }

    // blir kalt hvis bokstav ikke finnes i ordet
    void henge() {
        antallSjanser--;
        oppdaterBilde();
    }

    // oppdatere bilde nar bruker far feil bokstav
    void oppdaterBilde(){
        ImageView i = (ImageView)findViewById(R.id.bildeView);

        switch (antallSjanser){
            case 7 : i.setImageResource(R.drawable.forste);
                break;
            case 6 : i.setImageResource(R.drawable.annen);
                break;
            case 5 : i.setImageResource(R.drawable.tredje);
                break;
            case 4 : i.setImageResource(R.drawable.fjerde);
                break;
            case 3 : i.setImageResource(R.drawable.femte);
                break;
            case 2 : i.setImageResource(R.drawable.sjette);
                break;
            case 1 : i.setImageResource(R.drawable.syvende);
                break;
            case 0 : i.setImageResource(R.drawable.ottende);
                break;
        }
    }

    // skjuler knappen og blir kalt nar bruker trykker pa bokstav (onClick)
    void skjuleKnapp(Button b) {
        b.setVisibility(View.INVISIBLE);
    }
    
    // vis alle knapper
    void visKnapper(){
        knapp_a.setVisibility(View.VISIBLE);
        knapp_b.setVisibility(View.VISIBLE);
        knapp_c.setVisibility(View.VISIBLE);
        knapp_d.setVisibility(View.VISIBLE);
        knapp_e.setVisibility(View.VISIBLE);
        knapp_f.setVisibility(View.VISIBLE);
        knapp_g.setVisibility(View.VISIBLE);
        knapp_h.setVisibility(View.VISIBLE);
        knapp_i.setVisibility(View.VISIBLE);
        knapp_j.setVisibility(View.VISIBLE);
        knapp_k.setVisibility(View.VISIBLE);
        knapp_l.setVisibility(View.VISIBLE);
        knapp_m.setVisibility(View.VISIBLE);
        knapp_n.setVisibility(View.VISIBLE);
        knapp_o.setVisibility(View.VISIBLE);
        knapp_p.setVisibility(View.VISIBLE);
        knapp_q.setVisibility(View.VISIBLE);
        knapp_r.setVisibility(View.VISIBLE);
        knapp_s.setVisibility(View.VISIBLE);
        knapp_t.setVisibility(View.VISIBLE);
        knapp_u.setVisibility(View.VISIBLE);
        knapp_v.setVisibility(View.VISIBLE);
        knapp_w.setVisibility(View.VISIBLE);
        knapp_x.setVisibility(View.VISIBLE);
        knapp_y.setVisibility(View.VISIBLE);
        knapp_z.setVisibility(View.VISIBLE);

        if(sprak.equals("nb")){
            knapp_ae.setVisibility(View.VISIBLE);
            knapp_oe.setVisibility(View.VISIBLE);
            knapp_aa.setVisibility(View.VISIBLE);
        }
        else{
            knapp_ae.setVisibility(View.INVISIBLE);
            knapp_oe.setVisibility(View.INVISIBLE);
            knapp_aa.setVisibility(View.INVISIBLE);
        }
    }

    // apner "gameover" aktivitetet og sender med valgt ord som "ekstra"
    void gameover(){
        antallTap++;
        sjekketBokstav = false;
        Intent intent = new Intent(this, Gameover.class);
        intent.putExtra("valgtOrdFraHangman",ord.getValgtOrdMedMellomrom());
        startActivity(intent);
    }

    // kalles nar alle bokstaver er funnet og aper "vinn" aktivitet
    void vinnSpill(){
        antallVinn++;
        sjekketBokstav = false;
        Intent intent = new Intent(this, Vinn.class);
        intent.putExtra("valgtOrdFraHangman",ord.getValgtOrdMedMellomrom());
        intent.putExtra("sjanserFraHangman", antallSjanser);
        startActivity(intent);
    }
    
    // oppdaterer labelen pa skjermen med "strek og bokstaver"
    void oppdaterTekst(String s){
        TextView tv = (TextView)findViewById(R.id.labelNoSpacing);
        tv.setText(s);
    }

    // forbererede knapper
    void forberedeKnapper(){

        knapp_a = (Button) findViewById(R.id.button_a);
        knapp_b = (Button) findViewById(R.id.button_b);
        knapp_c = (Button) findViewById(R.id.button_c);
        knapp_d = (Button) findViewById(R.id.button_d);
        knapp_e = (Button) findViewById(R.id.button_e);
        knapp_f = (Button) findViewById(R.id.button_f);
        knapp_g = (Button) findViewById(R.id.button_g);
        knapp_h = (Button) findViewById(R.id.button_h);
        knapp_i = (Button) findViewById(R.id.button_i);
        knapp_j = (Button) findViewById(R.id.button_j);
        knapp_k = (Button) findViewById(R.id.button_k);
        knapp_l = (Button) findViewById(R.id.button_l);
        knapp_m = (Button) findViewById(R.id.button_m);
        knapp_n = (Button) findViewById(R.id.button_n);
        knapp_o = (Button) findViewById(R.id.button_o);
        knapp_p = (Button) findViewById(R.id.button_p);
        knapp_q = (Button) findViewById(R.id.button_q);
        knapp_r = (Button) findViewById(R.id.button_r);
        knapp_s = (Button) findViewById(R.id.button_s);
        knapp_t = (Button) findViewById(R.id.button_t);
        knapp_u = (Button) findViewById(R.id.button_u);
        knapp_v = (Button) findViewById(R.id.button_v);
        knapp_w = (Button) findViewById(R.id.button_w);
        knapp_x = (Button) findViewById(R.id.button_x);
        knapp_y = (Button) findViewById(R.id.button_y);
        knapp_z = (Button) findViewById(R.id.button_z);

        // norske bokstaver
        knapp_ae = (Button) findViewById(R.id.button_ae);
        knapp_oe = (Button) findViewById(R.id.button_oe);
        knapp_aa = (Button) findViewById(R.id.button_aa);
                
        knapp_a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                char bokstav = getResources().getString(R.string.bokstav_a).toUpperCase().charAt(0);
                skjuleKnapp(knapp_a);
                sjekkBokstav(bokstav);
            }
        });
        
        knapp_b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                char bokstav = getResources().getString(R.string.bokstav_b).toUpperCase().charAt(0);
                skjuleKnapp(knapp_b);
                sjekkBokstav(bokstav);
            }
        });

        knapp_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_c).toUpperCase().charAt(0);
                skjuleKnapp(knapp_c);
                sjekkBokstav(bokstav);
            }
        });

        knapp_d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_d).toUpperCase().charAt(0);
                skjuleKnapp(knapp_d);
                sjekkBokstav(bokstav);
            }
        });

        knapp_e.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_e).toUpperCase().charAt(0);
                skjuleKnapp(knapp_e);
                sjekkBokstav(bokstav);
            }
        });

        knapp_f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_f).toUpperCase().charAt(0);
                skjuleKnapp(knapp_f);
                sjekkBokstav(bokstav);
            }
        });

        knapp_g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_g).toUpperCase().charAt(0);
                skjuleKnapp(knapp_g);
                sjekkBokstav(bokstav);
            }
        });

        knapp_h.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_h).toUpperCase().charAt(0);
                skjuleKnapp(knapp_h);
                sjekkBokstav(bokstav);
            }
        });

        knapp_i.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_i).toUpperCase().charAt(0);
                skjuleKnapp(knapp_i);
                sjekkBokstav(bokstav);
            }
        });

        knapp_j.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_j).toUpperCase().charAt(0);
                skjuleKnapp(knapp_j);
                sjekkBokstav(bokstav);
            }
        });

        knapp_k.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_k).toUpperCase().charAt(0);
                skjuleKnapp(knapp_k);
                sjekkBokstav(bokstav);
            }
        });

        knapp_l.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_l).toUpperCase().charAt(0);
                skjuleKnapp(knapp_l);
                sjekkBokstav(bokstav);
            }
        });

        knapp_m.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_m).toUpperCase().charAt(0);
                skjuleKnapp(knapp_m);
                sjekkBokstav(bokstav);
            }
        });

        knapp_n.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_n).toUpperCase().charAt(0);
                skjuleKnapp(knapp_n);
                sjekkBokstav(bokstav);
            }
        });

        knapp_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_o).toUpperCase().charAt(0);
                skjuleKnapp(knapp_o);
                sjekkBokstav(bokstav);
            }
        });

        knapp_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_p).toUpperCase().charAt(0);
                skjuleKnapp(knapp_p);
                sjekkBokstav(bokstav);
            }
        });

        knapp_q.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_q).toUpperCase().charAt(0);
                skjuleKnapp(knapp_q);
                sjekkBokstav(bokstav);
            }
        });

        knapp_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_r).toUpperCase().charAt(0);
                skjuleKnapp(knapp_r);
                sjekkBokstav(bokstav);
            }
        });

        knapp_s.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_s).toUpperCase().charAt(0);
                skjuleKnapp(knapp_s);
                sjekkBokstav(bokstav);
            }
        });

        knapp_t.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_t).toUpperCase().charAt(0);
                skjuleKnapp(knapp_t);
                sjekkBokstav(bokstav);
            }
        });


        knapp_u.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_u).toUpperCase().charAt(0);
                skjuleKnapp(knapp_u);
                sjekkBokstav(bokstav);
            }
        });

        knapp_v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_v).toUpperCase().charAt(0);
                skjuleKnapp(knapp_v);
                sjekkBokstav(bokstav);
            }
        });

        knapp_w.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_w).toUpperCase().charAt(0);
                skjuleKnapp(knapp_w);
                sjekkBokstav(bokstav);
            }
        });

        knapp_x.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_x).toUpperCase().charAt(0);
                skjuleKnapp(knapp_x);
                sjekkBokstav(bokstav);
            }
        });

        knapp_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_y).toUpperCase().charAt(0);
                skjuleKnapp(knapp_y);
                sjekkBokstav(bokstav);
            }
        });

        knapp_z.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_z).toUpperCase().charAt(0);
                skjuleKnapp(knapp_z);
                sjekkBokstav(bokstav);
            }
        });

        // norske bokstaver
        knapp_ae.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_ae).toUpperCase().charAt(0);
                skjuleKnapp(knapp_ae);
                sjekkBokstav(bokstav);
            }
        });

        knapp_oe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_oe).toUpperCase().charAt(0);
                skjuleKnapp(knapp_oe);
                sjekkBokstav(bokstav);
            }
        });

        knapp_aa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                char bokstav = getResources().getString(R.string.bokstav_aa).toUpperCase().charAt(0);
                skjuleKnapp(knapp_aa);
                sjekkBokstav(bokstav);
            }
        });
    }    
}
