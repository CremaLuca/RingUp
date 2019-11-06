package com.gruppo4.sms.dataLink;

import org.junit.Assert;
import org.junit.Test;

public class SMSPeerTest extends Variables{

    @Test
    public void checkPhoneNumber_telephoneNumber_isTooShort() {
        Assert.assertEquals(peer.checkPhoneNumber(TOO_SHORT_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT);
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_isTooLong() {
        Assert.assertEquals(peer.checkPhoneNumber(TOO_LONG_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG);
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_missingCountryCode() {
        Assert.assertEquals(peer.checkPhoneNumber(NO_COUNTRY_CODE_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE);
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_hasLetters() {
        Assert.assertEquals(peer.checkPhoneNumber(LETTERS_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER);
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_isEmpty() {
        Assert.assertEquals(peer.checkPhoneNumber(EMPTY_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT);
    }

    @Test
    public void staticCheckPhoneNumber_telephoneNumber_isValid() {
        Assert.assertEquals(peer.checkPhoneNumber(VALID_TELEPHONE_NUMBER), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID);
    }

    @Test
    public void NOTstaticCheckPhoneNumber_telephoneNumber_isValid() {
        Assert.assertEquals(new SMSPeer(VALID_TELEPHONE_NUMBER).checkPhoneNumber(), SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID);
    }
}
