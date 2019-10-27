package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSMessage;

public interface SMSReceivedListener {

    void onSMSReceived(SMSMessage message);

}
