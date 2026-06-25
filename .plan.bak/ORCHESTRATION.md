# Orchestration Guide

## YAGNI / KISS Rules (MANDATORY)

### What We DON'T Build
- **No generic components** — if a component is used once, inline it
- **No adaptive layouts** — phone-only for MVP, tablet later
- **No animation tokens** — use literal values, extract later if repeated
- **No bridge layers** — just wire new state directly
- **No abstraction for the sake of abstraction**

### What We DO Build
- One file at a time, via one subagent
- Simple composables that do one thing
- Direct wiring — no indirection layers
- Delete code that isn't used

### Subagent Rules
1. **Verify API via MCP first** — `android-docs_search_android_docs` + `android-docs_get_api_reference`
2. **Write the file** — minimal, no extras
3. **Verify it connects** — check function signatures match callers
4. **Return success** — that's it

### What Subagents NEVER Do
- Never create "helper" abstractions not in the plan
- Never add animation/transition code not explicitly asked for
- Never add error handling beyond what's specified
- Never add documentation/comments unless asked
- Never create multiple files in one run
- Never run build commands

## Workflow

```
Phase N:
  1. Read phase plan
  2. For each file: spawn subagent (verify API → write file → verify connection)
  3. Commit → Push → gh run watch
  4. If CI fails: fix → retry
  5. Only proceed after CI passes
```

## Phase Order

| # | File | Goal |
|---|------|------|
| 1 | `01-THEME.md` | Colors, typography, shapes, theme |
| 2 | `02-STATE.md` | AppState decomposition |
| 3 | `03-NAVIGATION.md` | Routes, nav bar, AppNavigation |
| 4 | `04-SCREENS.md` | All screens + components |
| 5 | `05-STREAM.md` | Move existing stream code |
| 6 | `07-CLEANUP.md` | Delete old file, verify |

> Phase 6 (Polish) merged into other phases. Do it inline.

## CI Workflow

```bash
git add -A && git commit -m "Phase N: [name]" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
```
