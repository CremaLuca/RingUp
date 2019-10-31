package com.gruppo4.sms.dataLink;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSPacketUnitTest {

    private SMSPacket packet;

    @Before
    public void init() {
        packet = new SMSPacket(333, 222, 5, 10, "Message test");
    }

    @Test
    public void parse_packet_wrongFormat() {
        // Assert.assertNull(SMSPacket.parseSMSPacket("This is definitely " + SMSPacket.SEPARATOR + "a wrong format" + SMSPacket.SEPARATOR + "whatever the format will be"));
    }

    //Normal tests

    @Test
    public void application_code_isEquals() {
        Assert.assertEquals(packet.getApplicationCode(), 333);
    }

    @Test
    public void message_code_isEquals() {
        //Assert.assertEquals(packet.getMessageCode(), 222);
    }

    @Test
    public void packet_number_isEquals() {
        Assert.assertEquals(packet.getPacketNumber(), 5);
    }

    @Test
    public void total_number_isEquals() {
        Assert.assertEquals(packet.getTotalNumber(), 10);
    }

    @Test
    public void message_isEquals() {
        Assert.assertEquals(packet.getMessageText(), "Message test");
    }

    //Wrong format tests


    @Test
    public void application_code_tooBig() {
        try {
            packet = new SMSPacket(1000, 1, 2, 3, "Test");
            Assert.fail("Should have thrown IllegalArgumentException exception");
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_code_tooBig() {
        try {
            packet = new SMSPacket(1, 1000, 2, 3, "Test");
            Assert.fail("Should have thrown IllegalArgumentException exception");
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void packet_number_tooBig() {
        try {
            packet = new SMSPacket(1, 2, 1000, 3, "Test");
            Assert.fail("Should have thrown IllegalArgumentException exception");
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void total_number_tooBig() {
        try {
            packet = new SMSPacket(1, 2, 3, 1000, "Test");
            Assert.fail("Should have thrown IllegalArgumentException exception");
        } catch (Exception e) {
            //Success
        }
    }

    @Test
    public void message_tooLong() {
        try {
            packet = new SMSPacket(111, 222, 333, 555, "Too long message to fit in a single packet (SMS) because it should exceed the maximum characters limit -AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            Assert.fail("Should have thrown IllegalArgumentException exception");
        } catch (Exception e) {
            //Success
        }
    }

    /**
     * Checks if an exception is thrown if the packet number is greater than the total number
     */
    @Test
    public void packet_numbering_isWrong() {
        try {
            packet = new SMSPacket(1, 2, 10, 8, "Test");
            Assert.fail("Should have thrown IllegalStateException exception");
        } catch (Exception e) {
            //Success
        }
    }

}
