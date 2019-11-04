package com.gruppo4.sms.dataLink.listeners;

import com.gruppo4.sms.dataLink.SMSMessage;
import com.gruppo4.sms.dataLink.SMSMessage.SentState;

public interface SMSSentListener {

    void onSMSSent(SMSMessage message, SentState sentState);

}
