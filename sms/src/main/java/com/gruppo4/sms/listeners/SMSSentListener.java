package com.gruppo4.sms.listeners;

import com.gruppo4.sms.SMSController;

public interface SMSSentListener {
    void onSMSSent(SMSController.SMSSentState state);
}
