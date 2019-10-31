package com.gruppo4.sms.dataLink.listeners;

import com.gruppo4.sms.dataLink.SMSMessage;

public interface SMSReceivedListener {

    void onSMSReceived(SMSMessage message);

}
