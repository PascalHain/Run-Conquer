package com.pascalhain.runconquer.data.model;

public class Territory {
    private String id;
    private String name;
    private OwnerType ownerType;
    private int strength;

    public Territory(String id, String name, OwnerType ownerType, int strength) {
        this.id = id;
        this.name = name;
        this.ownerType = ownerType;
        this.strength = strength;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public int getStrength() {
        return strength;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
