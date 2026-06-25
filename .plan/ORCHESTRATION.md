# Orchestration Guide

## CRITICAL RULES — SUBAGENTS MUST FOLLOW

### Rule 1: ALWAYS Use MCP Tools Before Writing Code

Before writing ANY file, the subagent MUST:
1. Use `android-docs_search_android_docs` to verify the API exists
2. Use `android-docs_get_api_reference` to get exact class/function signatures
3. Use `android-docs_get_release_notes` if using a specific library version

**Why:** APIs change. Versions differ. Don't guess — verify.

**Example:**
```
# Before writing Coil 3 code:
android-docs_search_android_docs("coil3 compose AsyncImage")
android-docs_get_api_reference("coil3.compose.AsyncImage")

# Before writing Navigation Compose:
android-docs_search_android_docs("navigation compose NavHost")
android-docs_get_api_reference("androidx.navigation.compose.NavHost")

# Before writing ExposedDropdownMenuBox:
android-docs_search_android_docs("material3 ExposedDropdownMenuBox")
android-docs_get_api_reference("androidx.compose.material3.ExposedDropdownMenuBox")
```

### Rule 2: NEVER Run Local Build/Compile

- **NO** `./gradlew`
- **NO** `./gradlew assembleDebug`
- **NO** `./gradlew compileDebugKotlin`
- **NO** any gradle command at all
- **ALL** builds go through CI: `git push → gh run watch`
- If you need to verify something, use grep/read tools, not build commands

### Rule 2b: Use Simple `gh` Commands Only

- **USE:** `gh run list` — list recent runs
- **USE:** `gh run watch <id>` — watch a running build
- **USE:** `gh run view <id> --log-failed` — see what failed
- **NEVER USE:** `gh api` — too complex, error-prone, unnecessary
- **NEVER USE:** `gh run view` with custom jq queries
- **NEVER USE:** `gh api repos/...` — stick to simple `gh` CLI

### Rule 3: ALWAYS Verify Before Writing

For EVERY file the subagent creates:
1. **Verify API exists** via MCP tools
2. **Verify function signatures** match what callers expect
3. **Verify imports** will resolve (check existing import patterns)
4. **After writing**, run the grep checks from the phase plan

### Rule 4: Follow YAGNI / KISS Religiously

**YAGNI — Don't build it unless you need it NOW:**
- No generic components used once
- No "reusable" abstractions for one use case
- No extension functions for one call site
- No sealed classes for 2 variants
- No wrapper composables around a single widget

**KISS — The simplest solution that works:**
- Copy-paste existing code, change package
- Inline everything possible
- No indirection layers
- No "bridge" classes between old and new
- No helper functions — write the composable directly

**Concrete YAGNI violations to avoid:**
- Creating a `GameCard` component used in one screen → inline it
- Creating a `SearchBar` component used in two screens with different logic → inline both
- Creating a `SettingsSection` wrapper → just use `Column` with spacing
- Creating a `StreamControlSlider` for one slider → inline `Slider`

## Workflow

```
Phase N:
  1. Read phase plan (this file + phase-specific file)
  2. For each file in the plan:
     a. Use MCP to verify APIs
     b. Spawn subagent with exact prompt from plan
     c. Subagent writes file
     d. Subagent runs verify grep commands
     e. Subagent returns success
  3. Commit → Push → gh run watch
  4. If CI fails: gh run view --log-failed → fix → retry
  5. ONLY proceed after CI passes
```

## Subagent Prompt Template

When spawning a subagent for a file, use this template:

```
You are writing a file for an Android Kotlin/Compose project.

CRITICAL RULES:
- Use android-docs_search_android_docs and android-docs_get_api_reference MCP tools to verify APIs BEFORE writing
- Do NOT run any gradle/build commands
- Do NOT add comments unless asked
- Do NOT add error handling beyond what's specified
- Do NOT add animations/transitions unless explicitly in the plan
- Do NOT create helper functions — write composables directly
- Follow YAGNI: if it's used once, inline it
- Follow KISS: simplest solution that works

YOUR TASK:
[exact file path]
[exact function to extract or create]
[exact source lines to copy from]
[exact package declaration]
[exact imports needed]
[exact function signatures (public/private)]

VERIFY AFTER WRITING:
[specific grep commands to run]

Return SUCCESS with the grep results.
```

## Phase Order

| # | File | Goal |
|---|------|------|
| 1 | `01-THEME.md` | Extract theme |
| 2 | `02-DEPENDENCIES.md` | Add Coil 3 + Navigation Compose |
| 3 | `03-NAVIGATION.md` | Routes, NavBar, AppNavigation |
| 4 | `04-SCREENS.md` | Extract Login, Home, Library, Settings |
| 5 | `05-STREAM.md` | Move stream + rewrite overlay |
| 6 | `06-CLEANUP.md` | Delete remnants |

## Integration Check (After Every Phase)

```bash
# 1. No orphaned imports — check each file compiles conceptually
rg -l "import com.opencloudgaming.opennow" app/src/main/java/com/opencloudgaming/opennow/ui/ | while read f; do
  rg "import" "$f" | while read line; do
    file=$(echo "$line" | grep -oP 'import\s+\K[^\s]+' | tr '.' '/')
    # Check if the imported symbol exists in the target file
  done
done

# 2. All ViewModel methods called from UI exist
rg "viewModel\.\w+|viewModel::\w+" app/src/main/java/com/opencloudgaming/opennow/ui/ -o --no-filename | sort -u

# 3. All routes have destinations (if Phase 3+)
rg "composable<" app/src/main/java/com/opencloudgaming/opennow/ui/navigation/AppNavigation.kt

# 4. No duplicate function definitions
rg "^fun |^private fun " app/src/main/java/ --no-filename | sort | uniq -d

# 5. No local build artifacts
ls app/build/ 2>/dev/null && echo "WARNING: local build exists"
```

## CI Workflow

```bash
git add -A && git commit -m "Phase N: [name]" && git push
gh run watch  # MUST show BUILD SUCCESSFUL
# If fails: gh run view --log-failed → fix → retry
```

## Rules Summary

| Rule | Enforcement |
|------|-------------|
| Use MCP tools | Every subagent prompt includes this instruction |
| No local builds | Every phase plan has explicit "DO NOT BUILD" |
| YAGNI | Every phase plan has "YAGNI checklist" |
| KISS | Every phase plan has "KISS checklist" |
| Verify after write | Every phase plan has grep commands |
| CI only | Every phase ends with `gh run watch` |
