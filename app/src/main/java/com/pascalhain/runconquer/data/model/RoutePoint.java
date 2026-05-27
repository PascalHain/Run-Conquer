package com.pascalhain.runconquer.data.model;

import java.io.Serializable;

public class RoutePoint implements Serializable {
    private final double latitude;
    private final double longitude;
    private final long timestampMs;

    public RoutePoint(double latitude, double longitude, long timestampMs) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampMs = timestampMs;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestampMs() {
        return timestampMs;
    }
}

