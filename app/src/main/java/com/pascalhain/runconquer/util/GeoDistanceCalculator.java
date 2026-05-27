package com.pascalhain.runconquer.util;

public final class GeoDistanceCalculator {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    private GeoDistanceCalculator() {
    }

    public static double distanceMeters(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return EARTH_RADIUS_METERS * c;
    }

    public static double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        return distanceMeters(lat1, lng1, lat2, lng2) / 1000.0;
    }
}

