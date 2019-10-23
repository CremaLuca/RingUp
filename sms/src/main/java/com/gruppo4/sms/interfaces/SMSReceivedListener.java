package com.gruppo4.sms.interfaces;

import com.gruppo4.sms.SMSMessage;

public interface SMSReceivedListener {

    void onMessageReceived(SMSMessage message);

}
