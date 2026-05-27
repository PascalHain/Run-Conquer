package com.pascalhain.runconquer.util;

public final class RunSession {

    private Double lastLat;
    private Double lastLng;
    private double totalDistanceKm;

    public void reset() {
        lastLat = null;
        lastLng = null;
        totalDistanceKm = 0.0;
    }

    public void seedLocation(double lat, double lng) {
        lastLat = lat;
        lastLng = lng;
    }

    public void addLocation(double lat, double lng) {
        if (lastLat == null || lastLng == null) {
            seedLocation(lat, lng);
            return;
        }
        double deltaKm = GeoDistanceCalculator.distanceKm(lastLat, lastLng, lat, lng);
        totalDistanceKm += deltaKm;
        lastLat = lat;
        lastLng = lng;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }
}

