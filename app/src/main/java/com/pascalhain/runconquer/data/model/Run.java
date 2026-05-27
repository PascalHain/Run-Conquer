package com.pascalhain.runconquer.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Run implements Serializable {
    private String id;
    private String date;
    private String locationName;
    private double latitude;
    private double longitude;
    private double distanceKm;
    private String duration;
    private String pace;
    private int calories;
    private String conqueredTerritoryId;
    private int earnedXp;
    private String userId;
    private long startedAtMs;
    private long endedAtMs;
    private int durationSeconds;
    private double avgSpeedMps;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String duelResult;
    private String opponentUserId;
    private int conquerScore;
    private List<RoutePoint> routePoints = new ArrayList<>();

    public Run(String id, String date, String locationName, double latitude, double longitude,
               double distanceKm, String duration, String pace, int calories,
               String conqueredTerritoryId, int earnedXp) {
        this(id, date, locationName, latitude, longitude, distanceKm, duration, pace, calories,
                conqueredTerritoryId, earnedXp, null, 0L, 0L, 0, 0.0,
                0.0, 0.0, 0.0, 0.0, null, null, 0, null);
    }

    public Run(String id, String date, String locationName, double latitude, double longitude,
               double distanceKm, String duration, String pace, int calories,
               String conqueredTerritoryId, int earnedXp, String userId, long startedAtMs, long endedAtMs,
               int durationSeconds, double avgSpeedMps, double startLatitude, double startLongitude,
               double endLatitude, double endLongitude, String duelResult, String opponentUserId, int conquerScore,
               List<RoutePoint> routePoints) {
        this.id = id;
        this.date = date;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
        this.duration = duration;
        this.pace = pace;
        this.calories = calories;
        this.conqueredTerritoryId = conqueredTerritoryId;
        this.earnedXp = earnedXp;
        this.userId = userId;
        this.startedAtMs = startedAtMs;
        this.endedAtMs = endedAtMs;
        this.durationSeconds = durationSeconds;
        this.avgSpeedMps = avgSpeedMps;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.duelResult = duelResult;
        this.opponentUserId = opponentUserId;
        this.conquerScore = conquerScore;
        this.routePoints = routePoints == null ? new ArrayList<>() : new ArrayList<>(routePoints);
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public String getDuration() {
        return duration;
    }

    public String getPace() {
        return pace;
    }

    public int getCalories() {
        return calories;
    }

    public String getConqueredTerritoryId() {
        return conqueredTerritoryId;
    }

    public int getEarnedXp() {
        return earnedXp;
    }

    public String getUserId() {
        return userId;
    }

    public long getStartedAtMs() {
        return startedAtMs;
    }

    public long getEndedAtMs() {
        return endedAtMs;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public double getAvgSpeedMps() {
        return avgSpeedMps;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public String getDuelResult() {
        return duelResult;
    }

    public String getOpponentUserId() {
        return opponentUserId;
    }

    public int getConquerScore() {
        return conquerScore;
    }

    public List<RoutePoint> getRoutePoints() {
        return routePoints == null ? new ArrayList<>() : routePoints;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setConqueredTerritoryId(String conqueredTerritoryId) {
        this.conqueredTerritoryId = conqueredTerritoryId;
    }

    public void setEarnedXp(int earnedXp) {
        this.earnedXp = earnedXp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartedAtMs(long startedAtMs) {
        this.startedAtMs = startedAtMs;
    }

    public void setEndedAtMs(long endedAtMs) {
        this.endedAtMs = endedAtMs;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setAvgSpeedMps(double avgSpeedMps) {
        this.avgSpeedMps = avgSpeedMps;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public void setDuelResult(String duelResult) {
        this.duelResult = duelResult;
    }

    public void setOpponentUserId(String opponentUserId) {
        this.opponentUserId = opponentUserId;
    }

    public void setConquerScore(int conquerScore) {
        this.conquerScore = conquerScore;
    }

    public void setRoutePoints(List<RoutePoint> routePoints) {
        this.routePoints = routePoints == null ? new ArrayList<>() : new ArrayList<>(routePoints);
    }
}
