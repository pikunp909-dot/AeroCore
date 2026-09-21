package com.aerocore;

import org.junit.Test;
import static org.junit.Assert.*;

public class AeroCoreUnitTest {
    @Test
    public void testVersion() {
        AeroCore core = AeroCore.getInstance();
        assertEquals("1.0.0", core.sdkVersion());
        assertEquals(10000, core.sdkVersionCode());
    }
}
