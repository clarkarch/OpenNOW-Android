# Phase 6: Cleanup

**Goal:** Delete remnants of `OpenNowScreens.kt`. Verify no broken references.

**MCP:** No API calls needed — deletion only.
**YAGNI:** Don't add any new code. Only delete and verify.
**KISS:** Run grep checks, delete what's left, push to CI.
**DO NOT RUN:** `./gradlew` or any build command.

---

### Step 6.1: Verify no references to deleted functions

```bash
# Check no screen composables are referenced from OpenNowScreens.kt
grep -rn "private fun LoginScreen\|private fun HomeScreen\|private fun LibraryScreen\|private fun SettingsScreen\|private fun StreamScreen\|private fun UrlImage" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
# Should return ZERO
```

### Step 6.2: Verify all imports resolve

```bash
# Check that all screen imports in AppNavigation.kt exist
grep -n "import com.opencloudgaming.opennow.ui.screens" app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt

# Check that theme imports exist
grep -n "import com.opencloudgaming.opennow.ui.theme" app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
```

### Step 6.3: Delete extracted code from OpenNowScreens.kt

After all screens are extracted, `OpenNowScreens.kt` should only contain:
- `OpenNowApp` (entry point)
- `MainShell` (now potentially replaced by `AppNavigation`)
- `TopStatusBar`
- `AppNavigationRail` / `AppNavigationRailItem` / `BottomNavItem`
- `NativeSearchField`
- DPad/TV focus helpers
- Theme import (replaces inline theme code)

If `MainShell` is fully replaced by `AppNavigation`, delete it too.

### Step 6.4: Verify compilation

```bash
git add -A && git commit -m "Phase 6: Cleanup extracted code" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
```
