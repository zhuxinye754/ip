#!/usr/bin/env python3
"""Run the Clover UI test cases declared in test/ui-test-plan.md."""

from __future__ import annotations

import re
import subprocess
import sys
import tempfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[4]
PLAN = ROOT / "test" / "ui-test-plan.md"
CASE_PATTERN = re.compile(
    r"^## Test case: (?P<name>.+?)\n"
    r"\*\*Aim:\*\* (?P<aim>.+?)\n\n"
    r"(?:\*\*Saved data:\*\*\n```text\n(?P<saved_data>.*?)\n```\n\n)?"
    r"\*\*Input:\*\*\n```text\n(?P<input>.*?)\n```\n\n"
    r"\*\*Expected output:\*\*\n```text\n(?P<expected>.*?)\n```",
    re.MULTILINE | re.DOTALL,
)


def normalise(text: str) -> str:
    """Make line endings comparable while preserving the visible console output."""
    return text.replace("\r\n", "\n").rstrip("\n")


def show_block(title: str, content: str) -> None:
    """Print a labelled console transcript block."""
    print(f"--- {title} ---")
    print(content)
    print(f"--- end {title.lower()} ---")


def main() -> int:
    """Compile Clover and run plan cases in order, stopping on the first failure."""
    if not PLAN.is_file():
        print(f"Test plan not found: {PLAN}", file=sys.stderr)
        return 2

    java_version = subprocess.run(
        ["java", "-version"], text=True, capture_output=True, check=False
    ).stderr
    if "version \"25." not in java_version:
        print("Java 25 is required. Activate Java 25 and run this command again.", file=sys.stderr)
        return 2

    cases = list(CASE_PATTERN.finditer(PLAN.read_text()))
    if not cases:
        print("No valid test cases found in test/ui-test-plan.md", file=sys.stderr)
        return 2

    with tempfile.TemporaryDirectory(prefix="clover-ui-tests-") as classes:
        compilation = subprocess.run(
            ["javac", "-d", classes, *map(str, (ROOT / "src/main/java").glob("*.java"))],
            text=True,
            capture_output=True,
            check=False,
        )
        if compilation.returncode:
            print(compilation.stderr, file=sys.stderr)
            return compilation.returncode

        for number, case in enumerate(cases, start=1):
            name = case["name"]
            console_input = case["input"] + "\n"
            expected = normalise(case["expected"])
            with tempfile.TemporaryDirectory(prefix="clover-ui-case-") as case_directory:
                saved_data = case["saved_data"]
                if saved_data is not None:
                    data_directory = Path(case_directory) / "data"
                    data_directory.mkdir()
                    (data_directory / "duke.txt").write_text(saved_data + "\n")
                run = subprocess.run(
                    ["java", "-cp", classes, "Clover"],
                    input=console_input,
                    text=True,
                    capture_output=True,
                    check=False,
                    cwd=case_directory,
                )
            actual = normalise(run.stdout + run.stderr)

            if run.returncode or actual != expected:
                print(f"FAIL: Test case {number} — {name}")
                show_block("Console input", console_input.rstrip("\n"))
                show_block("Expected output", expected)
                show_block("Actual output", actual)
                return 1

            print(f"PASS: Test case {number} — {name}")
            show_block("Console input", console_input.rstrip("\n"))
            show_block("Console output", actual)

    print(f"All {len(cases)} UI test case(s) passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
