package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSPeerUnitTest {

    private static SMSPeer peer = null;
    private static final String TOO_SHORT_TELEPHON_NUMBER = "+39111";
    private static final String TOO_LONG_TELEPHON_NUMBER = "+39111111111111111111";
    private static final String NO_COUNTRY_CODE_TELEPHON_NUMBER = "1111111111";
    private static final String LETTERS_TELEPHON_NUMBER = "+391111111ABC";
    private static final String VALID_TELEPHON_NUMBER = "+391111111111";

    @Test
    public void checkPhoneNumber_telephonNumber_isTooShort() {
        Assert.assertEquals(peer.checkPhoneNumber(TOO_SHORT_TELEPHON_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT);
    }

    @Test
    public void checkPhoneNumber_telephonNumber_isTooLong() {
        Assert.assertEquals(peer.checkPhoneNumber(TOO_LONG_TELEPHON_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG);
    }

    @Test
    public void checkPhoneNumber_telephonNumber_missingCountryCode() {
        Assert.assertEquals(peer.checkPhoneNumber(NO_COUNTRY_CODE_TELEPHON_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE);
    }

    @Test
    public void checkPhoneNumber_telephonNumber_hasLetters() {
        Assert.assertEquals(peer.checkPhoneNumber(LETTERS_TELEPHON_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER);
    }

    @Test
    public void staticCheckPhoneNumber_telephonNumber_isValid() {
        Assert.assertEquals(peer.checkPhoneNumber(VALID_TELEPHON_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID);
    }

    @Test
    public void NOTstaticCheckPhoneNumber_telephonNumber_isValid() {
        Assert.assertEquals(new SMSPeer(VALID_TELEPHON_NUMBER).checkPhoneNumber(), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID);
    }
}
