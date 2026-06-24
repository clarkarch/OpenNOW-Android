# UI Rewrite — Quick Reference

## Workflow

```
Phase N:
  1. Read skills (compose-*, material-3, kotlin-*)
  2. Verify ALL APIs via MCP before writing code
  3. Create files via subagents
  4. INTEGRATION CHECK: verify all callbacks/routes/params connect
  5. Commit → Push → gh run watch → CI passes?
  6. If fails: read errors → fix → retry
  7. Only proceed after CI passes AND integration check passes
```

## CRITICAL RULES

### API Verification (MANDATORY)
Before writing ANY code in a sub-agent prompt, you MUST:
1. Use `android-docs_search_android_docs` to find the correct API
2. Use `android-docs_get_api_reference` to get exact method signatures
3. Include the verified signature in the sub-agent prompt
4. NEVER assume an API exists — always verify first

### Integration Check (MANDATORY after every phase)
After creating files, verify:
1. **Every composable called from AppNavigation has matching parameters** — read the screen file, check the function signature, check the call site
2. **Every ViewModel method called from UI exists** — grep for `viewModel.xxx()` and verify `xxx()` exists in ViewModel
3. **Every navigation route has a composable destination** — check Route sealed interface vs NavHost destinations
4. **Every imported symbol resolves** — no missing imports
5. **No orphaned components** — if you create a component, it MUST be called somewhere

### Sub-Agent Prompt Requirements
Every sub-agent prompt MUST include:
1. The exact file path to create/edit
2. The verified API signatures (from MCP)
3. The exact parameters expected (from reading the calling code)
4. A list of imports that MUST be present
5. An explicit instruction: "Do NOT use APIs without verifying them first via MCP"

## Rules

- **NO local builds** — all builds via GitHub Actions
- **One phase at a time** — wait for CI before next phase
- **One subagent per file** — don't bundle multiple files
- **Read skills before each phase** — listed in each phase file
- **NEVER skip integration check** — this is what caused the last failure

## Phases

| # | File | Goal | Integration Check |
|---|------|------|-------------------|
| 1 | `.plan/01-THEME.md` | Colors, typography, shapes, motion, theme | Verify theme params match M3 API |
| 2 | `.plan/02-STATE.md` | AppState, AuthState, CatalogState, etc. | Verify state bridge maps correct legacy fields |
| 3 | `.plan/03-NAVIGATION.md` | Routes, bars, scaffold, AppNavigation | Verify ALL routes have destinations, ALL callbacks wired |
| 4a | `.plan/04-SCREENS.md` (4.1-4.8) | Components: UrlImage, GameCard, GameGrid, etc. | Verify each component is used in a screen |
| 4b | `.plan/04-SCREENS.md` (4.9-4.11) | LoginScreen, HomeScreen, LibraryScreen | Verify params match AppNavigation call sites |
| 4c | `.plan/04-SCREENS.md` (4.12+) | SettingsScreen, GameDetailsSheet | Verify params match AppNavigation call sites |
| 5 | `.plan/05-STREAM.md` | StreamControls, StreamScreen, queue UI | Verify NativeStreamClient wiring, verify all APIs |
| 6 | `.plan/06-POLISH.md` | Animations, recomposition, stability | Verify all animations use correct APIs |
| 7 | `.plan/07-CLEANUP.md` | Delete old file, update references | Verify NO references to deleted code remain |

## Tools Available

| Tool | Purpose | When |
|------|---------|------|
| MCP (android-docs) | Verify API signatures | **BEFORE writing code — MANDATORY** |
| CI (./gradlew) | Compilation checking | **AFTER writing code — MANDATORY** |
| Skills | Patterns/anti-patterns | Before each phase (at `~/.agents/skills/`) |

## CI Workflow

```bash
git add -A
git commit -m "Phase N: [name]"
git push
gh run watch  # Wait for CI — MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
# NEVER proceed if CI fails
```

## Error Handling

1. CI fails → read last 100 lines from summary
2. Common issues: missing imports, wrong API, typos, wrong parameter names
3. Fix → commit → push → watch again
4. Only proceed after CI passes AND integration check passes

## Key Files (Don't Modify)

- `GfnApi.kt` — API calls
- `Streaming.kt` — WebRTC + input (reference only — do NOT rewrite)
- `Models.kt` — data classes
- `Persistence.kt` — storage
- `QrCode.kt` — QR generation

## Key Files (Will Modify)

- `OpenNowViewModel.kt` — add appState, bridge code
- `MainActivity.kt` — update theme + navigation
- `OpenNowScreens.kt` — delete at end

## Integration Checklist (Run After Every Phase)

```bash
# 1. Check all ViewModel methods called from UI exist
grep -rn "viewModel\." app/src/main/java/com/opencloudgaming/opennow/ui/ | grep -oP 'viewModel\.\w+' | sort -u

# 2. Check all routes have destinations
grep -rn "Route\." app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt | grep "composable<"

# 3. Check all imports resolve (no red squiggly in IDE)
# 4. Check no orphaned components (every created file is imported somewhere)
# 5. Check all callback parameters match between call site and definition
```

## Start

### Pre-requisites (do before executing)

1. Commit current changes:
```bash
git add -A
git commit -m "Add UI rewrite plan, MCP/LSP config, CI error capture"
git push
```

2. Verify CI passes (green checkmark on GitHub)

### Execute

Read this file → Read `.plan/00-CONTEXT.md` → Read `.plan/01-THEME.md` → Execute Phase 1
