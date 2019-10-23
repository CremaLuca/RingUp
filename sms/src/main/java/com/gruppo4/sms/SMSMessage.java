package com.gruppo4.sms;

public class SMSMessage {
    String text_message;
    String tel_number;

    public SMSMessage(String telephoneNumber, String text){
        text_message = text;
        tel_number = telephoneNumber;
    }

    public String getTelephoneNumber(){
        return tel_number;
    }

    public String getMessage(){
        return text_message;
    }

}
