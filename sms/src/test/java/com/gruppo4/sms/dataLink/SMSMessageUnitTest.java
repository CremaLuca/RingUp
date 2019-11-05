package com.gruppo4.sms.dataLink;

import android.content.Context;
import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SMSMessageUnitTest {

    @Test
    public void phone_number_isLongEnough(){
        try {
            String
            message = new SMSMessage(context, new SMSPeer("+39111"), "Test message");
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }catch(InvalidTelephoneNumberException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }
    }

    @Test
    public void phone_number_isShortEnough(){
        try {
            message = new SMSMessage(context, new SMSPeer("+39111111111111111111111111"), "Test message");
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }catch(InvalidTelephoneNumberException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }
    }

    @Test
    public void phone_number_hasCountryCode(){
        try {
            message = new SMSMessage(context, new SMSPeer("111111111"), "Test message");
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }catch(InvalidTelephoneNumberException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }
    }

    @Test
    public void phone_number_hasNoLetters(){
            try {
            message = new SMSMessage(context, new SMSPeer("+11a1b11c1"), "Test message");
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }catch(InvalidTelephoneNumberException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }
    }

    @Test
    public void message_text_isShortEnough(){
        String messageTooLong = "";
        for(int i=0;i<2500;i++){
            messageTooLong += "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }
        try {
            message = new SMSMessage(context, peer, messageTooLong);
            Assert.fail("Should have thrown InvalidSMSMessageException exception");
        }catch(InvalidSMSMessageException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidSMSMessageException exception");
        }
    }

    /**
     * Checks whether a normal message would send an exception or not
     */
    @Test
    public void sms_isNormalMessageOk(){
        try {
            message = new SMSMessage(context, peer, "This is a test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
    }

    @Test
    public void sms_textHasOnlyNumbers(){
        try{
            message = new SMSMessage(context, peer, "3514654254252415");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
    }
}
