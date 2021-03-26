package seedu.duke.task;

import seedu.duke.ModuleInfo;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

//@@author hazelhedmine-reused
//Reused from https://github.com/hazelhedmine/ip/blob/master/src/main/java/duke/task/TaskList.java
//with modifications and additional methods
public class TaskList {

    public static ArrayList<Task> tasks;
    public static ArrayList<Assignment> assignments;
    public static ArrayList<Midterm> midterms;
    public static ArrayList<FinalExam> finalExams;
    public static HashMap<String, ArrayList<Task>> pinnedTasks;

    /**
     * Constructs tasklist.
     */
    public TaskList() {
        tasks = new ArrayList<>();
        assignments = new ArrayList<>();
        midterms = new ArrayList<>();
        finalExams = new ArrayList<>();
        pinnedTasks = new HashMap<>();
    }

    public static void addNewTask(int taskTypeNumber) {
        String dateAndTime = "";

        if (ModuleInfo.modules.isEmpty()) {
            Ui.printNoModulesMessage();
            return;
        }

        Ui.printAddTaskModuleMessage(taskTypeNumber);
        String module = getModule();
        Ui.printHorizontalLine();
        Ui.printAddTaskDescriptionMessage(taskTypeNumber);
        String description = Ui.readCommand();
        Ui.printHorizontalLine();
        if (taskTypeNumber != 1) {
            dateAndTime = TaskList.getDate(taskTypeNumber) + ", " + TaskList.getTime(taskTypeNumber);
        }
        Ui.printAddMessageAfterCompletedTask();
        String message = Ui.readCommand();
        Ui.printHorizontalLine();

        switch (taskTypeNumber) {
        case 1:
            addTask(module, description, message);
            break;
        case 2:
            addAssignment(module, description, message, dateAndTime);
            break;
        case 3:
            addMidterm(module, description, message, dateAndTime);
            break;
        case 4:
            addFinalExam(module, description, message, dateAndTime);
            break;
        default:
            Ui.printInvalidIntegerMessage();
        }
    }

    public static void addTask(String module, String description, String message) {
        Task task = new Task(module, description, message);
        tasks.add(task);
        assert tasks.contains(task) : "Task was not added to task list";
        Ui.printAddedTaskMessage(task);
    }

    public static void addAssignment(String module, String description,
                                     String message, String dateAndTime) {
        Assignment assignment = new Assignment(module, description, message, dateAndTime);
        assignments.add(assignment);
        assert assignments.contains(assignment) : "Assignment was not added to assignment list";
        Ui.printAddedTaskMessage(assignment);
    }

    public static void addMidterm(String module, String description,
                                  String message, String dateAndTime) {
        Midterm midterm = new Midterm(module, description, message, dateAndTime);
        midterms.add(midterm);
        assert midterms.contains(midterm) : "Midterm was not added to midterm list";
        Ui.printAddedTaskMessage(midterm);
    }

    public static void addFinalExam(String module, String description,
                                String message, String dateAndTime) {
        FinalExam finalExam = new FinalExam(module, description, message, dateAndTime);
        finalExams.add(finalExam);
        assert finalExams.contains(finalExam) : "Final exam was not added to final exam list";
        Ui.printAddedTaskMessage(finalExam);
    }

    public static String getModule() {
        for (int i = 1; i <= ModuleInfo.modules.size(); ++i) {
            System.out.println("[" + i + "] " + ModuleInfo.modules.get(i - 1).getName());
        }
        int moduleNumber = Integer.parseInt(Ui.readCommand());
        String module = ModuleInfo.modules.get(moduleNumber - 1).getName();

        return module;
    }

    public static String getTime(int taskNumber) {
        while (true) {
            try {
                Ui.printAddTaskTimeMessage(taskNumber);
                String time = validTime(Ui.readCommand());
                assert !time.isBlank() : "Time field cannot be empty";
                Ui.printHorizontalLine();
                return time;
            } catch (DateTimeParseException e) {
                Ui.printInvalidTimeFormat();
            }
        }
    }

