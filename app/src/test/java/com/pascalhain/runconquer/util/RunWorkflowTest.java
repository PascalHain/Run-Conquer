package com.pascalhain.runconquer.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RunWorkflowTest {

    @Test
    public void workflowComputesPaceAndDuration() {
        RunSession session = new RunSession();
        session.seedLocation(0.0, 0.0);
        session.addLocation(0.009, 0.0);
        double distanceKm = session.getTotalDistanceKm();
        assertTrue(distanceKm > 0.9);

        int durationSeconds = 300;
        String durationText = TimeFormatter.formatSeconds(durationSeconds);
        String paceText = PaceCalculator.calculatePace(distanceKm, durationSeconds);

        assertEquals("05:00", durationText);
        assertTrue(paceText.endsWith("min/km"));
        assertTrue(paceText.startsWith("04:") || paceText.startsWith("05:") || paceText.startsWith("06:"));
    }
}
