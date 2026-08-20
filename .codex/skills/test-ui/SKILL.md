---
name: test-ui
description: Run the Clover command-line UI test plan, comparing each command session with its expected console output.
---

# Test UI

Use this skill when asked to test or verify Clover's command-line interface.

The source of truth for UI cases is [test/ui-test-plan.md](../../../test/ui-test-plan.md). Each case must have:

- a `## Test case:` heading;
- an **Aim**;
- an **Input** fenced `text` block containing one or more user commands, ending in `bye`;
- an **Expected output** fenced `text` block containing the complete expected console output.

Run the plan with:

```sh
python3 .codex/skills/test-ui/scripts/run_ui_tests.py
```

The runner verifies that Java 25 is active, compiles the program to a temporary directory, and runs every case in plan order. It prints a console-input/console-output transcript for each passing case. It stops at the first failure, shows that case's input, expected output, and actual output, and exits with a non-zero status.

After each code update, review the plan and update it when the change affects command inputs, expected console output, or UI behaviour; then run this skill before handing the change back. Do not alter expected output merely to hide an unexpected regression; confirm that the new behaviour is intended.
