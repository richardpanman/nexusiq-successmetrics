package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
public class DbRowStrTest {
    @Test
    void testToString() {
        DbRowStr dbRowStr = new DbRowStr("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        assertEquals(
                "DbRowStr [pointA=A, pointB=B, pointC=C, pointD=D, pointE=E, pointF=F]",
                dbRowStr.toString());
        ;
    }
}
