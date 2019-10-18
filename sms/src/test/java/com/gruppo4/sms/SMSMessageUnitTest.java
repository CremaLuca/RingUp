package com.gruppo4.sms;

import com.gruppo4.sms.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.exceptions.InvalidTelephoneNumberException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SMSMessageUnitTest {

    SMSMessage message;

    @Before
    public void init() {

    }

    @Test
    public void phone_number_isLongEnough(){
        try {
            message = new SMSMessage("+39111", "Test message");
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
            message = new SMSMessage("+39111111111111111111111111", "Test message");
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
            message = new SMSMessage("111111111", "Test message");
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
            message = new SMSMessage("+11a1b11c1", "Test message");
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }catch(InvalidTelephoneNumberException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidTelephoneNumberException exception");
        }
    }

    @Test
    public void message_text_isShortEnough(){
        try {
            message = new SMSMessage("+391111111111", "Test message that should be too long for the 160 characters limit for normal SMS messages and that should not be able to be sent with a single message and requires multiple ones to be fully sent");
            Assert.fail("Should have thrown InvalidSMSMessageException exception");
        }catch(InvalidSMSMessageException e){
            //Success
        }catch(Exception e){
            Assert.fail("Should have thrown InvalidSMSMessageException exception");
        }
    }

    @Test
    public void sms_isNormalMessageOk(){
        try {
            message = new SMSMessage("+391111111111", "This is a test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
    }

    @Test
    public void sms_hasSameTelephoneNumber(){
        try {
            message = new SMSMessage("+391111111111", "This is a test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
        Assert.assertEquals(message.getTelephoneNumber(),"+391111111111");
    }

    @Test
    public void sms_hasSameText(){
        try {
            message = new SMSMessage("+391111111111", "This is a test message");
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
        Assert.assertEquals(message.getMessageText(),"This is a test message");
    }
}
