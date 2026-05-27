package com.pascalhain.runconquer.data.local;

import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.data.model.Run;

import java.util.ArrayList;
import java.util.List;

public final class RunMapper {

    private RunMapper() {
    }

    public static RunEntity toEntity(Run run) {
        return new RunEntity(
                run.getId(),
                run.getUserId(),
                run.getDate(),
                run.getLocationName(),
                run.getStartedAtMs(),
                run.getEndedAtMs(),
                run.getDurationSeconds(),
                run.getDistanceKm(),
                run.getPace(),
                run.getCalories(),
                run.getAvgSpeedMps(),
                run.getStartLatitude(),
                run.getStartLongitude(),
                run.getEndLatitude(),
                run.getEndLongitude(),
                run.getConqueredTerritoryId(),
                run.getDuelResult(),
                run.getOpponentUserId(),
                run.getConquerScore(),
                run.getEarnedXp()
        );
    }

    public static Run fromEntity(RunEntity entity, List<RoutePoint> routePoints) {
        Run run = new Run(
                entity.getId(),
                entity.getDateLabel(),
                entity.getLocationName(),
                entity.getEndLatitude(),
                entity.getEndLongitude(),
                entity.getDistanceKm(),
                formatDuration(entity.getDurationSeconds()),
                entity.getPace(),
                entity.getCalories(),
                entity.getTerritoryId(),
                entity.getEarnedXp(),
                entity.getUserId(),
                entity.getStartedAtMs(),
                entity.getEndedAtMs(),
                entity.getDurationSeconds(),
                entity.getAvgSpeedMps(),
                entity.getStartLatitude(),
                entity.getStartLongitude(),
                entity.getEndLatitude(),
                entity.getEndLongitude(),
                entity.getDuelResult(),
                entity.getOpponentUserId(),
                entity.getConquerScore(),
                routePoints
        );
        return run;
    }

    public static List<RoutePointEntity> toRouteEntities(String runId, List<RoutePoint> points) {
        List<RoutePointEntity> entities = new ArrayList<>();
        if (points == null) {
            return entities;
        }
        for (int i = 0; i < points.size(); i++) {
            RoutePoint point = points.get(i);
            entities.add(new RoutePointEntity(runId, i, point.getLatitude(), point.getLongitude(), point.getTimestampMs()));
        }
        return entities;
    }

    public static List<RoutePoint> fromRouteEntities(List<RoutePointEntity> entities) {
        List<RoutePoint> points = new ArrayList<>();
        if (entities == null) {
            return points;
        }
        for (RoutePointEntity entity : entities) {
            points.add(new RoutePoint(entity.getLatitude(), entity.getLongitude(), entity.getTimestampMs()));
        }
        return points;
    }

    private static String formatDuration(int durationSeconds) {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds);
    }
}

