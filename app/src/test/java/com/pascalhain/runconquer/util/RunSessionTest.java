package com.pascalhain.runconquer.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RunSessionTest {

    @Test
    public void addsDistanceAfterMultipleLocations() {
        RunSession session = new RunSession();
        session.seedLocation(0.0, 0.0);
        session.addLocation(0.009, 0.0);
        double km = session.getTotalDistanceKm();
        assertTrue(km > 0.9);
    }

    @Test
    public void resetClearsDistance() {
        RunSession session = new RunSession();
        session.seedLocation(0.0, 0.0);
        session.addLocation(0.009, 0.0);
        session.reset();
        assertEquals(0.0, session.getTotalDistanceKm(), 0.0001);
    }
}

