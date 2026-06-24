# Phase 6: Polish

**Goal:** Final pass on transitions, animations, loading states.

**Skills to read first:** `compose-animations/SKILL.md`, `compose-recomposition-performance/SKILL.md`

**MCP verification:** Use `android-docs_search_android_docs` and `android-docs_get_api_reference` to verify `AnimatedVisibility`, `animateFloatAsState`, and other animation APIs BEFORE using them.

---

### Step 6.1: Verify skeleton loading in HomeScreen

HomeScreen already includes skeleton loading in Phase 4 (Step 4.10).

### Step 6.2: Verify all animations use correct APIs

Check every animation usage:
- `AnimatedVisibility` — verify `enter`/`exit` parameters match API
- `animateFloatAsState` — verify `animationSpec` parameter
- `spring()` — verify `stiffness` and `dampingRatio` parameters
- `Crossfade` — verify `animationSpec` parameter

### Step 6.3: Verify recomposition stability

- All `@Composable` functions with unstable parameters should use `key()` or `remember`
- No `mutableStateOf` inside composables that should be hoisted
- No `LaunchedEffect` with changing keys that cause infinite loops

### Step 6.4: Final integration verification

Run the full integration checklist from SUMMARY.md:
1. Every ViewModel method called from UI exists
2. Every route has a destination
3. Every callback is wired
4. No orphaned components
5. No missing imports

### Step 6.5: Commit and verify CI

```bash
git add -A
git commit -m "Phase 6: Polish — verify animations and stability"
git push
gh run watch  # MUST show BUILD SUCCESSFUL
```
