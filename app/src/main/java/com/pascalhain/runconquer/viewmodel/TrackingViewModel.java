package com.pascalhain.runconquer.viewmodel;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

import com.pascalhain.runconquer.util.PaceCalculator;
import com.pascalhain.runconquer.util.RunSession;
import com.pascalhain.runconquer.util.TimeFormatter;
import com.pascalhain.runconquer.util.GeoDistanceCalculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrackingViewModel extends ViewModel {

    private static final long TICK_MS = 1000L;
    private static final double KCAL_PER_KM = 60.0;
    private static final double MAX_VALID_JUMP_KM = 0.2;
    private static final double MIN_VALID_MOVEMENT_METERS = 10.0;
    private static final double MIN_PACE_DISTANCE_KM = 0.05;
    private static final float MAX_ACCURACY_METERS = 20.0f;
    private static final long MIN_LOCATION_INTERVAL_MS = 1000L;

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable ticker = this::onTick;
    private final DecimalFormat distanceFormat = new DecimalFormat("0.00");
    private final RunSession runSession = new RunSession();

    private long accumulatedMs = 0L;
    private long lastStartMs = 0L;
    private boolean awaitingFirstFix = true;
    private Double lastAcceptedLat = null;
    private Double lastAcceptedLng = null;
    private long lastAcceptedTimeMs = 0L;

    private final MutableLiveData<String> timerText = new MutableLiveData<>("00:00");
    private final MutableLiveData<String> distanceText = new MutableLiveData<>("-- km");
    private final MutableLiveData<String> paceText = new MutableLiveData<>("-- min/km");
    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> paused = new MutableLiveData<>(false);
    private final MutableLiveData<String> caloriesText = new MutableLiveData<>("-- kcal");

    private final MutableLiveData<android.location.Location> lastLocation = new MutableLiveData<>();
    private final MutableLiveData<List<RoutePoint>> routePointsLive = new MutableLiveData<>(new ArrayList<>());
    private double lastDistanceKm = 0.0;
    private int lastCalories = 0;
    private String lastDuration = "00:00";
    private String lastPace = "-- min/km";
    private long runStartedAtMs = 0L;
    private long runEndedAtMs = 0L;
    private Double runStartLat = null;
    private Double runStartLng = null;

    public LiveData<String> getTimerText() {
        return timerText;
    }

    public LiveData<String> getDistanceText() {
        return distanceText;
    }

    public LiveData<String> getPaceText() {
        return paceText;
    }

    public LiveData<Boolean> isRunning() {
        return running;
    }

    public LiveData<Boolean> isPaused() {
        return paused;
    }

    public LiveData<String> getCaloriesText() {
        return caloriesText;
    }

    public LiveData<android.location.Location> getLastLocation() {
        return lastLocation;
    }

    public LiveData<List<RoutePoint>> getRoutePoints() {
        return routePointsLive;
    }

    public void updateLocation(android.location.Location location) {
        lastLocation.setValue(location);
        if (!Boolean.TRUE.equals(running.getValue())) {
            return;
        }
        if (location.hasAccuracy() && location.getAccuracy() > MAX_ACCURACY_METERS) {
            return;
        }
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        long timeMs = location.getTime();
        if (awaitingFirstFix) {
            runSession.seedLocation(lat, lng);
            awaitingFirstFix = false;
            runStartLat = lat;
            runStartLng = lng;
            lastAcceptedLat = lat;
            lastAcceptedLng = lng;
            lastAcceptedTimeMs = timeMs;
            addRoutePoint(lat, lng, timeMs);
            updateMetrics();
            return;
        } else {
            if (lastAcceptedLat == null || lastAcceptedLng == null) {
                runSession.seedLocation(lat, lng);
                lastAcceptedLat = lat;
                lastAcceptedLng = lng;
                lastAcceptedTimeMs = timeMs;
                updateMetrics();
                return;
            }
            long deltaTimeMs = timeMs - lastAcceptedTimeMs;
            if (deltaTimeMs > 0 && deltaTimeMs < MIN_LOCATION_INTERVAL_MS) {
                return;
            }
            double deltaKm = GeoDistanceCalculator.distanceKm(lastAcceptedLat, lastAcceptedLng, lat, lng);
            if ((deltaKm * 1000.0) < MIN_VALID_MOVEMENT_METERS) {
                return;
            }
            if (deltaKm > MAX_VALID_JUMP_KM) {
                runSession.seedLocation(lat, lng);
                lastAcceptedLat = lat;
                lastAcceptedLng = lng;
                lastAcceptedTimeMs = timeMs;
                addRoutePoint(lat, lng, timeMs);
                updateMetrics();
                return;
            }
            runSession.addLocation(lat, lng);
            lastAcceptedLat = lat;
            lastAcceptedLng = lng;
            lastAcceptedTimeMs = timeMs;
            addRoutePoint(lat, lng, timeMs);
        }
        updateMetrics();
    }

    public void startRun() {
        if (Boolean.TRUE.equals(running.getValue())) {
            return;
        }
        accumulatedMs = 0L;
        lastStartMs = SystemClock.elapsedRealtime();
        runStartedAtMs = System.currentTimeMillis();
        awaitingFirstFix = true;
        runSession.reset();
        routePointsLive.setValue(new ArrayList<>());
        runStartLat = null;
        runStartLng = null;
        lastAcceptedLat = null;
        lastAcceptedLng = null;
        lastAcceptedTimeMs = 0L;
        running.setValue(true);
        paused.setValue(false);
        updateMetrics();
        startTicker();
    }

    public void pauseRun() {
        if (!Boolean.TRUE.equals(running.getValue())) {
            return;
        }
        accumulatedMs += SystemClock.elapsedRealtime() - lastStartMs;
        running.setValue(false);
        paused.setValue(true);
        awaitingFirstFix = true;
        lastAcceptedLat = null;
        lastAcceptedLng = null;
        lastAcceptedTimeMs = 0L;
        stopTicker();
        updateMetrics();
    }

    public void resumeRun() {
        if (!Boolean.TRUE.equals(paused.getValue())) {
            return;
        }
        lastStartMs = SystemClock.elapsedRealtime();
        awaitingFirstFix = true;
        lastAcceptedLat = null;
        lastAcceptedLng = null;
        lastAcceptedTimeMs = 0L;
        running.setValue(true);
        paused.setValue(false);
        startTicker();
    }

    public void stopRun() {
        stopTicker();
        accumulatedMs = 0L;
        awaitingFirstFix = true;
        runSession.reset();
        routePointsLive.setValue(new ArrayList<>());
        runStartLat = null;
        runStartLng = null;
        lastAcceptedLat = null;
        lastAcceptedLng = null;
        lastAcceptedTimeMs = 0L;
        running.setValue(false);
        paused.setValue(false);
        timerText.setValue("00:00");
        distanceText.setValue("-- km");
        paceText.setValue("-- min/km");
        caloriesText.setValue("-- kcal");
        lastDistanceKm = 0.0;
        lastCalories = 0;
        lastDuration = "00:00";
        lastPace = "-- min/km";
        runStartedAtMs = 0L;
        runEndedAtMs = 0L;
    }

    public Run completeRun() {
        runEndedAtMs = System.currentTimeMillis();
        Run run = buildRunFromCurrentMetrics();
        repository.saveRun(run);
        repository.claimTerritoryForLastRun(run);
        stopRun();
        return run;
    }

    private Run buildRunFromCurrentMetrics() {
        String runId = "run_" + System.currentTimeMillis();
        String dateLabel = new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.US)
                .format(new java.util.Date());
        android.location.Location location = lastLocation.getValue();
        double lat = location == null ? 0.0 : location.getLatitude();
        double lng = location == null ? 0.0 : location.getLongitude();
        String locationName = location == null ? "Unbekannt" : "Aktueller Standort";
        Run run = new Run(runId, dateLabel, locationName, lat, lng, lastDistanceKm,
                lastDuration, lastPace, lastCalories, "t1", 120);
        run.setUserId("local_user");
        run.setStartedAtMs(runStartedAtMs);
        run.setEndedAtMs(runEndedAtMs == 0L ? System.currentTimeMillis() : runEndedAtMs);
        run.setDurationSeconds(getDurationSeconds());
        run.setAvgSpeedMps(getAverageSpeedMps());
        run.setStartLatitude(runStartLat == null ? lat : runStartLat);
        run.setStartLongitude(runStartLng == null ? lng : runStartLng);
        run.setEndLatitude(lat);
        run.setEndLongitude(lng);
        run.setDuelResult(null);
        run.setOpponentUserId(null);
        run.setConquerScore(0);
        run.setRoutePoints(routePointsLive.getValue());
        return run;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTicker();
    }

    private void startTicker() {
        handler.removeCallbacks(ticker);
        handler.post(ticker);
    }

    private void stopTicker() {
        handler.removeCallbacks(ticker);
    }

    private void onTick() {
        updateMetrics();
        if (Boolean.TRUE.equals(running.getValue())) {
            handler.postDelayed(ticker, TICK_MS);
        }
    }

    private void updateMetrics() {
        long elapsedMs = accumulatedMs;
        if (Boolean.TRUE.equals(running.getValue())) {
            elapsedMs += SystemClock.elapsedRealtime() - lastStartMs;
        }
        int totalSeconds = (int) (elapsedMs / 1000L);
        lastDuration = TimeFormatter.formatSeconds(totalSeconds);
        timerText.setValue(lastDuration);

        double distanceKm = runSession.getTotalDistanceKm();
        lastDistanceKm = distanceKm;
        distanceText.setValue(distanceFormat.format(distanceKm) + " km");

        String paceValue = PaceCalculator.calculatePace(distanceKm, totalSeconds);
        lastPace = (distanceKm < MIN_PACE_DISTANCE_KM || paceValue.equals("-")) ? "-- min/km" : paceValue;
        paceText.setValue(lastPace);

        lastCalories = (int) Math.round(distanceKm * KCAL_PER_KM);
        caloriesText.setValue(String.format(Locale.US, "%d kcal", lastCalories));
    }

    private void addRoutePoint(double lat, double lng, long timestampMs) {
        List<RoutePoint> points = new ArrayList<>(routePointsLive.getValue() == null
                ? new ArrayList<>() : routePointsLive.getValue());
        points.add(new RoutePoint(lat, lng, timestampMs));
        routePointsLive.setValue(points);
    }

    private int getDurationSeconds() {
        String duration = lastDuration;
        if (duration == null || duration.isEmpty()) {
            return 0;
        }
        String[] parts = duration.split(":");
        if (parts.length == 2) {
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        }
        return 0;
    }

    private double getAverageSpeedMps() {
        int seconds = getDurationSeconds();
        if (seconds <= 0) {
            return 0.0;
        }
        return (lastDistanceKm * 1000.0) / seconds;
    }
}
