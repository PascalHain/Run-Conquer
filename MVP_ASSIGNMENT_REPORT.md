# MVP Prototype Report (Run & Conquer)

Date: 2026-05-27

## Deliverables
- First working prototype of the central app idea (start run -> tracking -> summary -> save -> show in Home/You).
- Implemented MVP scenario with real GPS-based distance, pace, and time tracking.
- Updated requirements and scope notes (see Scope Update).
- Short technical progress report (see Technical Progress).
- List of known issues (see Known Issues).
- First automated tests (see Tests).
- Short AI use note (see AI Use Note).

## MVP Scenario (central use case)
1. Open the app on Home.
2. Tap the center Activity button.
3. Start a run in the Tracking screen.
4. The app collects GPS locations and updates distance, pace, and timer live.
5. Stop the run and view the Summary screen with the real metrics.
6. Save the run and confirm it appears in Home (last run) and You (all runs).

## Scope Update
- Reduced scope for prototype: Community feed, Team sync, and Territory conquest remain mock/placeholder.
- Focused scope: single-run tracking with live metrics and local persistence via the in-memory data source.
- Rationale: prioritize a stable and convincing core flow over a broad but incomplete feature set.

## Technical Progress (short)
- Tracking now uses real GPS updates to compute distance and pace.
- Metrics are formatted via TimeFormatter and PaceCalculator for consistent UI output.
- Run summary is created from live tracking data and saved through the repository.
- Home and You use repository data and show newly saved runs immediately.
- OSM tiles (osmdroid) provide a visible map without API keys.

## Known Issues / Limitations
- GPS jitter on emulator can create small distance spikes; a basic jump filter is applied.
- Tracking runs only while the app is in the foreground (no background service yet).
- Map tiles require internet access; emulator needs location injected to show correct position.
- Territories/Team/Community are still mock placeholders.

## Tests (first automated)
- Unit: `GeoDistanceCalculatorTest` (distance calculation).
- Unit: `RunSessionTest` (session distance accumulation, reset).
- Unit: `RunWorkflowTest` (end-to-end metric calculation: time + pace).

## AI Use Note
An AI assistant was used to summarize the existing codebase, propose MVP scope, and help implement and document the GPS-based tracking logic and tests. No external codebases were copied.

