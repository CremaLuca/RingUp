package com.gruppo4.sms.interfaces;

import com.gruppo4.sms.SMSController;
import com.gruppo4.sms.SMSMessage;

public interface SMSSentListener {

    void onSentReceived(SMSMessage message, SMSController.SentStatus status);

}
