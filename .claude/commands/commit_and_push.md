# Commit and Push

Generate a commit message, create a commit, and push to the remote branch.

## Steps

1. Run `git status` to see all changed and untracked files (never use -uall flag).
2. Run `git diff` to review all staged and unstaged changes.
3. Run `git log --oneline -5` to understand the existing commit message style.
4. Analyze the changes and generate a concise, imperative commit message that explains *why*, not just *what*.
   - First line: short summary (under 72 characters), imperative mood ("Add", "Fix", "Update", not "Added")
   - If more context is needed, leave a blank line and add bullet points
   - Do NOT mention Claude or AI generation in the message
5. Stage all relevant files with `git add`, including:
   - Modified/deleted tracked files
   - Untracked new files visible in `git status` output (lines starting with `??`)
   Avoid `git add -A` if there are sensitive files; instead add files by name or directory.
6. Commit using the generated message.
7. Push to the current remote branch with `git push`.

## Rules

- If there are no changes to commit, say so and stop.
- Do NOT skip pre-commit hooks (`--no-verify`).
- Do NOT force push.
- If `$ARGUMENTS` is provided, treat it as additional context or a hint for the commit message.

$ARGUMENTS