---
name: present-changes-visually
description: Generate a self-contained, GitHub-style split-view HTML page showing changes in this project. Use when asked to show, review, share, or inspect this project's code changes visually, or create an HTML diff.
---

# Present Changes Visually

Generate one interactive HTML page containing each changed file as a side-by-side before/after diff. The page folds long unchanged runs, highlights changed words within modified lines, lets readers filter files, and includes collapsed panels for unchanged files.

## Generate the page

1. Use this repository unless the user explicitly identifies another repository.
2. Use `HEAD` as the before point and `WORKTREE` as the after point unless the user specifies comparison points. `WORKTREE` includes staged, unstaged, and untracked files, but excludes ignored files.
3. Write the result to `_temp/visual-diff.html` unless the user gives an output path.
4. From the repository root, run:

   ```bash
   python3 .codex/skills/present-changes-visually/scripts/generate-split-view-diff.py \\
     . HEAD WORKTREE _temp/visual-diff.html
   ```

   Replace the revisions and output path when requested. Revisions may be a Git commit-ish such as `HEAD~1`, a tag, a branch, or a commit SHA.

5. Confirm that generation succeeded, check the reported changed-file count, and give the user the absolute path to the page. Do not open it unless they ask.

## Visual review

When the user asks to visually inspect the result, open or render the generated page with an available browser tool. If the page has no syntax coloring because it cannot reach the optional highlighting CDN, treat it as valid: the diff itself remains self-contained.

## Resource

`scripts/generate-split-view-diff.py` is the project-local, standard-library-only generator.
