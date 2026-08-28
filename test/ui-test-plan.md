# Clover UI test plan

The runner compiles the Java files in `src/main/java` and launches `Clover` for every case. Expected output is compared exactly, excluding only a final newline.

Each case uses an isolated working directory. A case can include an optional **Saved data** block to provide the contents of `data/clover.txt` before Clover starts.

## Test case: Load saved tasks

**Aim:** Verify that Clover restores saved tasks, including their task type and completion status, when it starts.

**Saved data:**
```text
T | 1 | read \| annotate book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2-4pm | Aug 6th 4-6pm
```

**Input:**
```text
list
bye
```

**Expected output:**
```text
____________________________________________________________
  _____    _         ____    __      __   ______    _____
 / ____|  | |       / __ \   \ \    / /  |  ____|  |  __ \
| |       | |      | |  | |   \ \  / /   | |__     | |__) |
| |       | |      | |  | |    \ \/ /    |  __|    |  _  /
| |____   | |____  | |  | |     \  /     | |____   | | \ \
 \_____|  |______|  \____/       \/      |______|  |_|  \_\

Hello! I'm Clover.
What can I do for you?

____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T] [X] read | annotate book
2.[D] [ ] return book (by: June 6th)
3.[E] [ ] project meeting (from: Aug 6th 2-4pm to: Aug 6th 4-6pm)
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Recover from invalid saved data

**Aim:** Verify that malformed saved data does not prevent Clover from starting.

**Saved data:**
```text
Q | 0 | unknown task
```

**Input:**
```text
list
bye
```

**Expected output:**
```text
____________________________________________________________
I could not load your saved tasks. Starting with an empty list.
____________________________________________________________
____________________________________________________________
  _____    _         ____    __      __   ______    _____
 / ____|  | |       / __ \   \ \    / /  |  ____|  |  __ \
| |       | |      | |  | |   \ \  / /   | |__     | |__) |
| |       | |      | |  | |    \ \/ /    |  __|    |  _  /
| |____   | |____  | |  | |     \  /     | |____   | | \ \
 \_____|  |______|  \____/       \/      |______|  |_|  \_\

Hello! I'm Clover.
What can I do for you?

____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add an event

**Aim:** Verify that Clover accepts a correctly formatted event, displays it, and reports the correct task count.

**Input:**
```text
event tutorial /from Monday 2pm /to Monday 4pm
bye
```

**Expected output:**
```text
____________________________________________________________
  _____    _         ____    __      __   ______    _____
 / ____|  | |       / __ \   \ \    / /  |  ____|  |  __ \
| |       | |      | |  | |   \ \  / /   | |__     | |__) |
| |       | |      | |  | |    \ \/ /    |  __|    |  _  /
| |____   | |____  | |  | |     \  /     | |____   | | \ \
 \_____|  |______|  \____/       \/      |______|  |_|  \_\

Hello! I'm Clover.
What can I do for you?

____________________________________________________________
____________________________________________________________
Got it. I've added this task:
 [E] [ ] tutorial (from: Monday 2pm to: Monday 4pm)
Now you have 1 in the list.
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved data:**
```text
E | 0 | tutorial | Monday 2pm | Monday 4pm
```

## Test case: Reject an event without times

**Aim:** Verify that an incomplete event command produces guidance instead of terminating the program.

**Input:**
```text
event tutorial
bye
```

**Expected output:**
```text
____________________________________________________________
  _____    _         ____    __      __   ______    _____
 / ____|  | |       / __ \   \ \    / /  |  ____|  |  __ \
| |       | |      | |  | |   \ \  / /   | |__     | |__) |
| |       | |      | |  | |    \ \/ /    |  __|    |  _  /
| |____   | |____  | |  | |     \  /     | |____   | | \ \
 \_____|  |______|  \____/       \/      |______|  |_|  \_\

Hello! I'm Clover.
What can I do for you?

____________________________________________________________
____________________________________________________________
Please use the format: event DESCRIPTION /from START /to END
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Reject incorrect inputs without crashing

**Aim:** Verify that Clover explains common invalid inputs and remains available for the next command.

**Input:**
```text
todo
mark one
unmark 1
deadline submit
event tutorial /from Monday

bye
```

**Expected output:**
```text
____________________________________________________________
  _____    _         ____    __      __   ______    _____
 / ____|  | |       / __ \   \ \    / /  |  ____|  |  __ \
| |       | |      | |  | |   \ \  / /   | |__     | |__) |
| |       | |      | |  | |    \ \/ /    |  __|    |  _  /
| |____   | |____  | |  | |     \  /     | |____   | | \ \
 \_____|  |______|  \____/       \/      |______|  |_|  \_\

Hello! I'm Clover.
What can I do for you?

____________________________________________________________
____________________________________________________________
The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
Please enter a valid task number to mark.
____________________________________________________________
____________________________________________________________
Please enter a valid task number to unmark.
____________________________________________________________
____________________________________________________________
Please use the format: deadline DESCRIPTION /by DUE DATE
____________________________________________________________
____________________________________________________________
Please use the format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Please enter a command or task description.
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
