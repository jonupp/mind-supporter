package ch.ost.rj.mge.mind_supporter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ToDo implements Serializable {

    private String title;
    private LocalDateTime dueDateTime;
    private int durationMinutes;
    private int priority;
    private boolean finished;
    private String image;
    private String note;

    public ToDo(String title, LocalDateTime dueDateTime, int durationMinutes, int priority, boolean finished, String image, String note){
        this.title=title;
        this.dueDateTime=dueDateTime;
        this.durationMinutes=durationMinutes;
        this.priority=priority;
        this.finished=finished;
        this.note = note;
        if(image == null){ //User provided no image --> use standard image in to_do_list_item_layout
            return;
        }
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote(){
        return note; }

    public void setNote(String note){
        this.note = note;
    }
}
