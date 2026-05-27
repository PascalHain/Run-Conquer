package com.pascalhain.runconquer.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "run_route_points",
        foreignKeys = @ForeignKey(
                entity = RunEntity.class,
                parentColumns = "id",
                childColumns = "runId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("runId")}
)
public class RoutePointEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String runId;
    private int seq;
    private double latitude;
    private double longitude;
    private long timestampMs;

    public RoutePointEntity(String runId, int seq, double latitude, double longitude, long timestampMs) {
        this.runId = runId;
        this.seq = seq;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampMs = timestampMs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRunId() {
        return runId;
    }

    public int getSeq() {
        return seq;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestampMs() {
        return timestampMs;
    }
}
