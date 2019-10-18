package com.gruppo4.sms;

import android.telephony.SmsManager;

public class SMSController {

    Message message = new Message();

    //Scrittura messaggio
    public void writeMessage(String mex){
        message.setMessage(mex);
    }

    //Scrittura numero di telefono
    public void writeNumber(String num){
        message.setNumber(num);
    }

    //Invio del messaggio
    public void sendMessage(Message message){

        SmsManager.getDefault().sendTextMessage(message.getNumber(),null,message.getMessage(),null,null);

    }

    public void addOnReceiveListener(Message message){


    }

}

class Message{

    String text_message;
    String tel_number;

    Message(){}

    public void setNumber(String number){
        tel_number = number;
    }

    public void setMessage(String message){
        text_message = message;
    }

    public String getNumber(){
        return tel_number;
    }

    public String getMessage(){
        return text_message;
    }

}
