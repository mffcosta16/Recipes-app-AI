---
name: create-issue
description: Drafts and creates a single GitHub issue (a use case, task, or bug) using this project's canonical .github/ISSUE_TEMPLATE.md, checking for duplicates and confirming the draft with the user before creating it via gh issue create. Use when the user asks to create/file an issue, or to turn a use case into an issue.
---

# Create Issue

Use when the user wants a single GitHub issue created — e.g. for a use case from the domain model, a
task, or a bug. To turn several use cases into issues, run this once per use case, confirming each
before creating it.

## 1. Gather context

- **From the user**: what they want built, fixed, or changed — and why it matters. Ask if it isn't
  already clear from the conversation.
- **From the codebase**: run `git log --oneline -20` and check the relevant area of code so the issue
  body reflects accurate technical context. For a domain use case, read the relevant aggregate under
  `src/main/java/com/recipes/domain/**` if it exists, or `README.md`'s Domain model section / `CLAUDE.md`
  if it doesn't yet. Note what's already in place vs. what's missing.
- **From GitHub**: check for duplicates (step 2) before drafting further.

## 2. Check for duplicates

`gh issue list --repo mffcosta16/Recipes-app-AI --state all --search "<keywords from the request>"`

If potential duplicates are found, show them to the user with title, number, and state. Let the user
decide whether to proceed — don't block, just inform.

## 3. Draft the issue

Use `.github/ISSUE_TEMPLATE.md` as the source of truth for structure — read it
(`cat .github/ISSUE_TEMPLATE.md`) rather than relying on a cached copy, in case it has changed. Fill in:

- **Context**: 1-2 sentences on what this is and why it matters — the current state, the desired state,
  and the gap between them. Pull specifics from the codebase (class/file names, aggregate involved) so
  the implementer has a head start. Note what's out of scope if that helps keep the issue focused.
- **Tasks**: concrete steps needed to implement it.
- **Acceptance criteria**: checkboxes, each a binary pass/fail condition, 3-7 items. Avoid vague criteria
  like "works well" — prefer concrete ones like "publishing a recipe with zero ingredients is rejected".
  For a domain use case this usually maps directly to the invariants/behavior involved.

**Title**: start with a verb ("Add...", "Fix...", "Update...", "Remove..."), under 70 characters, and
specific — "Add recipe publish lifecycle with draft/published/archived states" not "Recipe status".

## 4. Confirm with the user

Show the drafted issue (title + full body) and ask for confirmation before creating it. The user might
want to tweak wording, add criteria, or adjust scope.

## 5. Create the issue

```
gh issue create --repo mffcosta16/Recipes-app-AI \
  --title "<title>" \
  --label "<label>" \
  --body "<body>"
```

**Label guidance**: default to `enhancement` for new use cases/features. Use `bug` only if it's clearly a
bug (something that used to work and doesn't, or behavior contradicting an existing issue's acceptance
criteria). Check `gh label list --repo mffcosta16/Recipes-app-AI` first; don't invent new labels without
asking.

After creation, share the issue URL with the user, and remind them: this issue should be referenced in
the commit(s) that implement it, e.g. `Refs #<number>` or `Closes #<number>`.

## Notes

- Never close or delete existing issues as part of this skill.
- If creating issues for multiple use cases in one request, list all the drafts and get one confirmation
  covering the batch, then create them — don't create any before confirming.