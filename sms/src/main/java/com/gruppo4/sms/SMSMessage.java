package com.gruppo4.sms;

public class SMSMessage {

    private String number = "";
    private String text = "";

    /*
     *constructor
     *@param a String as phone number to which we send the message and a String as the text of the message
     *@return 
     */
    public SMSMessage(String phoneNum, String smsTxt){
        number = phoneNum;
        text = smsTxt;
    }

    /*
     *Returns the phone number to which we send the message
     *@param no parameters
     *@return the phone number
     */
    public String getPhoneNumber(){
        return number;
    }

    /*
     *Returns the message text
     *@param no parameters
     *@return the message text
     */
    public String getTextMessage(){
        return text;
    }
}