    public static String getDate(int taskNumber) {
        while (true) {
            try {
                Ui.printAddTaskDateMessage(taskNumber);
                String date = validDate(Ui.readCommand());
                assert !date.isBlank() : "Time field cannot be empty";
                Ui.printHorizontalLine();
                return date;
            } catch (DateTimeParseException e) {
                Ui.printInvalidDateFormat();
            }
        }
    }

    public static String validTime(String time) throws DateTimeParseException {
        LocalTime taskTime = LocalTime.parse(time);
        String formattedTime = taskTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        return formattedTime;
    }

    public static String validDate(String date) throws DateTimeParseException {
        LocalDate taskDate = LocalDate.parse(date);
        String formattedDate = taskDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        return formattedDate;
    }

    public static boolean isValidTaskType(String command) {
        try {
            int taskNumber = Integer.parseInt(command);
            boolean isInvalidTaskType = (taskNumber <= 0) || (taskNumber >= 5);
            assert !command.isBlank() : "Task number cannot be empty";
            if (!isInvalidTaskType) {
                return true;
            }
            System.out.println("Please enter a valid integer from the list.");
        } catch (NumberFormatException n) {
            System.out.println("Error! Enter an integer.");
        }
        return false;
    }

    public static int getTaskNumber() {
        int taskNumber;
        while (true) {
            String command = Ui.readCommand();
            if (isValidTaskType(command)) {
                taskNumber = Integer.parseInt(command);
                break;
            }
        }
        return taskNumber;
    }

    public static void markOrUnmarkTask(int taskTypeNumber) {
        if (taskListIsEmpty(taskTypeNumber)) {
            Ui.printTaskListIsEmptyMessage();
            return;
        }
        Ui.printSelectTaskNumberToMarkOrUnmark(taskTypeNumber);
        while (true) {
            try {
                int taskNumber = Integer.parseInt(Ui.readCommand());
                Ui.printHorizontalLine();
                switch (taskTypeNumber) {
                case 1:
                    toggleTaskStatus(taskNumber);
                    break;
                case 2:
                    toggleAssigmentStatus(taskNumber);
                    break;
                case 3:
                    toggleMidtermStatus(taskNumber);
                    break;
                case 4:
                    toggleFinalExamStatus(taskNumber);
                    break;
                default:
                    Ui.printInvalidIntegerMessage();
                }
                return;
            } catch (NumberFormatException e) {
                Ui.printInvalidIntegerMessage();
            } catch (IndexOutOfBoundsException e) {
                Ui.printInvalidTaskNumberMessage();
            }
        }
    }

    public static void deleteTask(int taskTypeNumber) {
        if (taskListIsEmpty(taskTypeNumber)) {
            Ui.printTaskListIsEmptyMessage();
            return;
        }
        Ui.printSelectTaskNumberToDelete(taskTypeNumber);
        while (true) {
            try {
                int taskNumber = Integer.parseInt(Ui.readCommand());
                Ui.printHorizontalLine();
                switch (taskTypeNumber) {
                case 1:
                    findAndDeleteTask(taskNumber);
                    break;
                case 2:
                    findAndDeleteAssigment(taskNumber);
                    break;
                case 3:
                    findAndDeleteMidterm(taskNumber);
                    break;
                case 4:
                    findAndDeleteFinalExam(taskNumber);
                    break;
                default:
                    Ui.printInvalidIntegerMessage();
                }
                return;
            } catch (NumberFormatException e) {
                Ui.printInvalidIntegerMessage();
            } catch (IndexOutOfBoundsException e) {
                Ui.printInvalidTaskNumberMessage();
            }
        }
    }

    public static boolean taskListIsEmpty(int taskTypeNumber) {
        boolean isEmpty = false;
        switch (taskTypeNumber) {
        case 1:
            isEmpty = tasks.isEmpty();
            break;
        case 2:
            isEmpty = assignments.isEmpty();
            break;
        case 3:
            isEmpty = midterms.isEmpty();
            break;
        case 4:
            isEmpty = finalExams.isEmpty();
            break;
        default:
            Ui.printInvalidIntegerMessage();
        }
        return isEmpty;
    }

