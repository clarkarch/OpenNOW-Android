# Phase 6: Cleanup

**Goal:** Delete old monolithic UI file.

---

### Step 6.1: Verify no references to old code

```bash
grep -rn "OpenNowApp\|OpenNowScreens" app/src/main/java/
```

All should return ZERO results.

### Step 6.2: Delete OpenNowScreens.kt

```bash
rm app/src/main/java/com/opencloudgaming/opennow/OpenNowScreens.kt
```

### Step 6.3: Verify compilation

```bash
git add -A && git commit -m "Phase 6: Delete OpenNowScreens.kt" && git push
gh run watch
```
