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
Some machine banners now load real photographs from Wikimedia Commons over HTTPS (currently treadmill, elliptical, lat pulldown and leg press), with source credit links. Other equipment explicitly shows a **photo coming soon** placeholder rather than a misleading repeated icon. Photos need internet access; full offline JPG/PNG asset packaging and complete per-machine photography remain future work. The catalog is not an exhaustive, verified inventory of any individual gym.

A music control dock is displayed at the top of the workout page. Music does **not** embed streaming services, show track metadata, or request sign-in credentials. Music control depends on the Android media app and may not respond for every service or device. Links open the music provider externally.

## Build APK
GitHub Actions runs **Build AtlasFit APK** on pushes to main or manual dispatch. Open **Actions**, select the newest successful run, download **AtlasFit-debug-APK**, unzip, and install `app-debug.apk` on your Android phone.

The APK is a debug build for personal testing. Backup your data before uninstalling. Android WebView local storage uses app-private preferences.

## Upgrading from v1
The app migrates previous workout items in place, retaining logged sessions, measurements, and workout weights. It adds cardio fields and themes automatically. Export a backup before installing any new version.

## Interface improvements
- Choose any workout day when adding a machine from the library.
- Custom exercise and removal dialogs use app-themed overlays, and library confirmations use a themed toast rather than file:// JavaScript alerts.
- Workout page includes persistent music dock with transport controls (active-player support varies).

## Now-playing dock (Android)
The app now includes an opt-in media-session integration. On the Workout page, select **Enable now-playing information** and explicitly grant AtlasFit access in Android's Notification Access settings. This permission is broad; only grant it if you are comfortable with Android's access scope. AtlasFit queries active media sessions to display the available title, artist, app package and cover art. Playback buttons prefer the selected media session's native transport controls. The app does not send track metadata to an external server. Availability varies by player, including YouTube, and the feature may not display video titles when no compatible media session is published. Disable access any time in Android Settings.
