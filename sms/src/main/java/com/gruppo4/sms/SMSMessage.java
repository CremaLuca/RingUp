package com.gruppo4.sms;

public class SMSMessage {

    String numero = "";
    String testo = "";

    //costruttore
    public SMSMessage(String numTel, String txt){
        numero = numTel;
        testo = txt;
    }

    //restituisce il numero di telefono
    public String getNumber(){
        return numero;
    }

    //restituisce il testo del messaggio
    public String getText(){
        return testo;
    }
}
