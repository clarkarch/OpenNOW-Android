# Phase 7: Cleanup

**Goal:** Delete old monolithic UI file and update references.

**INTEGRATION CHECK (MANDATORY before deleting):**
Before deleting OpenNowScreens.kt, verify:
1. NO file imports from OpenNowScreens.kt
2. ALL composable functions from OpenNowScreens.kt are now in new files
3. ALL routes navigate to new screens (not old ones)
4. The app compiles without OpenNowScreens.kt

---

### Step 7.1: Verify no references to old code

```bash
# Check for any imports of old composables
grep -rn "import com.opencloudgaming.opennow.OpenNowApp" app/src/main/java/
grep -rn "import com.opencloudgaming.opennow.StreamScreen" app/src/main/java/
grep -rn "import com.opencloudgaming.opennow.HomeScreen" app/src/main/java/
grep -rn "import com.opencloudgaming.opennow.LibraryScreen" app/src/main/java/
grep -rn "import com.opencloudgaming.opennow.SettingsScreen" app/src/main/java/
grep -rn "import com.opencloudgaming.opennow.LoginScreen" app/src/main/java/

# Check for any references to old composables
grep -rn "OpenNowApp(" app/src/main/java/
grep -rn "OpenNowScreens" app/src/main/java/
```

All of these should return ZERO results.

### Step 7.2: Delete OpenNowScreens.kt

```bash
rm app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
```

### Step 7.3: Verify compilation

```bash
# This should succeed with no errors
./gradlew :app:assembleDebug --no-daemon 2>&1 | tail -5
# Expected: BUILD SUCCESSFUL
```

### Step 7.4: Final integration test

Verify ALL user flows work:
1. Login → Home screen shows games
2. Home → tap game → GameDetailsSheet shows
3. Home → play game → Stream screen launches
4. Bottom nav → Library shows games
5. Bottom nav → Settings shows categories
6. Settings → Add account → navigates to Login
7. Stream → controls panel opens
8. Stream → exit confirmation works
9. TV login → TvDeviceLogin route works

### Step 7.5: Commit and verify CI

```bash
git add -A
git commit -m "Phase 7: Cleanup — delete OpenNowScreens.kt"
git push
gh run watch  # MUST show BUILD SUCCESSFUL
```
