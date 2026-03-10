---
name: review
description: Review code changes with OpenAI Codex CLI against project standards (test presence, unit tests, K8s best practices)
argument-hint: [--base <branch> | --commit <sha>]
allowed-tools: Bash, Read, Grep, Glob
---

# Codex Code Review

Run an OpenAI Codex review on the current changes. Default reviews against `main` branch.

## Usage examples

- `/review` — review changes vs main (default)
- `/review --base main` — review changes vs main branch
- `/review --commit HEAD` — review the last commit
- `/review --commit abc1234` — review a specific commit

## Execution

Determine the review scope from arguments. If no arguments are provided, default to `--base main`.

Note: `codex review` does NOT accept both `--base` and a prompt argument simultaneously. Use `--base` or `--commit` only.

Run the following command:

```bash
npx @openai/codex review $ARGUMENTS
```

After the review completes, summarize the results to the user with PASS/WARN/FAIL for each of these categories:

1. **TEST PRESENCE**: Does every changed production class have a corresponding test class?
2. **UNIT TEST QUALITY**: Do tests cover success/failure paths, use proper assertions, mock external deps?
3. **Transaction**: No external api calls within transaction. always mark which transactionManager is used in `@Transaction` annotation
4. **Concurrency Problem**: if concurrency problem is showed, suggest solution, such as redis pub/sub, Optimistic Lock, Pessimistic Lock, etc

## Post-review: Add comment to PR

After summarizing the results to the user, automatically post the review as a PR comment.

1. Find the PR number for the current branch:
   ```bash
   gh pr view --json number --jq '.number'
   ```
2. If a PR exists, post the full review summary (category table + findings) as a comment:
   ```bash
   gh pr comment $PR_NUMBER --body "$REVIEW_SUMMARY"
   ```
3. If no PR exists, skip the comment and inform the user.
