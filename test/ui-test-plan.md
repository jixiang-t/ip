# UI Test Plan

This file records command-line UI test cases for the current Level-6 Gnaix implementation.

## Setup

- Working directory: project root
- Java version: Java 25
- Compile command: `javac -d out src/main/java/*.java`
- Run command: `java -cp out Gnaix`
- Note: Each test case starts a new program session, so tasks added in one test case do not carry over to another test case.

## Test Cases

### Test Case 1: Add a todo task

- Aim: Verify that the `todo` command adds a todo task and reports the updated task count.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo read book
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 2: Add a deadline task

- Aim: Verify that the `deadline` command records the task description and `/by` value.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
deadline return book /by Sunday
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 3: Add an event task

- Aim: Verify that the `event` command records the task description, `/from` value, and `/to` value.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
event project meeting /from Monday 2pm /to Monday 4pm
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 4: List todo, deadline, and event tasks

- Aim: Verify that `list` displays all added tasks in insertion order with the correct task type labels.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo read book
deadline return book /by Sunday
event project meeting /from Monday 2pm /to Monday 4pm
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 5: Mark a task as done

- Aim: Verify that `mark` changes a task status from not done to done.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo read book
mark 1
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 6: Unmark a task as not done

- Aim: Verify that `unmark` changes a completed task back to not done.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo read book
mark 1
unmark 1
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 7: Reject empty and invalid task-creation commands

- Aim: Verify that empty input and task commands with missing required information show clear error messages and do not add tasks.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text

todo
deadline
deadline return book
deadline return book /by
deadline /by Sunday
event
event project meeting /to Tuesday
event project meeting
event /from Monday /to Tuesday
event project meeting /from  /to Tuesday
event project meeting /from Monday /to
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Please enter a command! :(
____________________________________________________________
NO DESCRIPTION GIVEN! :(
____________________________________________________________
A deadline needs a description and a /by date! :(
____________________________________________________________
A deadline needs a description and a /by date! :(
____________________________________________________________
A deadline needs a description and a /by date! :(
____________________________________________________________
A deadline needs a description and a /by date! :(
____________________________________________________________
Not enough info given! :(
____________________________________________________________
Not enough info given! :(
____________________________________________________________
Not enough info given! :(
____________________________________________________________
Not enough info given! :(
____________________________________________________________
An event needs a /from time, and /to time! :(
____________________________________________________________
An event needs a description and timings! :(
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 8: Reject invalid mark and unmark task numbers

- Aim: Verify that `mark` and `unmark` handle missing, nonnumeric, and out-of-range task numbers without changing the task list.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
list
mark
mark abc
mark 1
todo read book
mark 2
unmark
unmark abc
unmark 2
unmark 1
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 9: Reject unknown commands

- Aim: Verify that an unrecognized command shows an error message and the program continues running.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
hello
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
That's not a valid command! :(
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 10: Failed task-creation commands do not change the list or task counter

- Aim: Verify that invalid `deadline` and `event` commands interleaved with valid commands do not add tasks or increase the task counter.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline
deadline beta /by Friday
event broken /from  /to 4pm
event gamma /from 1pm /to 2pm
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
A deadline needs a description and a /by date! :(
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
An event needs a /from time, and /to time! :(
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 3 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
3. [E][ ] gamma (from: 1pm to: 2pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 11: Failed status commands do not change the list or task counter

- Aim: Verify that invalid `mark` and `unmark` commands interleaved with valid commands do not add tasks, remove tasks, or change task status.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
mark
mark abc
mark 2
mark 1
unmark xyz
unmark 3
unmark 1
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] alpha
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] alpha
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 12: Exit with bye

- Aim: Verify that `bye` exits the program with the farewell message.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 13: Delete a task from the middle of the list

- Aim: Verify that `delete` removes a middle task and preserves the order of the remaining tasks.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
todo beta
todo gamma
delete 2
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [T][ ] beta
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [T][ ] gamma
Now you have 3 tasks in the list.
____________________________________________________________
Noted. I've removed this task:
  [T][ ] beta
Now you have 2 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [T][ ] gamma
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 14: Delete the first task

- Aim: Verify that `delete` removes the first task and renumbers the remaining tasks correctly.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline beta /by Friday
event gamma /from 1pm /to 2pm
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 3 tasks in the list.
____________________________________________________________
Noted. I've removed this task:
  [T][ ] alpha
Now you have 2 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] beta (by: Friday)
2. [E][ ] gamma (from: 1pm to: 2pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 15: Delete the last task

- Aim: Verify that `delete` removes the last task without changing the earlier tasks.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline beta /by Friday
event gamma /from 1pm /to 2pm
delete 3
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 3 tasks in the list.
____________________________________________________________
Noted. I've removed this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 2 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 16: Delete the only task in the list

- Aim: Verify that `delete` can remove the only task and leave an empty list.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo solo
delete 1
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] solo
Now you have 1 tasks in the list.
____________________________________________________________
Noted. I've removed this task:
  [T][ ] solo
Now you have 0 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 17: Reject invalid delete task numbers without changing the list

- Aim: Verify that `delete` rejects missing, nonnumeric, zero, and too-large task numbers, and that each failed delete leaves the task list unchanged.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline beta /by Friday
delete
list
delete abc
list
delete 0
list
delete 3
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
____________________________________________________________
That task number is not a number! :(
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
____________________________________________________________
That task number does not exist! :(
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 18: Delete preserves status and type of remaining tasks

- Aim: Verify that deleting a task does not affect the completion status or task type of the remaining tasks.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline beta /by Friday
event gamma /from 1pm /to 2pm
mark 1
mark 3
delete 2
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 3 tasks in the list.
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] alpha
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] gamma (from: 1pm to: 2pm)
____________________________________________________________
Noted. I've removed this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][X] alpha
2. [E][X] gamma (from: 1pm to: 2pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Test Case 19: ArrayList refactoring preserves existing task behaviour

- Aim: Verify that the ArrayList-based implementation preserves the existing `todo`, `deadline`, `event`, `list`, `mark`, and `unmark` behaviour.
- Command:

```bash
java -cp out Gnaix
```

- Inputs:

```text
todo alpha
deadline beta /by Friday
event gamma /from 1pm /to 2pm
list
mark 2
unmark 2
list
bye
```

- Expected output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] alpha
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] beta (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] gamma (from: 1pm to: 2pm)
Now you have 3 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
3. [E][ ] gamma (from: 1pm to: 2pm)
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] beta (by: Friday)
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] beta (by: Friday)
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] alpha
2. [D][ ] beta (by: Friday)
3. [E][ ] gamma (from: 1pm to: 2pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Console Session Record

The expected output above is based on command-line sessions after compiling with `javac -d out src/main/java/*.java`.

Command:

```bash
java -cp out Gnaix
```

Console input:

```text
todo read book
deadline return book /by Sunday
event project meeting /from Monday 2pm /to Monday 4pm
list
mark 1
unmark 1
bye
```

Console output:

```text
____________________________________________________________
  ____ _   _    _    _____  __
 / ___| \ | |  / \  |_ _\ \/ /
| |  _|  \| | / _ \  | | \  /
| |_| | |\  |/ ___ \ | | /  \
 \____|_| \_/_/   \_\___/_/\_\
Hello! I'm Gnaix
What can I do for you?
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (from: Monday 2pm to: Monday 4pm)
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
