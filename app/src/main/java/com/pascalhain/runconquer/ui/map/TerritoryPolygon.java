package com.pascalhain.runconquer.ui.map;

import com.pascalhain.runconquer.data.model.OwnerType;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class TerritoryPolygon {
    private final String id;
    private final OwnerType ownerType;
    private final int strength;
    private final List<GeoPoint> points;

    public TerritoryPolygon(String id, OwnerType ownerType, int strength, List<GeoPoint> points) {
        this.id = id;
        this.ownerType = ownerType;
        this.strength = strength;
        this.points = points == null ? new ArrayList<>() : new ArrayList<>(points);
    }

    public String getId() {
        return id;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public int getStrength() {
        return strength;
    }

    public List<GeoPoint> getPoints() {
        return points;
    }
}
