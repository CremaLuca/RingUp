package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSMessage;

public interface SMSSentListener {
    void onSMSSent(SMSMessage message);
}
