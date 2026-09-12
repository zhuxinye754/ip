 # Clover UI test plan

The `test-ui` skill compiles Clover and launches `clover.Clover` for every command-line case. Expected output is compared exactly, excluding only a final newline. Cases marked **Type:** Manual require a JavaFX visual check and are skipped by the console runner.

Each case uses an isolated working directory. A case can include an optional **Saved data** block to provide the contents of `data/clover.txt` before Clover starts.

## Test case: Launch the JavaFX window

**Type:** Manual

**Aim:** Verify that Gradle starts the JavaFX application through `clover.Launcher`.

**Input:**

1. Run `./gradlew run` with JDK 25 selected.
2. Confirm that an FXML-based JavaFX window appears.
3. Enter `todo read book` and press Enter. Confirm that the left-aligned reply confirms that the task was added.
4. Enter `list` and select `Send`. Confirm that the reply lists `read book`, showing that chat commands use Clover's task list.
5. Add enough messages to exceed the visible area and confirm that it scrolls to the latest response.
6. Enter `mark 1` and confirm that Clover's reply bubble uses the marked-task style. Enter `delete 1` and confirm it uses the delete-task style.
7. Resize the window. Confirm that the input field, Send button, and scroll pane remain anchored to their respective edges.
8. Confirm that the background image, styled message bubbles, and button hover/pressed states appear.
9. Close the window.

**Expected output:** Clover opens an FXML-based JavaFX window with the Part 5 responsive, styled chat layout and exits cleanly when the window is closed.

## Test case: Run the extracted task commands

**Aim:** Verify that the command classes add, update, delete, and list the correct tasks.

**Input:**
```text
todo read book
deadline submit report /by 2026-09-01
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
Nice! I've marked this task as done: [D] [X] submit report (by: Sep 1 2026)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet: [D] [ ] submit report (by: Sep 1 2026)
____________________________________________________________
____________________________________________________________
Noted. I've removed this task: [T] [ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D] [ ] submit report (by: Sep 1 2026)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved data:**
```text
D | 0 | submit report | 2026-09-01
```

## Test case: Find tasks by keyword

**Aim:** Verify that Clover finds matching task descriptions regardless of letter case and keeps their original order.

**Saved data:**
```text
T | 1 | read book
D | 1 | return book | 2026-06-06
T | 0 | buy groceries
```

**Input:**
```text
find BOOK
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
Here are the matching tasks in your list:
1.[T] [X] read book
2.[D] [X] return book (by: Jun 6 2026)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
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
read book

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
Unknown command. Please use: todo, deadline, event, list, find, mark, unmark, delete, add-tutoree, list-tutorees, find-tutoree, or bye.
____________________________________________________________
____________________________________________________________
Please enter a command or task description.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Manage tutorees and link tasks

**Aim:** Verify that Clover stores tutoree records and links each supported task type only to an existing tutoree.

**Input:**
```text
add-tutoree Alice Tan /address 12 Example Road /fee $50/hour
todo prepare worksheet /for Alice Tan
deadline collect fee /by 2026-09-30 /for alice tan
event lesson /from 2026-09-20 /to 2026-09-20 /for Alice Tan
list-tutorees
find-tutoree ALI
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
Got it. I've added this tutoree:
Alice Tan
Address: 12 Example Road
Fee: $50/hour
Now you have 1 tutoree in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task: [T] [ ] prepare worksheet (for: Alice Tan)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task: [D] [ ] collect fee (by: Sep 30 2026) (for: Alice Tan)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task: [E] [ ] lesson (from: Sep 20 2026 to: Sep 20 2026) (for: Alice Tan)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tutorees in your list:
1. Alice Tan
   Address: 12 Example Road
   Fee: $50/hour
____________________________________________________________
____________________________________________________________
Here are the matching tutorees in your list:
1. Alice Tan
   Address: 12 Example Road
   Fee: $50/hour
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T] [ ] prepare worksheet (for: Alice Tan)
2.[D] [ ] collect fee (by: Sep 30 2026) (for: Alice Tan)
3.[E] [ ] lesson (from: Sep 20 2026 to: Sep 20 2026) (for: Alice Tan)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected saved data:**
```text
T | 0 | prepare worksheet | Alice Tan
D | 0 | collect fee | 2026-09-30 | Alice Tan
E | 0 | lesson | 2026-09-20 | 2026-09-20 | Alice Tan
```

**Expected saved tutoree data:**
```text
S | Alice Tan | 12 Example Road | $50/hour
```

## Test case: Reject an unknown linked tutoree

**Aim:** Verify that a task cannot be linked to a student who is absent from the tutoree directory.

**Input:**
```text
todo prepare worksheet /for Alice Tan
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
No tutoree named "Alice Tan" exists. Add the tutoree before linking a task to them.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
