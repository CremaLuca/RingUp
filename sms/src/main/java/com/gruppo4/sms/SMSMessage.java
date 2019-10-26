package com.gruppo4.sms;

import android.util.Log;

import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;

public class SMSMessage {

    public static final int MAX_MESSAGE_LENGTH = SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH * 999; //This is because package number cannot exceed three characters
    public static final int MAX_TELEPHONE_NUMBER_LENGTH = 20;
    public static final int MIN_TELEPHONE_NUMBER_LENGTH = 7;
    private String telephoneNumber;
    private String messageText;
    private int messageCode;

    /**
     * Wrap for a text message, used to check the parameters validity
     *
     * @param telephoneNumber a valid telephone number to send the message to
     * @param messageText     the text to be sent via one or multiple SMS depending on its length
     * @throws InvalidSMSMessageException      if the message is longer than MAX_MESSAGE_LENGTH
     * @throws InvalidTelephoneNumberException if the number is longer than MAX_TELEPHONE_NUMBER_LENGTH or shorter than
     *                                         MIN_TELEPHONE_NUMBER_LENGTH or misses country code or is not a number
     */
    public SMSMessage(String telephoneNumber, String messageText, int messageCode) throws InvalidSMSMessageException, InvalidTelephoneNumberException {

        TelephoneNumberState telephoneNumberState = checkTelephoneNumber(telephoneNumber);
        if (telephoneNumberState == TelephoneNumberState.TELEPHONE_NUMBER_VALID)
            this.telephoneNumber = telephoneNumber;
        else
            throw new InvalidTelephoneNumberException("Telephone number invalid, reason: " + telephoneNumberState, telephoneNumberState);

        MessageTextState messageTextState = checkMessageText(messageText);
        if (messageTextState == MessageTextState.MESSAGE_TEXT_VALID)
            this.messageText = messageText;
        else
            throw new InvalidSMSMessageException("The message text is invalid, reason: " + messageTextState, messageTextState);

        if (!checkMessageCode(messageCode))
            throw new IllegalArgumentException("Message code is out of bounds");

        this.messageCode = messageCode;
    }

    public static boolean checkMessageCode(int messageCode) {
        return messageCode > -99 && messageCode < 1000;
    }

    /**
     * Checks if the message is valid
     *
     * @param messageText the text to check
     * @return The state of the message after the tests
     */
    public static MessageTextState checkMessageText(String messageText) {
        if (messageText.length() > MAX_MESSAGE_LENGTH) {
            return MessageTextState.MESSAGE_TEXT_TOO_LONG;
        }
        return MessageTextState.MESSAGE_TEXT_VALID;
    }

    /**
     * Checks if the phone number is valid
     *
     * @param telephoneNumber the phone number to check
     * @return The state of the telephone number after the tests
     */
    public static TelephoneNumberState checkTelephoneNumber(String telephoneNumber) {
        //Check if the number is shorter than the MAX.
        if (telephoneNumber.length() > MAX_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG;
        }
        //Check if the number is longer than the MIN.
        if (telephoneNumber.length() < MIN_TELEPHONE_NUMBER_LENGTH) {
            return TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT;
        }
        //Check if it's actually a number and doesn't contain anything else
        //First we have to remove the "+"
        if (!telephoneNumber.substring(1, telephoneNumber.length() - 1).matches("[0-9]+")) {
            return TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER;
        }
        //Check if there is a country code.
        if (!telephoneNumber.substring(0, 1).equals("+")) {
            return TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE;
        }
        //If it passed all the tests we are sure the number is valid.
        return TelephoneNumberState.TELEPHONE_NUMBER_VALID;
    }

    /**
     * Telephone Number is the number this message has to be sent to or has been already sent
     *
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Message Text is the content of the message that can be sent via one or multiple SMS
     *
     * @return the message
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Message Code is an identifier for the service that will send or use the message content
     * @return the message code
     */
    public int getMessageCode() {
        return messageCode;
    }

    /**
     * Splits the message in packets that can be sent via SMS
     * @return the array of packets to be sent via SMS
     */
    SMSPacket[] getPackets() {
        //Calculate the number of packets we have to send in order to send the full message
        int packetsCount = (int) (Math.floor(messageText.length() / SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH) + 1);
        Log.d("SMSMessage", "I have to send " + packetsCount + " messages fo a message " + messageText.length() + " characters long");
        SMSPacket[] packets = new SMSPacket[packetsCount];
        //The SMSController must be initialized (should already be, this method is for the send() method)
        int applicationCode = SMSController.getApplicationCode();
        String subMessage;
        for (int i = 0; i < packetsCount; i++) {
            //We either choose the maximum length for a message or the actual length of the text
            //ES: "hello" substr will be from 0 to 5
            //ES: a string of 180 characters will have i=0) substr(0,159), i=1)substr(160,180)
            int finalCharacter = Math.min((((i + 1) * SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH) - 1), messageText.length());
            Log.d("SMSMessage", "Substring from " + i * SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH + " to " + finalCharacter);
            subMessage = messageText.substring(i * SMSPacket.PACKAGE_MESSAGE_MAX_LENGTH, finalCharacter);
            packets[i] = new SMSPacket(applicationCode, messageCode, i + 1, packetsCount, subMessage);
        }
        return packets;
    }

    /**
     * A set of states for the message validity tests
     */
    public enum MessageTextState {
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

    /**
     * A set of states for the message
     */
    public enum SentState {
        NOT_SENT,
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

}
