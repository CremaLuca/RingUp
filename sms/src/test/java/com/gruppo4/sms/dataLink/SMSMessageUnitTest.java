package com.gruppo4.sms.dataLink;

import com.gruppo4.sms.dataLink.exceptions.InvalidSMSMessageException;
import com.gruppo4.sms.dataLink.exceptions.InvalidTelephoneNumberException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSMessageUnitTest {

    private static final String TEST_MESSAGE = "This is a test message";
    private static final String TEST_NUMBER = "+391111111111";

    private SMSMessage message;

    @Before
    public void init() {
        SMSController.init(null, 1);
        try {
            message = new SMSMessage(TEST_NUMBER, TEST_MESSAGE);
        } catch (Exception e) {
            Assert.fail("Should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void constructor_numberTooShort() {
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
    public void constructor_numberTooLong() {
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
    public void constructor_hasCountryCode() {
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
    public void constructor_phoneNumberIsOnlyNumbers() {
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
    public void constructor_messageIsNotTooLong() {
        String messageTooLong = "";
        for(int i=0;i<2500;i++){
            messageTooLong += "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }
        try {
            message = new SMSMessage(TEST_NUMBER, messageTooLong);
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
    public void constructor_typicalData() {
        try {
            message = new SMSMessage(TEST_NUMBER, TEST_MESSAGE);
        }catch(Exception e){
            Assert.fail("Should not have thrown an exception");
        }
    }

    @Test
    public void getTelephoneNumber_hasSameTelephoneNumber() {
        Assert.assertEquals(message.getTelephoneNumber(), TEST_NUMBER);
    }

    @Test
    public void getMessage_hasSameMessage() {
        Assert.assertEquals(message.getMessage(), TEST_MESSAGE);
    }

}
