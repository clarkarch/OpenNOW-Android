# AGENTS.md

## Rules

- **NEVER run build/compile commands locally.** No `./gradlew`, no `npm run build`, no compilation.
- **To verify builds:** Push to `fresh-start` branch, then `gh run watch` to monitor CI.
- Follow YAGNI: Only implement what is asked. No extras.
- Follow KISS: Simplest solution that works.
- Always use subagents for execution.
- No comments in code unless explicitly asked.
