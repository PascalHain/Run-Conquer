# Run & Conquer - Walking Skeleton

This project provides a minimal walking skeleton for the Run & Conquer Android app.

## Quick start

- Open the project in Android Studio.
- Sync Gradle.
- Run the `app` configuration on an Android 34+ emulator or device.

## Flow to verify

1. Start on Home.
2. Tap the red center Activity button.
3. Tap Stop to open Run Summary.
4. Go to Map, then You (Profile).

## Map previews (no API key)

- Map previews use OpenStreetMap static tiles (`staticmap.openstreetmap.de`) and do not need a key.
- On emulator: set a location in Extended Controls -> Location, otherwise previews stay empty.
- Ensure the emulator has internet access.

## Data flow

UI -> ViewModel -> RunConquerRepository -> Local Room (Runs) + MockDataProvider (User/Team/Territory)

## TODOs

- Replace mock user/team data with real persistence.
- Replace mock tracking values with real GPS tracking.
- Add real territory calculation based on GPS route.
- Add real team synchronization.
