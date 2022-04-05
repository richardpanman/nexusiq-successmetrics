package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
public class PayloadItemTest {
    @Test
    void testNormalPayload() {
        PayloadItem payload = new PayloadItem("payloadItem");
        assertEquals("payloadItem", payload.getItem());
        assertEquals(true, payload.isExists());
    }

    @Test
    void testTrimmedPayload() {
        PayloadItem payload = new PayloadItem(" payloadItem ");
        assertEquals("payloadItem", payload.getItem());
        assertEquals(true, payload.isExists());
    }

    @Test
    void testEmptyPayload() {
        PayloadItem payload = new PayloadItem("");
        assertEquals("", payload.getItem());
        assertEquals(false, payload.isExists());
    }
}
