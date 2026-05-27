package com.pascalhain.runconquer.data.model;

public class Team {
    private String id;
    private String name;
    private int memberCount;
    private double weeklyDistanceKm;

    public Team(String id, String name, int memberCount, double weeklyDistanceKm) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.weeklyDistanceKm = weeklyDistanceKm;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public double getWeeklyDistanceKm() {
        return weeklyDistanceKm;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public void setWeeklyDistanceKm(double weeklyDistanceKm) {
        this.weeklyDistanceKm = weeklyDistanceKm;
    }
}
