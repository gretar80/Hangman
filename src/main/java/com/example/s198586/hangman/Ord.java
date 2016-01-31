/**
 * Gretar AEvarsson
 * gretar80@gmail.com
 * © 2016
 */

package com.example.s198586.hangman;

public class Ord {

    // variabler
    private String valgtOrd;                    // gemmer ord som er valgt fra arrays.xml
    private String strekOgBokstaver;            // viser strek _ for skjulte bokstaver, ellers selve bokstaven
    private String ordMedMellomrom;             // setter mellomrom mellom staver for visning av 'strekOgBokstaver'
    private String valgtOrdMedMellomrom = "";   // setter mellomrom mellom staver for visning av 'valgtord'

    // konstruktor
    public Ord(String ord){
        // initialisere
        valgtOrd = ord;
        strekOgBokstaver = "";
        ordMedMellomrom = "";

        for(int i = 0; i < valgtOrd.length(); i++)
            valgtOrdMedMellomrom += valgtOrd.charAt(i) + " ";

        generereStrek();
    }

    // velg nytt ord
    void nyttOrd(String ord){
        valgtOrd = ord;
        valgtOrdMedMellomrom = "";
        for(int i = 0; i < valgtOrd.length(); i++)
            valgtOrdMedMellomrom += valgtOrd.charAt(i) + " ";
    }

    public String getValgtOrd(){
        return valgtOrd;
    }

    public String getStrekOgBokstaver(){
        return strekOgBokstaver;
    }

    public String getOrdMedMellomrom(){
        return ordMedMellomrom;
    }

    public String getValgtOrdMedMellomrom(){
        return valgtOrdMedMellomrom;
    }

    public void setOrdMedMellomrom(String ordMedMellomrom) {
        this.ordMedMellomrom = ordMedMellomrom;
    }

    public void setStrekOgBokstaver(String strekOgBokstaver) {
        this.strekOgBokstaver = strekOgBokstaver;
    }

    public void setValgtOrd(String valgtOrd) {
        this.valgtOrd = valgtOrd;
    }

    public void setValgtOrdMedMellomrom(String valgtOrdMedMellomrom) {
        this.valgtOrdMedMellomrom = valgtOrdMedMellomrom;
    }

    // generer samme antall strek _ og bokstaver i ordet
    void generereStrek(){
        strekOgBokstaver = "";
        for(int i = 0; i < valgtOrd.length(); i++)
            strekOgBokstaver = strekOgBokstaver + "_";

        ordMedMellomrom = "";
        for(int i = 0; i < valgtOrd.length()-1; i++)
            ordMedMellomrom = ordMedMellomrom + "_ ";
        ordMedMellomrom += "_";
    }

    // sjekker hvis bokstav finnes i ordet
    boolean bokstavFinnes(char bokstavSomSkalSjekke){
        // sjekk alle bokstaver i valgte ordet
        for(int i = 0; i < valgtOrd.length(); i++) {
            // sjekk bokstavene i valgte ordet
            char bokstavValgtOrd = valgtOrd.toUpperCase().charAt(i);

            if(bokstavSomSkalSjekke == bokstavValgtOrd)
                return true;
        }
        return false;
    }

    // erstatter strek _ med bokstav
    void settInnBokstav(char bokstav){
        // sjekker alle bokstavene i valgte ordet
        for(int i = 0; i < valgtOrd.length(); i++){
            char c = valgtOrd.toUpperCase().charAt(i);

            // hvis gjettet bokstav er i valgte ordet, erstatter vi strek _ med selve bokstaven
            if(c == bokstav)
                strekOgBokstaver = strekOgBokstaver.substring(0, i) + c + strekOgBokstaver.substring(i + 1, strekOgBokstaver.length());
        }

        ordMedMellomrom = "";
        for(int i = 0; i < strekOgBokstaver.length(); i++)
            ordMedMellomrom += strekOgBokstaver.charAt(i) + " ";
    }
}
