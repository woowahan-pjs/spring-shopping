#!/bin/bash
# codex-review.sh — PostToolUse hook that triggers OpenAI Codex review
# Only runs when a PR exists for the current branch.
# Triggers on: gh pr create, or git commit when PR already exists.

set -euo pipefail

DEFAULT_BASE="staging/project"

# Resolve the origin repo (fork) for gh commands
ORIGIN_REPO=$(git remote get-url origin 2>/dev/null | sed -n 's|.*github.com[:/]\(.*\)\.git|\1|p')
if [[ -z "$ORIGIN_REPO" ]]; then
ORIGIN_REPO=$(git remote get-url origin 2>/dev/null | sed -n 's|.*github.com[:/]\(.*\)|\1|p')
fi
GH_REPO_FLAG=""
if [[ -n "$ORIGIN_REPO" ]]; then
GH_REPO_FLAG="--repo $ORIGIN_REPO"
fi

INPUT=$(cat)
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // empty')
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // empty')

# Only act on Bash tool calls
if [[ "$TOOL_NAME" != "Bash" ]]; then
exit 0
fi

# Get current branch name
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD 2>/dev/null || echo "")

# Helper: check if a PR exists for the current branch on origin (fork)
pr_exists() {
if [[ -z "$CURRENT_BRANCH" ]]; then
return 1
fi
gh pr view "$CURRENT_BRANCH" $GH_REPO_FLAG --json number 2>/dev/null | jq -e '.number' >/dev/null 2>&1
}

# Detect gh pr create → always run review on new PR
if [[ "$COMMAND" =~ gh\ pr\ create ]]; then
# Determine base branch from command, fallback to default
BASE="$DEFAULT_BASE"
if [[ "$COMMAND" =~ --base[[:space:]]+([a-zA-Z0-9_./-]+) ]]; then
BASE="${BASH_REMATCH[1]}"
fi

echo "--- Codex Review (new PR against $BASE) ---" >&2
npx @openai/codex review --base "$BASE" 2>&1 || echo "[codex-review] review failed (non-fatal)" >&2
exit 0
fi

# Detect git commit → only run review if a PR already exists
if [[ "$COMMAND" =~ git\ commit ]]; then
if pr_exists; then
# Get the PR base branch from origin, fallback to default
BASE=$(gh pr view "$CURRENT_BRANCH" $GH_REPO_FLAG --json baseRefName -q '.baseRefName' 2>/dev/null || echo "$DEFAULT_BASE")

echo "--- Codex Review (commit added to existing PR, base: $BASE) ---" >&2
npx @openai/codex review --base "$BASE" 2>&1 || echo "[codex-review] review failed (non-fatal)" >&2
fi
exit 0
fi

exit 0
