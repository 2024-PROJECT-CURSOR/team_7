package adapter;

public class Schedule {
    private int id;
    private String date; // yyyy-MM-dd 형식
    private String task;
    private boolean isCompleted;

    public Schedule(int id, String date, String task, boolean isCompleted) {
        this.id = id;
        this.date = date;
        this.task = task;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTask() {
        return task;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}