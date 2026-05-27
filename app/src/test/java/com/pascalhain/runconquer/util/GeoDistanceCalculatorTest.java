package com.pascalhain.runconquer.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeoDistanceCalculatorTest {

    @Test
    public void distanceKm_isAboutOneKm() {
        double km = GeoDistanceCalculator.distanceKm(0.0, 0.0, 0.009, 0.0);
        assertEquals(1.0, km, 0.05);
    }

    @Test
    public void distanceMeters_isZeroForSamePoint() {
        double meters = GeoDistanceCalculator.distanceMeters(48.0, 11.0, 48.0, 11.0);
        assertTrue(meters < 0.001);
    }
}

