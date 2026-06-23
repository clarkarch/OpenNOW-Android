# OpenNOW Android

Standalone Android client for NVIDIA GeForce NOW streaming.

> **Warning:** This project is vibecoded and will be unstable. Use at your own risk.

## Features

- GeForce NOW game streaming via WebRTC
- Touch overlay with virtual gamepad + finger mouse
- Queue ad playback
- Game catalog browsing and library management

## Build

```bash
./gradlew :app:assembleDebug
```

Requires Android SDK with API 36 and CMake 3.22.1.

## Tech Stack

- Kotlin + Jetpack Compose
- Material 3
- WebRTC (Media3)
- Native JNI (`opennow_native`)

## License

GPL v3 — see [LICENSE](LICENSE)
