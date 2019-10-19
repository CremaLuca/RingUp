package com.gruppo4.sms;

import  com.gruppo4.sms.exceptions.*;

public class SMSMessage {

    private String telephoneNumber;
    private String messageText;
    private int messageCode;

    public static final int MAX_MESSAGE_LENGTH = SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH * Byte.MAX_VALUE; //This is because package number cannot exceed three characters
    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;

    /**
     * A set of states for the message validity tests
     */
    public enum MessageTextState{
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    /**
     * A set of states for the phone number validity tests
     */
    public enum TelephoneNumberState {
        TELEPHONE_NUMBER_VALID,
        TELEPHONE_NUMBER_TOO_SHORT,
        TELEPHONE_NUMBER_TOO_LONG,
        TELEPHONE_NUMBER_NO_COUNTRY_CODE,
        TELEPHONE_NUMBER_NOT_A_NUMBER
    }

    public SMSMessage(String telephoneNumber, String messageText, int messageCode) throws InvalidSMSMessageException, InvalidTelephoneNumberException{

        TelephoneNumberState telephoneNumberState = checkTelephoneNumber(telephoneNumber);
        if(telephoneNumberState == TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            this.telephoneNumber = telephoneNumber;
        else
            throw new InvalidTelephoneNumberException("Telephone number invalid, reason: " + telephoneNumberState,telephoneNumberState);

        MessageTextState messageTextState = checkMessageText(messageText);
        if(messageTextState == MessageTextState.MESSAGE_TEXT_VALID)
            this.messageText = messageText;
        else
            throw new InvalidSMSMessageException("The message text is invalid, reason: " + messageTextState, messageTextState);

        this.messageCode = messageCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public SMSPacket[] getPackets(){
        //Calculate the number of packets we have to send in order to send the full message
        byte packetsCount = (byte)(Math.floor(messageText.length() / MAX_MESSAGE_LENGTH) + 1);
        SMSPacket[] packets = new SMSPacket[packetsCount];
        //The SMSController must be initialized (should already be, this method is for the send() method)
        int applicationCode = SMSController.getApplicationCode();
        String subMessage;
        for(byte i = 0; i < packetsCount; i++){
            //We either choose the maximum length for a message or the actual length of the text
            //ES: "hello" substr will be from 0 to 5
            //ES: a string of 180 characters will have i=0) substr(0,159), i=1)substr(160,180)
            int finalCharacter = Math.min((((i+1)* MAX_MESSAGE_LENGTH) - 1), (messageText.length() - i*MAX_MESSAGE_LENGTH));
            subMessage = messageText.substring(i*MAX_MESSAGE_LENGTH, finalCharacter);
            packets[i] = new SMSPacket(applicationCode,messageCode,i + 1,packetsCount,subMessage);
        }
        return  packets;
    }

    /**
     *  Checks if the message is valid
     * @param messageText the text to check
     * @return The state of the message after the tests
     */
    public static MessageTextState checkMessageText(String messageText) {
        if(messageText.length() > MAX_MESSAGE_LENGTH){
            return MessageTextState.MESSAGE_TEXT_TOO_LONG;
        }
        //TODO : Controllare che non abbia caratteri proibiti
        return MessageTextState.MESSAGE_TEXT_VALID;
    }

    /**
     * Checks if the phone number is valid
     * @param telephoneNumber the phone number to check
     * @return The state of the telephone number after the tests
     */
    public static TelephoneNumberState checkTelephoneNumber(String telephoneNumber){
        //Check if the number is shorter than the MAX.
        if(telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH){
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if(telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH){
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if(!telephoneNumber.substring(1,telephoneNumber.length()-1).matches("[0-9]+")){
            return TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER;
        }
        //Check if there is a country code.
        if(!telephoneNumber.substring(0,1).equals("+")){
            return TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE;
        }
        //If it passed all the tests we are sure the number is valid.
        return TelephoneNumberState.TELEPHONE_NUMBER_VALID;
    }

}
