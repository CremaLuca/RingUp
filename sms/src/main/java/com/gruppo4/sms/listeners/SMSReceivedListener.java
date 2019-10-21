package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSReceivedMessage;

public interface SMSReceivedListener {

    void onSMSReceived(SMSReceivedMessage message);

}
