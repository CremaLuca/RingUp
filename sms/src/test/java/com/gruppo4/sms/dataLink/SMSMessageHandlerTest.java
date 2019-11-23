package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino
 */
public class SMSMessageHandlerTest extends Variables {

    private static SMSMessageHandler instance = null;
    protected static final String NO_SEPARATOR_CHAR_MESSAGE = HIDDEN_CHARACTER + VALID_APPLICATION_CODE + MAIN_MESSAGE;
    protected static final String NO_INVISIBLE_CHAR_MESSAGE = VALID_APPLICATION_CODE + SEPARATOR_CHARACTER + MAIN_MESSAGE;
    protected static final String TOO_BIG_APP_CODE_MESSAGE = HIDDEN_CHARACTER + TOO_BIG_APPLICATION_CODE + SEPARATOR_CHARACTER + MAIN_MESSAGE;
    protected static final String LETTERS_APP_CODE_MESSAGE = HIDDEN_CHARACTER + "aaa" + SEPARATOR_CHARACTER + MAIN_MESSAGE;

    @Before
    public void init() {
        instance = instance.getInstance();
    }

    @Test
    public void parseMessage_separatorChar_isAbsent() {
        Assert.assertEquals(null, instance.parseMessage(NO_SEPARATOR_CHAR_MESSAGE, VALID_TELEPHONE_NUMBER));
    }

    @Test
    public void parseMessage_InvisibleChar_isAbsent() {
        Assert.assertEquals(null, instance.parseMessage(NO_INVISIBLE_CHAR_MESSAGE, VALID_TELEPHONE_NUMBER));
    }

    @Test
    public void parseMessage_appID_tooBig() {
        Assert.assertEquals(null, instance.parseMessage(TOO_BIG_APP_CODE_MESSAGE, VALID_TELEPHONE_NUMBER));
    }

    @Test
    public void parseMessage_appID_hasLetters() {
        Assert.assertEquals(null, instance.parseMessage(LETTERS_APP_CODE_MESSAGE, VALID_TELEPHONE_NUMBER));
    }

    @Test
    public void getOutput() {
        Assert.assertEquals(VALID_TEXT_MESSAGE, instance.getOutput(new SMSMessage(VALID_APPLICATION_CODE, VALID_TELEPHONE_NUMBER, MAIN_MESSAGE)));
    }
}