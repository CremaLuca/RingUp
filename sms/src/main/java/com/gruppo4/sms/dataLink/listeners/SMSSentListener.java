package com.gruppo4.sms.dataLink.listeners;

import com.gruppo4.sms.dataLink.SMSHandler.SentState;
import com.gruppo4.sms.dataLink.SMSMessage;

public interface SMSSentListener {

    void onSMSSent(SMSMessage message, SentState sentState);

}
