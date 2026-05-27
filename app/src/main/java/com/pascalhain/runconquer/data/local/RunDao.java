package com.pascalhain.runconquer.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RunDao {

    @Query("SELECT * FROM runs ORDER BY startedAtMs DESC")
    List<RunEntity> getRecentRuns();

    @Query("SELECT * FROM runs ORDER BY startedAtMs DESC LIMIT 1")
    RunEntity getLastRun();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRun(RunEntity run);
}

