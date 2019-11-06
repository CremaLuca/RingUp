package com.gruppo4.sms.dataLink;

public class Variables {

    /**
     * Variables used by tests
     */

    //Class objectss
    protected static SMSMessage smsMessage = null;
    protected static SMSPeer peer = null;
    protected static SMSHandler smsHandler = null;

    //Text Messages
    protected static final String VALID_TEXT_MESSAGE = "Test message";
    protected static final int MAX_MSG_TEXT_LEN = 139860;
    protected static final int MAX_PACKET_TEXT_LEN = 140;
    protected static final String TOO_LONG_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN * 2]).replace('\0', ' ');
    protected static final String MAX_LENGTH_TEXT_MESSAGE_P1 = new String(new char[MAX_MSG_TEXT_LEN + 1]).replace('\0', ' ');
    protected static final String MAX_LENGTH_TEXT_MESSAGE = new String(new char[MAX_MSG_TEXT_LEN]).replace('\0', ' ');
    protected static final String MAX_LENGTH_PACKET_MESSAGE = new String(new char[MAX_PACKET_TEXT_LEN]).replace('\0', ' ');
    protected static final String MAX_LENGTH_PACKET_MESSAGE_P1 = new String(new char[MAX_PACKET_TEXT_LEN + 1]).replace('\0', ' ');  //P1 = Plus 1
    protected static final String EMPTY_TEXT_MESSAGE = "";

    //Telephone Numbers
    protected static final String VALID_TELEPHONE_NUMBER = "+391111111111";
    protected static final String TOO_SHORT_TELEPHONE_NUMBER = "+39111";
    protected static final String TOO_LONG_TELEPHONE_NUMBER = "+39111111111111111111";
    protected static final String NO_COUNTRY_CODE_TELEPHONE_NUMBER = "1111111111";
    protected static final String LETTERS_TELEPHONE_NUMBER = "+391111111ABC";
    protected static final String EMPTY_TELEPHONE_NUMBER = "";

    //Packets
    protected static final int VALID_APPLICATION_CODE = 1;
    protected static final int VALID_MESSAGE_ID = 1;
    protected static final int VALID_PACKET_NUMBER = 1;
    protected static final int VALID_TOTAL_PACKET_NUMBER = 1;
    protected static final String VALID_PACKET_DATA = "[1_1_1_1_Test message]"; //Used VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE
    protected static final int TOO_BIG_APPLICATION_CODE = 1000;
    protected static final int TOO_SMALL_APPLICATION_CODE = -1;
    protected static final int TOO_BIG_MESSAGE_ID = 1000;
    protected static final int TOO_BIG_PACKET_NUMBER = 1000;
    protected static final int TOO_BIG_TOTAL_PACKET_NUMBER = 1000;

    //Messages
    protected static final String SHOULD_NOT = "Should not have thrown an exception";
    protected static final String SHOULD_THROW = "Should have thrown an exception";

    //Instantiated objects
    protected static final SMSPacket SMS_PACKET = new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
    protected static final SMSPacket SMS_PACKET_2 = new SMSPacket(VALID_APPLICATION_CODE, VALID_MESSAGE_ID, VALID_PACKET_NUMBER, VALID_TOTAL_PACKET_NUMBER, VALID_TEXT_MESSAGE);
    protected static final SMSMessage SMS_MESSAGE = new SMSMessage(new SMSPeer(VALID_TELEPHONE_NUMBER), SMS_PACKET);

}
