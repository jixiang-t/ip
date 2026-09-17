# Gnaix User Guide

Gnaix helps you manage todos, deadlines, events, keyword searches, date
searches, and tags through a JavaFX chat-style interface.

## Starting Gnaix

Run the application from the Gradle wrapper:

```bash
./gradlew run
```

On Windows:

```bash
.\gradlew.bat run
```

You can also build and run the executable JAR:

```bash
./gradlew build
java -jar build/libs/gnaix.jar
```

## Adding tasks

Add a todo:

```text
todo read lecture notes
```

Add a deadline:

```text
deadline submit quiz /by 2026-09-10
```

Add an event:

```text
event project meeting /from 2026-09-15 1000 /to 2026-09-15 1100
```

## Listing and updating tasks

Show all tasks:

```text
list
```

Mark a task as done:

```text
mark 1
```

Mark a task as not done:

```text
unmark 1
```

Delete a task:

```text
delete 1
```

Task numbers are the one-based numbers shown in the current list.

## Searching

Find tasks by keyword:

```text
find quiz
```

Find tasks scheduled for a date:

```text
date 2026-09-10
```

Dates use `yyyy-MM-dd`.

## Tags

Add tags by placing them at the end of a task command:

```text
todo revise chapters #school #urgent
deadline submit report /by 2026-09-20 #school
event consultation /from 2026-09-21 1400 /to 2026-09-21 1430 #school
```

Search by one or more tags:

```text
tag #school
tag #school #urgent
```

When multiple tags are supplied, Gnaix returns tasks that contain all of those
tags. Tags are case-insensitive and duplicate tags are stored once.

## Exiting

Close the application with:

```text
bye
```

## Error handling

Gnaix reports invalid commands, invalid task numbers, malformed dates, malformed
event times, and malformed tags without crashing.
