# Orchestration Guide — 3-Tier Architecture

## How It Works

```
Main Orchestrator (you)
├── Reads: .plan/ORCHESTRATION.md (this file)
├── Reads: .plan/00-CONTEXT.md (shared context: deps, gotchas, decisions)
├── Spawns: Phase Orchestrators (one per phase, sequentially)
│   ├── Reads: .plan/0N-PHASE_NAME.md (its phase plan only)
│   ├── Spawns: Subagents (one per file/task)
│   │   └── Reads: single file creation/edit task
│   └── Returns: build verification result
└── Verifies: build passes after each phase
```

## Main Orchestrator Instructions

You are the main orchestrator. Your job:

1. Read this file (ORCHESTRATION.md)
2. Read `.plan/00-CONTEXT.md` for shared project context
3. For each phase (1, 2, 3, 4a, 4b, 4c, 5, 6, 7):
   a. Spawn a **Phase Orchestrator** with the phase plan
   b. Wait for it to complete
   c. Commit the phase changes
   d. Push to trigger CI
   e. Run `gh run watch` — wait for CI to finish
   f. If CI fails, run `gh run view --log-failed`, fix the issue, commit, push, watch again
   g. Only proceed to next phase after CI passes
4. Report final status

### Spawning a Phase Orchestrator

Use the Task tool with `subagent_type: general`. Pass:
- The phase plan file content (from `.plan/0N-PHASE_NAME.md`)
- The shared context (from `.plan/00-CONTEXT.md`)
- A clear instruction: "Execute this phase. Create/edit the files specified. Do NOT run build commands."

### Example Main Orchestrator Prompt

```
You are a Phase Orchestrator for Phase N: [PHASE NAME].

SHARED CONTEXT:
[paste contents of .plan/00-CONTEXT.md]

YOUR PHASE PLAN:
[paste contents of .plan/0N-PHASE_NAME.md]

YOUR JOB:
1. Read the phase plan above
2. For each Step in the plan, spawn a Subagent to create/edit the file
3. Wait for each subagent to complete before starting the next step
4. Do NOT run build commands — the main orchestrator handles that
5. Return a summary of what was created/edited

SPAWN SUBAGENTS:
- Use Task tool with subagent_type: "general"
- Each subagent gets ONE file to create/edit
- Pass the exact code from the plan to the subagent
- Verify the subagent succeeded before moving on
```

## Phase Orchestrator Instructions

You are a Phase Orchestrator. Your job:

1. Read the phase plan passed to you
2. **Verify APIs using MCP** before spawning subagents — use `android-docs-mcp` to check API signatures
3. For each Step in the plan:
   a. Spawn a **Subagent** to create/edit the file
   b. Wait for completion
   c. If the subagent fails, read the error and retry or fix manually
4. Return a summary: files created, files edited, any issues

### Spawning a Subagent

Use the Task tool with `subagent_type: general`. Pass:
- The exact file path to create/edit
- The exact code content from the plan
- Instructions: "Create this file with the exact content below. Do NOT run build commands."

### Example Phase Orchestrator Prompt

```
You are executing Phase 1: Design System + Theme.

YOUR STEPS:
1. Create directory: app/src/main/java/com/opencloudgaming/opennow/ui/theme
2. Create file: Color.kt with this content: [code]
3. Create file: Type.kt with this content: [code]
4. Create file: Shape.kt with this content: [code]
5. Create file: Motion.kt with this content: [code]
6. Create file: Theme.kt with this content: [code]

For each step:
- Spawn a subagent to create the file
- Wait for completion
- If it fails, fix it manually

VERIFY APIs:
- Use android-docs MCP to verify Material3 APIs before writing code
- Use kotlin-ls LSP to catch compile errors after writing

DO NOT run ./gradlew or any build commands.
Return a summary when done.
```

## Subagent Instructions

You are a Subagent. Your job:

1. Read the file path and content passed to you
2. Create the file at the specified path with the exact content
3. If editing an existing file, make the specified edit
4. Return success or error

### Example Subagent Prompt

