# Clover UI test plan

The runner compiles the Java files in `src/main/java` and launches `Clover` for every case. Expected output is compared exactly, excluding only a final newline.

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
