# UI Rewrite — Quick Reference

## YAGNI / KISS — The Only Rules That Matter

**YAGNI:** Don't build it unless you need it NOW.
**KISS:** The simplest solution that works is the best solution.

### Concrete YAGNI Rules for This Project
1. **No generic settings components** — inline each setting directly in the screen
2. **No adaptive scaffold** — phone-only, tablet later
3. **No Motion.kt** — use `200f` not `DurationShort`, spring with literals
4. **No empty state components** — just a `Text("No games")` inline
5. **No skeleton card component** — inline a placeholder Box
6. **No bridge layer in ViewModel** — wire `appState` directly, old screens stay on old state
7. **No "reusable" components used once** — if it appears in one screen, inline it
8. **No comments unless asked** — code should be self-documenting

### Concrete KISS Rules for Subagents
1. **Verify API via MCP** → **Write minimal file** → **Check signature matches caller** → **Done**
2. Don't add imports you don't use
3. Don't add parameters you don't need
4. Don't add error handling beyond what's specified
5. Don't add animations/transitions unless explicitly in the plan
6. Don't create "helper" functions — just write the composable directly

---

## Workflow

```
Phase N:
  1. Read phase plan
  2. For each file: spawn subagent (verify API → write file → verify connection)
  3. Commit → Push → gh run watch
  4. If CI fails: fix → retry
  5. Only proceed after CI passes
```

## Phases

| # | File | Goal | Files Created |
|---|------|------|---------------|
| 1 | `01-THEME.md` | Colors, typography, shapes, theme | Color.kt, Type.kt, Shape.kt, Theme.kt |
| 2 | `02-STATE.md` | AppState decomposition | AppState.kt + sub-states, ViewModel edits |
| 3 | `03-NAVIGATION.md` | Routes, nav bar, AppNavigation | Routes.kt, NavBar.kt, AppNavigation.kt, MainActivity edits |
| 4 | `04-SCREENS.md` | All screens + components | LoginScreen, HomeScreen, LibraryScreen, SettingsScreen, GameCard, UrlImage |
| 5 | `05-STREAM.md` | Move existing stream code | Move from OpenNowScreens.kt to stream/ package |
| 6 | `07-CLEANUP.md` | Delete old file, verify | Delete OpenNowScreens.kt |

> Phase 6 (Polish) merged into other phases. Do animations inline where needed.

## Integration Check (After Every Phase)

```bash
# 1. All ViewModel methods called from UI exist
grep -rn "viewModel\." app/src/main/java/com/opencloudgaming/opennow/ui/ | grep -oP 'viewModel\.\w+' | sort -u

# 2. All routes have destinations
grep -rn "Route\." app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt | grep "composable<"

# 3. No orphaned imports
# 4. All callback parameters match between call site and definition
```

## CI Workflow

```bash
git add -A && git commit -m "Phase N: [name]" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
```

## Key Files (Don't Modify)

- `GfnApi.kt` — API calls
- `Streaming.kt` — WebRTC + input
- `Models.kt` — data classes
- `Persistence.kt` — storage
- `QrCode.kt` — QR generation

## Key Files (Will Modify)

- `OpenNowViewModel.kt` — add appState
- `MainActivity.kt` — update theme + navigation
- `OpenNowScreens.kt` — delete at end
