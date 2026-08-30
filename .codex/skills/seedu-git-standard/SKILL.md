---
name: seedu-git-standard
description: Apply the project's SE-EDU Git conventions when naming branches and preparing commit messages.
---

# SE-EDU Git standard

Apply the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html) whenever preparing a commit or creating a branch in this repository.

## Commit messages

- Write a clear subject in imperative mood. Capitalize its first letter, keep it to 50 characters when practical (never over 72), and do not end it with a period.
- Use an optional `scope:` or `category:` prefix only when it helps identify the affected area or change type.
- For a non-trivial change, include a body separated from the subject by one blank line. Wrap body lines at 72 characters and separate paragraphs with blank lines.
- Explain what changed and why, not implementation mechanics that the diff already shows. Give enough context for a reviewer to assess the change without reading the diff.
- If an explanation becomes lengthy or spans unrelated changes, split the work into finer-grained commits where practical.

## Branch names

- Use a meaningful kebab-case name made from relevant keywords, such as `refactor-ui-tests`.
- For issue-related work, use `issueNumber-keywords-from-issue-title`, such as `1234-ui-freeze-error`.

## Before committing

Review the staged diff and confirm the message accurately describes only the staged changes. Do not create a commit, amend a commit, or push unless the user has explicitly authorized that action.