```
Create the file at this path:
app/src/main/java/com/opencloudgaming/opennow/ui/theme/Color.kt

With this exact content:
[code block]

VERIFY FIRST:
- Use android-docs MCP to verify any Android/Compose APIs in the code
- Check parameter names, return types, nullability

DO NOT run any build commands.
Return "Created [path]" on success.
```

## Phase Order

| Phase | Plan File | Description |
|-------|-----------|-------------|
| 1 | `.plan/01-THEME.md` | Design system: colors, typography, shapes, motion, theme |
| 2 | `.plan/02-STATE.md` | State decomposition: AppState, AuthState, CatalogState, etc. |
| 3 | `.plan/03-NAVIGATION.md` | Navigation: Routes, bars, scaffold, AppNavigation, MainActivity |
| 4a | `.plan/04-SCREENS.md` (Steps 4.1-4.8) | Components only: UrlImage, GameCard, GameGrid, HeroBanner, SearchField, EmptyState, SkeletonCard |
| 4b | `.plan/04-SCREENS.md` (Steps 4.9-4.11) | Screens: LoginScreen, HomeScreen, LibraryScreen |
| 4c | `.plan/04-SCREENS.md` (Steps 4.12-4.14 + expansions) | Settings + GameDetails + Store: SettingsComponents, SettingsScreen, GameDetailsSheet, StoreComponents, TvDeviceLogin, Accent migration |
| 5 | `.plan/05-STREAM.md` | Stream: StreamControls, StreamScreen, queue UI, touch controls |
| 6 | `.plan/06-POLISH.md` | Polish: animations, recomposition, stability |
| 7 | `.plan/07-CLEANUP.md` | Cleanup: delete old file, update references |

> **Phase 4 is split into 3 sub-phases** to keep each orchestrator under 10k tokens.
>
> When spawning Phase 4 orchestrators, pass these step ranges:
> - **4a:** Steps 4.1-4.8 (components only)
> - **4b:** Steps 4.9-4.11 (LoginScreen, HomeScreen, LibraryScreen)
> - **4c:** Steps 4.12-4.14 + all expansion sections (SettingsScreen, GameDetailsSheet, StoreComponents, TvDeviceLogin, Accent migration)

## Build Verification (CI Only)

**No local builds.** Builds happen on GitHub Actions only.

After each phase completes, the main orchestrator does:

1. **Static checks only** — verify file structure, imports, and type references are correct
2. **Commit the phase** — push to trigger CI
3. **Wait for CI result** — check GitHub Actions for build status
4. **If CI fails:**
   a. Read the error output from CI (last 100 lines shown in summary)
   b. Fix the issue (usually missing imports, wrong API, or typos)
   c. Commit and push again
   d. Only proceed to next phase after CI passes

### Static Verification (No Build Required)

Before committing each phase, verify:

- [ ] All new files exist at the correct paths
- [ ] All imports reference types that exist in `Models.kt` or standard libraries
- [ ] All composable function signatures match the plan
- [ ] No references to deleted/renamed types
- [ ] `package` declarations match directory structure
- [ ] LSP diagnostics show no errors (if `kotlin-ls` is available)

### GitHub Actions Workflow

```bash
# After phase completes, commit and push
git add -A
git commit -m "Phase N: [phase name]"
git push

# Watch CI until it finishes (blocks until done)
gh run watch

# If it failed, get the error logs
gh run view --log-failed
```

**The main orchestrator must run `gh run watch` after every push.** This blocks until CI finishes. If it fails, run `gh run view --log-failed` to get the exact error, then fix and retry.

## Critical Rules

1. **NEVER run build commands locally** — all builds via GitHub Actions
2. **One subagent per file** — don't bundle multiple files in one subagent
3. **Wait for each step** — phases are sequential within themselves
4. **Phases are sequential** — don't start Phase 2 until Phase 1 CI passes
5. **Read skills before each phase** — the phase plan lists which skills to read
6. **Verify against Models.kt** — if a plan references a type, confirm it exists before creating code
7. **Commit after each phase** — one commit per phase for clean CI history
