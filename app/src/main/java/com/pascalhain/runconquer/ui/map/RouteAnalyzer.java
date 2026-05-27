package com.pascalhain.runconquer.ui.map;

import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.util.GeoDistanceCalculator;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public final class RouteAnalyzer {
    private static final double CLOSE_LOOP_THRESHOLD_KM = 0.08;

    private RouteAnalyzer() {
    }

    public static boolean canCreateTerritory(List<RoutePoint> points) {
        if (points == null || points.size() < 4) {
            return false;
        }
        RoutePoint first = points.get(0);
        RoutePoint last = points.get(points.size() - 1);
        double closeKm = GeoDistanceCalculator.distanceKm(
                first.getLatitude(), first.getLongitude(),
                last.getLatitude(), last.getLongitude());
        return closeKm <= CLOSE_LOOP_THRESHOLD_KM;
    }

    public static List<GeoPoint> toPolygon(List<RoutePoint> points) {
        List<GeoPoint> polygon = new ArrayList<>();
        if (points == null) {
            return polygon;
        }
        for (RoutePoint point : points) {
            polygon.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        }
        return polygon;
    }
}
