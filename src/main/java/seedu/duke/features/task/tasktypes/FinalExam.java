package seedu.duke.features.task.tasktypes;

//@@author hazelhedmine-reused
//Reused from https://github.com/hazelhedmine/ip/blob/master/src/main/java/duke/task/Deadline.java
//with modifications
public class FinalExam extends Task {
    protected String on;

    public FinalExam(String module, String description, String message, String on) {
        super(module, description, message);
        this.on = on;
    }

    public String getOn() {
        return on;
    }

    @Override
    public String getTaskType() {
        return "[Final Exam]";
    }

    @Override
    public String toString() {
        return super.toString() + " (on: " + on + ")";
    }
}
