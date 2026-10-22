# AtlasFit v2 💪

Offline-first Android gym tracker built for Planet Fitness routines, with an expandable equipment catalog.

## Features
- Four editable training days, individual machine cards and completion tracking.
- Ten themes: Atlas Purple, Midnight Blue, Emerald, Crimson, Electric Orange, Neon Green, Ocean Teal, Royal Gold, Rose Pink, Arctic Light.
- Searchable catalog of **34 common equipment types** grouped by body part and cardio. Actual machines vary by Planet Fitness location.
- Cardio-specific inputs: treadmill **speed (mph), duration (minutes), incline (%)**; other cardio **resistance/level, time, distance**.
- Strength inputs: weight, sets and reps.
- Workout history, body-weight and waist logs, JSON export/import, offline storage.
- Music panel with shortcuts to Spotify, YouTube Music, YouTube, SoundCloud and Pandora, and basic Android media-key play/pause/skip commands for the active player **when supported**.

## Media and image limitations
The machine banners in this version are **original generated SVG illustrations**, not manufacturer photos or imported JPG/PNG assets. They are deliberately distinct per machine label and designed to work offline. Licensed model-specific machine photographs can be added in a later update once sources and reuse rights are verified. The catalog is not an exhaustive, verified inventory of any individual gym.

Music does **not** embed streaming services or request sign-in credentials. Music control depends on the Android media app and may not respond for every service or device. Links open the music provider externally.

## Build APK
GitHub Actions runs **Build AtlasFit APK** on pushes to main or manual dispatch. Open **Actions**, select the newest successful run, download **AtlasFit-debug-APK**, unzip, and install `app-debug.apk` on your Android phone.

The APK is a debug build for personal testing. Backup your data before uninstalling. Android WebView local storage uses app-private preferences.

## Upgrading from v1
The app migrates previous workout items in place, retaining logged sessions, measurements, and workout weights. It adds cardio fields and themes automatically. Export a backup before installing any new version.
