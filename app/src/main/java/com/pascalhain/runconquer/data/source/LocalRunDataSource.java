package com.pascalhain.runconquer.data.source;

import com.pascalhain.runconquer.data.local.RoutePointDao;
import com.pascalhain.runconquer.data.local.RunDao;
import com.pascalhain.runconquer.data.local.RunMapper;
import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.data.model.Run;

import java.util.ArrayList;
import java.util.List;

public class LocalRunDataSource implements RunDataSource {

    private final RunDao runDao;
    private final RoutePointDao routePointDao;

    public LocalRunDataSource(RunDao runDao, RoutePointDao routePointDao) {
        this.runDao = runDao;
        this.routePointDao = routePointDao;
    }

    @Override
    public List<Run> getRecentRuns() {
        List<com.pascalhain.runconquer.data.local.RunEntity> entities = runDao.getRecentRuns();
        List<Run> runs = new ArrayList<>();
        for (com.pascalhain.runconquer.data.local.RunEntity entity : entities) {
            List<RoutePoint> routePoints = RunMapper.fromRouteEntities(routePointDao.getRoutePoints(entity.getId()));
            runs.add(RunMapper.fromEntity(entity, routePoints));
        }
        return runs;
    }

    @Override
    public Run getLastRun() {
        com.pascalhain.runconquer.data.local.RunEntity entity = runDao.getLastRun();
        if (entity == null) {
            return null;
        }
        List<RoutePoint> routePoints = RunMapper.fromRouteEntities(routePointDao.getRoutePoints(entity.getId()));
        return RunMapper.fromEntity(entity, routePoints);
    }

    @Override
    public void saveRun(Run run) {
        if (run == null) {
            return;
        }
        runDao.insertRun(RunMapper.toEntity(run));
        routePointDao.deleteRoutePoints(run.getId());
        routePointDao.insertRoutePoints(RunMapper.toRouteEntities(run.getId(), run.getRoutePoints()));
    }

    @Override
    public Run createMockCompletedRun() {
        return null;
    }
}

