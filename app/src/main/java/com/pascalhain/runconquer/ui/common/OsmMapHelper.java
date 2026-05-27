package com.pascalhain.runconquer.ui.common;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import com.pascalhain.runconquer.data.model.RoutePoint;

import java.util.ArrayList;
import java.util.List;

public final class OsmMapHelper {

    private static final double FALLBACK_LAT = 48.137154;
    private static final double FALLBACK_LNG = 11.576124;
    private static final double DEFAULT_ZOOM = 15.0;

    private OsmMapHelper() {
    }

    public static void configure(Context context, MapView mapView) {
        if (context == null || mapView == null) {
            return;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Configuration.getInstance().load(context.getApplicationContext(), prefs);
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getController().setZoom(DEFAULT_ZOOM);
    }

    public static void enableMyLocation(Context context, MapView mapView, boolean followLocation) {
        if (context == null || mapView == null) {
            return;
        }
        MyLocationNewOverlay existing = null;
        for (Object overlay : mapView.getOverlays()) {
            if (overlay instanceof MyLocationNewOverlay) {
                existing = (MyLocationNewOverlay) overlay;
                break;
            }
        }
        if (existing == null) {
            MyLocationNewOverlay overlay = new MyLocationNewOverlay(
                    new GpsMyLocationProvider(context.getApplicationContext()), mapView);
            overlay.enableMyLocation();
            if (followLocation) {
                overlay.enableFollowLocation();
            }
            mapView.getOverlays().add(overlay);
        } else {
            existing.enableMyLocation();
            if (followLocation) {
                existing.enableFollowLocation();
            }
        }
    }

    public static void updateLocation(MapView mapView, Double latitude, Double longitude, boolean showMarker) {
        if (mapView == null) {
            return;
        }
        double lat = latitude == null ? 0.0 : latitude;
        double lng = longitude == null ? 0.0 : longitude;
        if (lat == 0.0 && lng == 0.0) {
            lat = FALLBACK_LAT;
            lng = FALLBACK_LNG;
        }
        GeoPoint point = new GeoPoint(lat, lng);
        mapView.getController().setCenter(point);
        if (showMarker) {
            clearOverlaysExceptMyLocation(mapView);
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    public static void updateRoute(MapView mapView, List<RoutePoint> points, boolean showMarker) {
        if (mapView == null) {
            return;
        }
        if (points == null || points.isEmpty()) {
            updateLocation(mapView, null, null, showMarker);
            return;
        }
        List<GeoPoint> geoPoints = new ArrayList<>();
        double minLat = Double.MAX_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double maxLng = -Double.MAX_VALUE;
        for (RoutePoint point : points) {
            double lat = point.getLatitude();
            double lng = point.getLongitude();
            geoPoints.add(new GeoPoint(lat, lng));
            minLat = Math.min(minLat, lat);
            minLng = Math.min(minLng, lng);
            maxLat = Math.max(maxLat, lat);
            maxLng = Math.max(maxLng, lng);
        }
        clearOverlaysExceptMyLocation(mapView);
        Polyline polyline = new Polyline();
        polyline.setPoints(geoPoints);
        polyline.setColor(0xFFFF3B30);
        polyline.setWidth(6.0f);
        mapView.getOverlays().add(polyline);
        BoundingBox bounds = new BoundingBox(maxLat, maxLng, minLat, minLng);
        mapView.zoomToBoundingBox(bounds, true, 48);
        GeoPoint lastPoint = geoPoints.get(geoPoints.size() - 1);
        if (showMarker) {
            Marker marker = new Marker(mapView);
            marker.setPosition(lastPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private static void clearOverlaysExceptMyLocation(MapView mapView) {
        List<Object> keep = new ArrayList<>();
        for (Object overlay : mapView.getOverlays()) {
            if (overlay instanceof MyLocationNewOverlay) {
                keep.add(overlay);
            }
        }
        mapView.getOverlays().clear();
        for (Object overlay : keep) {
            mapView.getOverlays().add((org.osmdroid.views.overlay.Overlay) overlay);
        }
    }
}
