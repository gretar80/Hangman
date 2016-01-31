/**
 * Gretar AEvarsson
 * gretar80@gmail.com
 * © 2016
 */

package com.example.s198586.hangman;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Collections;

public class Ordliste implements Parcelable {
    private String[] ordList;            // array som inneholder alle ordene
    private int antallValgteOrd = 0;     // teller for hvor mange ord har blitt valgt denne spilleokten

    // konstruktor
    public Ordliste(Context context){
        // liste med ordene, som vi stokker og velger ut av lista
        ordList = context.getResources().getStringArray(R.array.listOrd);
        stokkeOrdliste();
    }

    // getters og setters
    public String[] getOrdList() {
        return ordList;
    }

    public int getAntallValgteOrd() {
        return antallValgteOrd;
    }

    public void setAntallValgteOrd(int t) {
        antallValgteOrd = t;
    }

    // stokke ordlisten
    public void stokkeOrdliste(){
        Collections.shuffle(Arrays.asList(ordList));
        setAntallValgteOrd(0);
    }

    String generereOrd(){
        System.out.println("ANT:" + antallValgteOrd);
        String ord = ordList[antallValgteOrd];
        antallValgteOrd++;
        return ord;
    }

    // brukt for lagring til Bundle for 'saveInstanceState' metoden
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(ordList);
        dest.writeInt(antallValgteOrd);
    }

    public static final Parcelable.Creator<Ordliste> CREATOR = new Parcelable.Creator<Ordliste>() {
        public Ordliste createFromParcel(Parcel in) {
            return new Ordliste(in);
        }

        public Ordliste[] newArray(int size) {
            return new Ordliste[size];
        }
    };

    private Ordliste(Parcel in){
        ordList = in.createStringArray();
        antallValgteOrd = in.readInt();
    }


}
