package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSReceivedMessage;

public interface SMSRecieveListener {

    void onSMSRecieve(SMSReceivedMessage message);

}
