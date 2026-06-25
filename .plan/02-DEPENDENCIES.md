# Phase 2: Add Dependencies

**Goal:** Add Coil 3 and Navigation Compose to `build.gradle.kts`. Low risk — additive only.

**MCP:** Before adding Coil 3, verify:
```
android-docs_search_android_docs("coil3 compose AsyncImage")
android-docs_get_release_notes("coil")
```
Verify Navigation Compose version compatibility:
```
android-docs_search_android_docs("navigation compose dependencies")
android-docs_get_api_reference("androidx.navigation.compose.NavHost")
```
**YAGNI:** Only add what's needed. No extra Coil modules.
**KISS:** 3 lines in build.gradle.kts, nothing else.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Step 2.1: Edit build.gradle.kts

**Edit:** `app/build.gradle.kts`

Add these 3 lines to the `dependencies` block (after the existing Compose dependencies):

```kotlin
    implementation("androidx.navigation:navigation-compose:2.9.8")
    implementation("io.coil-kt.coil3:coil-compose:3.5.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.5.0")
```

**Why these versions:**
- `navigation-compose:2.9.8` — latest stable, works with Compose BOM 2026.04.01
- `coil3:3.5.0` — Coil 3 (NOT Coil 2). Package is `io.coil-kt.coil3`, import is `coil3.compose.AsyncImage`
- `coil-network-okhttp` — required backend for Coil 3 image fetching

### Step 2.2: Verify

```bash
# Should show 3 new lines
grep -n "navigation-compose\|coil3" app/build.gradle.kts
```
