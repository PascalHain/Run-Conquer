package com.pascalhain.runconquer.data.model;

public class User {
    private String id;
    private String name;
    private int level;
    private int xp;
    private double totalDistanceKm;
    private int currentStreakDays;
    private int caloriesToday;
    private double distanceTodayKm;
    private double weeklyGoalKm;
    private double weeklyProgressKm;
    private String averagePace;
    private int totalRuns;
    private double bestDistanceKm;

    public User(String id, String name, int level, int xp, double totalDistanceKm,
                int currentStreakDays, int caloriesToday, double distanceTodayKm,
                double weeklyGoalKm, double weeklyProgressKm, String averagePace,
                int totalRuns, double bestDistanceKm) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.totalDistanceKm = totalDistanceKm;
        this.currentStreakDays = currentStreakDays;
        this.caloriesToday = caloriesToday;
        this.distanceTodayKm = distanceTodayKm;
        this.weeklyGoalKm = weeklyGoalKm;
        this.weeklyProgressKm = weeklyProgressKm;
        this.averagePace = averagePace;
        this.totalRuns = totalRuns;
        this.bestDistanceKm = bestDistanceKm;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public int getCurrentStreakDays() {
        return currentStreakDays;
    }

    public int getCaloriesToday() {
        return caloriesToday;
    }

    public double getDistanceTodayKm() {
        return distanceTodayKm;
    }

    public double getWeeklyGoalKm() {
        return weeklyGoalKm;
    }

    public double getWeeklyProgressKm() {
        return weeklyProgressKm;
    }

    public String getAveragePace() {
        return averagePace;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public double getBestDistanceKm() {
        return bestDistanceKm;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setTotalDistanceKm(double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public void setCurrentStreakDays(int currentStreakDays) {
        this.currentStreakDays = currentStreakDays;
    }

    public void setCaloriesToday(int caloriesToday) {
        this.caloriesToday = caloriesToday;
    }

    public void setDistanceTodayKm(double distanceTodayKm) {
        this.distanceTodayKm = distanceTodayKm;
    }

    public void setWeeklyGoalKm(double weeklyGoalKm) {
        this.weeklyGoalKm = weeklyGoalKm;
    }

    public void setWeeklyProgressKm(double weeklyProgressKm) {
        this.weeklyProgressKm = weeklyProgressKm;
    }

    public void setAveragePace(String averagePace) {
        this.averagePace = averagePace;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public void setBestDistanceKm(double bestDistanceKm) {
        this.bestDistanceKm = bestDistanceKm;
    }
}
