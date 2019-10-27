package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSController.SentState;

public interface SMSSentListener {

    void onSMSSent(SentState sentState);

}
