package com.pascalhain.runconquer.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoutePointDao {

    @Query("SELECT * FROM run_route_points WHERE runId = :runId ORDER BY seq ASC")
    List<RoutePointEntity> getRoutePoints(String runId);

    @Query("DELETE FROM run_route_points WHERE runId = :runId")
    void deleteRoutePoints(String runId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoutePoints(List<RoutePointEntity> points);
}

