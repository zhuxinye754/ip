 # Clover UI test plan

The runner compiles the Java files in `src/main/java` and launches `Clover` for every case. Expected output is compared exactly, excluding only a final newline.

Each case uses an isolated working directory. A case can include an optional **Saved data** block to provide the contents of `data/clover.txt` before Clover starts.

## Test case: Run the extracted task commands

**Aim:** Verify that the command classes add, update, delete, and list the correct tasks.

**Input:**
```text
todo read book
deadline submit report /by 2026-09-01
plain reminder
mark 2
unmark 2
delete 1
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
Got it. I've added this task: [T] [ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task: [D] [ ] submit report (by: Sep 1 2026)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
[ ] plain reminder
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done: [D] [X] submit report (by: Sep 1 2026)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet: [D] [ ] submit report (by: Sep 1 2026)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task: [T] [ ] read book
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D] [ ] submit report (by: Sep 1 2026)
2.[ ] plain reminder
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved data:**
```text
D | 0 | submit report | 2026-09-01
N | 0 | plain reminder
```

## Test case: Load saved tasks

**Aim:** Verify that Clover restores saved tasks, including their task type and completion status, when it starts.

**Saved data:**
```text
T | 1 | read \| annotate book
D | 0 | return book | 2019-12-02
E | 0 | project meeting | 2019-08-06 | 2019-08-07
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
2.[D] [ ] return book (by: Dec 2 2019)
3.[E] [ ] project meeting (from: Aug 6 2019 to: Aug 7 2019)
____________________________________________________________
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
I could not load your saved tasks. Starting with an empty list.
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
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add an event

**Aim:** Verify that Clover accepts a correctly formatted event, displays it, and reports the correct task count.

**Input:**
```text
event tutorial /from 2019-12-02 /to 2019-12-04
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
Got it. I've added this task: [E] [ ] tutorial (from: Dec 2 2019 to: Dec 4 2019)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved data:**
```text
E | 0 | tutorial | 2019-12-02 | 2019-12-04
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
event tutorial /from 2019-12-02
deadline return book /by tomorrow
event tutorial /from 2019-12-02 /to tomorrow

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
Please enter dates in the format yyyy-MM-dd.
____________________________________________________________
____________________________________________________________
Please enter dates in the format yyyy-MM-dd.
____________________________________________________________
____________________________________________________________
Please enter a command or task description.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
