---
name: create-pr
description: Creates a PR from the current feature branch to main (title/body drafted per this project's conventions, confirmed with the user before creation), runs code review (general /code-review plus the project-specific recipe-architecture-reviewer agent for Java changes), and merges only after explicit user confirmation. Use when the user asks to open a PR, ship a branch, or merge to main.
---

# Create PR

Use when the user wants to open a pull request from the current branch, get it reviewed, and (eventually)
merged into `main`.

## 1. Confirm branch state

`git status --short` — if there are uncommitted changes, don't silently include or discard them; ask the
user how to handle them first. `git log main..HEAD --oneline` to see what's actually being shipped, and
check which issue number the branch/commits reference (per the use-case-issues convention: commits
reference their issue with `Refs #N` / `Closes #N`).

## 2. Push

Ensure the branch is pushed and current (`git push`, or `git push -u origin <branch>` if it has no
upstream yet).

## 3. Draft the PR

**Title format**: `#<issue-number> - <short description>`
- Always start with `#` followed by the issue number.
- Follow with ` - ` (space-dash-space) as separator.
- Keep the description under 60 characters.
- Use lowercase, imperative mood: "add", "fix", "update", "remove".
- Examples: `#6 - bootstrap spring boot project and domain layer`, `#12 - fix authentication redirect loop`

If the branch/commits don't clearly reference an issue number, ask the user which issue this PR closes
before drafting the title.

**Body**: the repository has a canonical PR template at `.github/pull_request_template.md` — read it
(`cat .github/pull_request_template.md`) and use its current contents as the source of truth, not a
cached copy, in case it has changed. Fill in each section:

- **Summary**: 1-3 bullets giving a reviewer enough context without reading every line of code, plus
  `Closes #<issue-number>` so the issue auto-closes on merge.
- **Type of change**: check exactly the box(es) that apply.
- **Changes description**: highlight what's meaningful (new dependencies, architectural choices, API
  changes) — don't list every file.
- **How has this been tested?**: check what was actually done, with a short note on concrete steps a
  reviewer can follow.
- **Checklist**: work through it honestly based on what was actually done, not by default-checking
  everything.

## 4. Confirm with the user

Show the drafted PR (title + full body) and ask for confirmation before creating it. The user might want
to adjust scope, add context, or change the test plan. Only run `gh pr create` after they confirm.

## 5. Review

- Run `/code-review` for general correctness/simplification, and invoke the `recipe-architecture-reviewer`
  agent for anything touching `domain/`, `application/`, `infrastructure/`, or `web/` Java code.
- Summarize findings for the user. If there are unresolved high-severity findings, say so plainly and ask
  whether to fix them before proceeding — never merge over unresolved correctness issues without the user
  explicitly choosing to anyway.
- Post the findings as a `gh pr comment` on the PR, whether or not anything was found (a clean review is
  worth recording too). One consolidated comment per review pass, covering both the general review and the
  architecture review, structured roughly as: what each review checked, what it found, and — if anything
  was fixed as a result — a short note of what changed and in which commit. Do this automatically as part
  of the review step; it doesn't need separate confirmation, since posting review results is what this step
  is for. If findings get fixed and you re-review, post a follow-up comment rather than editing the
  original.

## 6. Merge — only with explicit confirmation, every time

- Never call `gh pr merge` without the user explicitly confirming *in this instance*, even if review
  passed clean and even if they approved merging on a prior PR — that approval doesn't carry forward.
- Default merge strategy is squash unless the user says otherwise.
- After merging, report the result and ask whether to delete the local and remote feature branch.

## Notes

- Never force-push, never bypass CI checks, never merge without an explicit go-ahead in the current
  request.