    public static void toggleTaskStatus(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        String taskStatus = task.getStatus();
        String done = "[DONE] ";
        String notDone = "[    ] ";
        if (taskStatus.equals(done)) {
            Ui.printTaskisDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsUnDone();
                assert task.getStatus().equals("[    ] ") : "Task should not be marked as done";
                Ui.printUnmarkedTaskMessage(task);
            }
        } else if (taskStatus.equals(notDone)) {
            Ui.printTaskisNotDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsDone();
                assert task.getStatus().equals("[DONE] ") : "Task should be marked as done";
                Ui.printMarkedTaskMessage(task);
            }
        }
    }

    public static void toggleAssigmentStatus(int taskNumber) {
        Assignment task = assignments.get(taskNumber - 1);
        String taskStatus = task.getStatus();
        String done = "[DONE] ";
        String notDone = "[    ] ";
        if (taskStatus.equals(done)) {
            Ui.printTaskisDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsUnDone();
                assert task.getStatus().equals("[    ] ") : "Task should not be marked as done";
                Ui.printUnmarkedTaskMessage(task);
            }
        } else if (taskStatus.equals(notDone)) {
            Ui.printTaskisNotDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsDone();
                assert task.getStatus().equals("[DONE] ") : "Task should be marked as done";
                Ui.printMarkedTaskMessage(task);
            }
        }
    }

    public static void toggleMidtermStatus(int taskNumber) {
        Midterm task = midterms.get(taskNumber - 1);
        String taskStatus = task.getStatus();
        String done = "[DONE] ";
        String notDone = "[    ] ";
        if (taskStatus.equals(done)) {
            Ui.printTaskisDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsUnDone();
                assert task.getStatus().equals("[    ] ") : "Task should not be marked as done";
                Ui.printUnmarkedTaskMessage(task);
            }
        } else if (taskStatus.equals(notDone)) {
            Ui.printTaskisNotDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsDone();
                assert task.getStatus().equals("[DONE] ") : "Task should be marked as done";
                Ui.printMarkedTaskMessage(task);
            }
        }
    }

    public static void toggleFinalExamStatus(int taskNumber) {
        FinalExam task = finalExams.get(taskNumber - 1);
        String taskStatus = task.getStatus();
        String done = "[DONE] ";
        String notDone = "[    ] ";
        if (taskStatus.equals(done)) {
            Ui.printTaskisDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsUnDone();
                assert task.getStatus().equals("[    ] ") : "Task should not be marked as done";
                Ui.printUnmarkedTaskMessage(task);
            }
        } else if (taskStatus.equals(notDone)) {
            Ui.printTaskisNotDoneMessage();
            String input = Ui.readCommand().trim();
            assert input.equals("Y") : "if input is not Y, should catch exception";
            if (input.equals("Y")) {
                task.markAsDone();
                assert task.getStatus().equals("[DONE] ") : "Task should be marked as done";
                Ui.printMarkedTaskMessage(task);
            }
        }
    }

    public static void findAndDeleteTask(int taskNumber) {
        Task deletedTask = tasks.get(taskNumber - 1);
        tasks.remove(deletedTask);
        boolean typeTaskIsPinned = pinnedTasks.containsKey("[Task]");
        if (typeTaskIsPinned) {
            assert pinnedTasks.containsKey("[Task]") : "Pinned task list for task should exist";
            if (pinnedTasks.get("[Task]").contains((deletedTask))) {
                assert !pinnedTasks.get("[Task]").isEmpty() : "Pinned task list should not be empty";
                pinnedTasks.get("[Task]").remove(deletedTask);
            }
        }
        Ui.printDeletedTaskMessage(deletedTask);
    }

    public static void findAndDeleteAssigment(int taskNumber) {
        Assignment deletedAssignment = assignments.get(taskNumber - 1);
        assignments.remove(deletedAssignment);
        boolean typeAssignmentIsPinned = pinnedTasks.containsKey("[Assignment]");
        if (typeAssignmentIsPinned) {
            assert pinnedTasks.containsKey("[Assignment]") : "Pinned task list for assignment should exist";
            if (pinnedTasks.get("[Assignment]").contains((deletedAssignment))) {
                assert !pinnedTasks.get("[Assignment]").isEmpty() : "Pinned task list should not be empty";
                pinnedTasks.get("[Assignment]").remove(deletedAssignment);
            }
        }
        Ui.printDeletedTaskMessage(deletedAssignment);
    }

    public static void findAndDeleteMidterm(int taskNumber) {
        Midterm deletedMidterm = midterms.get(taskNumber - 1);
        midterms.remove(deletedMidterm);
        boolean typeMidtermIsPinned = pinnedTasks.containsKey("[Midterm]");
        if (typeMidtermIsPinned) {
            assert pinnedTasks.containsKey("[Midterm]") : "Pinned task list for midterm should exist";
            if (pinnedTasks.get("[Midterm]").contains((deletedMidterm))) {
                assert !pinnedTasks.get("[Midterm]").isEmpty() : "Pinned task list should not be empty";
                pinnedTasks.get("[Midterm]").remove(deletedMidterm);
            }
        }
        Ui.printDeletedTaskMessage(deletedMidterm);
    }

    public static void findAndDeleteFinalExam(int taskNumber) {
        FinalExam deletedFinalExam = finalExams.get(taskNumber - 1);
        finalExams.remove(deletedFinalExam);
        boolean typeFinalExamIsPinned = pinnedTasks.containsKey("[Final Exam]");
        if (typeFinalExamIsPinned) {
            assert pinnedTasks.containsKey("[Final Exam]") : "Pinned task list for final exam should exist";
            if (pinnedTasks.get("[Final Exam]").contains((deletedFinalExam))) {
                assert !pinnedTasks.get("[Final Exam]").isEmpty() : "Pinned task list should not be empty";
                pinnedTasks.get("[Final Exam]").remove(deletedFinalExam);
            }
        }
        Ui.printDeletedTaskMessage(deletedFinalExam);
    }

    public static void pinTask(int taskTypeNumber) {
        if (taskListIsEmpty(taskTypeNumber)) {
            Ui.printTaskListIsEmptyMessage();
            return;
        }
        Ui.printSelectTaskNumberToPin(taskTypeNumber);
        while (true) {
            try {
                int taskNumber = Integer.parseInt(Ui.readCommand());
                Ui.printHorizontalLine();
                switch (taskTypeNumber) {
                case 1:
                    addTaskToPinnedTasks(tasks.get(taskNumber - 1), "[Task]");
                    break;
                case 2:
                    addTaskToPinnedTasks(assignments.get(taskNumber - 1), "[Assignment]");
                    break;
                case 3:
                    addTaskToPinnedTasks(midterms.get(taskNumber - 1), "[Midterm]");
                    break;
                case 4:
                    addTaskToPinnedTasks(finalExams.get(taskNumber - 1), "[Final Exam]");
                    break;
                default:
                    Ui.printInvalidIntegerMessage();
                }
                return;
            } catch (NumberFormatException e) {
                Ui.printInvalidIntegerMessage();
            } catch (IndexOutOfBoundsException e) {
                Ui.printInvalidTaskNumberMessage();
            }
        }
    }

    public static void addTaskToPinnedTasks(Task task, String taskTypeName) {
        pinnedTasks.computeIfAbsent(taskTypeName, k -> new ArrayList<>());
        if (pinnedTasks.get(taskTypeName).contains((task))) {
            Ui.printTaskAlreadyPinnedMessage();
            return;
        }
        pinnedTasks.get(taskTypeName).add(task);
        assert pinnedTasks.get(taskTypeName).contains(task) : "Task was not added to pinned list";
        Ui.printPinnedTaskMessage(task);
        return;
    }

}
