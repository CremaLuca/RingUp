package com.gruppo4.RingApplication.structure;

import com.eis.smslibrary.SMSPeer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RingCommandTest {

    RingCommand ringCommand = null;
    private static final String VALID_NUMBER = "+391111111111";
    private static final String VALID_PASSWORD = "password";
    private static final String EMPTY_PASSWORD = "";
    private static final String ONLY_NUMBERS_PASSWORD = "123456789";
    private static final String SHOULD_NOT = "It should not have thrown an exception";

    @Before
    public void init(){
        ringCommand = new RingCommand(new SMSPeer(VALID_NUMBER), VALID_PASSWORD);
    }

    @Test
    public void getSender() {
        Assert.assertEquals(VALID_NUMBER, ringCommand.getPeer().toString());
    }

    @Test
    public void getPassword() {
        Assert.assertEquals(VALID_PASSWORD, ringCommand.getPassword());
    }

    @Test
    public void password_isEmpty(){
        try{
            ringCommand = new RingCommand(new SMSPeer(VALID_NUMBER), EMPTY_PASSWORD);
        }catch(Exception e){
            Assert.fail(SHOULD_NOT);
        }
    }

    @Test
    public void password_hasOnlyNumbers(){
        try{
            ringCommand = new RingCommand(new SMSPeer(VALID_NUMBER), ONLY_NUMBERS_PASSWORD);
        }catch(Exception e){
            Assert.fail(SHOULD_NOT);
        }
    }
}