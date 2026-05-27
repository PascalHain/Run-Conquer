package com.pascalhain.runconquer.data.mock;

import com.pascalhain.runconquer.data.model.OwnerType;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.data.model.RoutePoint;

import java.util.ArrayList;
import java.util.List;

public final class MockDataProvider {

    private MockDataProvider() {
    }

    public static User getUser() {
        return new User(
                "u1",
                "Pascal",
                3,
                325,
                325.0,
                7,
                320,
                5.2,
                20.0,
                12.4,
                "05:20 min/km",
                67,
                12.5
        );
    }

    public static Team getTeam() {
        return new Team("team1", "Red Runners", 4, 42.6);
    }

    public static List<Run> getRecentRuns() {
        List<Run> runs = new ArrayList<>();
        Run r1 = new Run("r1", "12.03.2024", "Muenchen, DE", 48.137154, 11.576124, 6.8, "36:28", "05:21 min/km", 430, "t1", 160);
        Run r2 = new Run("r2", "10.03.2024", "Stuttgart, DE", 48.775845, 9.182932, 5.1, "28:50", "05:39 min/km", 320, "t2", 120);
        Run r3 = new Run("r3", "08.03.2024", "Augsburg, DE", 48.370545, 10.897790, 4.2, "24:10", "05:45 min/km", 280, "t3", 90);
        Run r4 = new Run("r4", "05.03.2024", "Ingolstadt, DE", 48.765080, 11.423720, 7.4, "40:12", "05:25 min/km", 480, "t4", 180);
        Run r5 = new Run("r5", "03.03.2024", "Regensburg, DE", 49.013432, 12.101624, 3.6, "20:05", "05:35 min/km", 220, "t5", 70);
        Run r6 = new Run("r6", "01.03.2024", "Nuernberg, DE", 49.452103, 11.076665, 8.9, "49:30", "05:34 min/km", 520, "t2", 210);
        Run r7 = new Run("r7", "27.02.2024", "Ulm, DE", 48.398401, 9.991550, 2.9, "16:10", "05:34 min/km", 180, "t5", 55);
        Run r8 = new Run("r8", "24.02.2024", "Rosenheim, DE", 47.856353, 12.128947, 6.1, "33:40", "05:31 min/km", 380, "t3", 140);
        Run r9 = new Run("r9", "21.02.2024", "Passau, DE", 48.566736, 13.431946, 5.7, "31:05", "05:27 min/km", 350, "t4", 130);
        Run r10 = new Run("r10", "18.02.2024", "Landshut, DE", 48.544200, 12.146853, 4.8, "26:20", "05:29 min/km", 300, "t1", 110);
        Run r11 = new Run("r11", "15.02.2024", "Kempten, DE", 47.724454, 10.311689, 7.0, "39:05", "05:35 min/km", 450, "t2", 175);
        Run r12 = new Run("r12", "12.02.2024", "Wuerzburg, DE", 49.791304, 9.953354, 3.9, "21:50", "05:36 min/km", 240, "t5", 85);
        addMockRoute(r1, 0.0020);
        addMockRoute(r2, 0.0018);
        addMockRoute(r3, 0.0015);
        addMockRoute(r4, 0.0022);
        addMockRoute(r5, 0.0014);
        addMockRoute(r6, 0.0024);
        addMockRoute(r7, 0.0012);
        addMockRoute(r8, 0.0019);
        addMockRoute(r9, 0.0020);
        addMockRoute(r10, 0.0016);
        addMockRoute(r11, 0.0023);
        addMockRoute(r12, 0.0017);
        runs.add(r1);
        runs.add(r2);
        runs.add(r3);
        runs.add(r4);
        runs.add(r5);
        runs.add(r6);
        runs.add(r7);
        runs.add(r8);
        runs.add(r9);
        runs.add(r10);
        runs.add(r11);
        runs.add(r12);
        return runs;
    }

    public static Run getMockCompletedRun() {
        Run run = new Run("r_mock_completed", "Heute", "Muenchen, DE", 48.137154, 11.576124,
                4.85, "23:45", "05:12 min/km", 310, "t1", 120);
        addMockRoute(run, 0.0016);
        return run;
    }

    private static void addMockRoute(Run run, double offset) {
        if (run == null) {
            return;
        }
        double lat = run.getLatitude();
        double lng = run.getLongitude();
        List<RoutePoint> points = new ArrayList<>();
        long baseTime = System.currentTimeMillis();
        points.add(new RoutePoint(lat, lng, baseTime));
        points.add(new RoutePoint(lat + offset, lng, baseTime + 60000));
        points.add(new RoutePoint(lat + offset, lng + offset, baseTime + 120000));
        points.add(new RoutePoint(lat, lng + offset, baseTime + 180000));
        points.add(new RoutePoint(lat, lng, baseTime + 240000));
        run.setRoutePoints(points);
        run.setStartLatitude(lat);
        run.setStartLongitude(lng);
        run.setEndLatitude(lat);
        run.setEndLongitude(lng);
    }
    public static List<Territory> getTerritories() {
        List<Territory> territories = new ArrayList<>();
        territories.add(new Territory("t1", "Innenstadt Sued", OwnerType.SELF, 80));
        territories.add(new Territory("t2", "Campus Ost", OwnerType.SELF, 50));
        territories.add(new Territory("t3", "Park West", OwnerType.ENEMY, 65));
        territories.add(new Territory("t4", "Altstadt", OwnerType.ENEMY, 70));
        territories.add(new Territory("t5", "Bahnhof Nord", OwnerType.NEUTRAL, 20));
        return territories;
    }
}
