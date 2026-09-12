# Clover User Guide

Clover helps private tutors maintain a task list and a directory of tutorees.

## Tutoree directory

Each tutoree has a name, tutoring address, and fee arrangement.

### Add a tutoree

```text
add-tutoree NAME /address ADDRESS /fee FEE
```

Example:

```text
add-tutoree Alice Tan /address 12 Example Road /fee $50/hour
```

Names must be unique regardless of capitalization. Name, address, and fee cannot be blank.

### List tutorees

```text
list-tutorees
```

The command displays every tutoree with their address and fee.

### Find a tutoree

```text
find-tutoree KEYWORD
```

The keyword partially matches student names without regard to capitalization.

```text
find-tutoree ali
```

## Link tasks to a tutoree

Optionally append `/for TUTOREE_NAME` to a task-creation command. The tutoree must already be in the directory.

```text
todo prepare worksheet /for Alice Tan
deadline collect fee /by 2026-09-30 /for Alice Tan
event lesson /from 2026-09-20 /to 2026-09-20 /for Alice Tan
```

Linked tasks show the tutoree in task lists, for example:

```text
[D] [ ] collect fee (by: Sep 30 2026) (for: Alice Tan)
```

If no matching tutoree exists, Clover reports:

```text
No tutoree named "Alice Tan" exists. Add the tutoree before linking a task to them.
```

## Saved data

Tasks continue to use `data/clover.txt`. Existing task records remain compatible; linked tasks append the tutoree name as their final field.

```text
T | 0 | prepare worksheet | Alice Tan
D | 0 | collect fee | 2026-09-30 | Alice Tan
E | 0 | lesson | 2026-09-20 | 2026-09-20 | Alice Tan
```

Tutorees are saved separately in `data/tutorees.txt`.

```text
S | Alice Tan | 12 Example Road | $50/hour
```
