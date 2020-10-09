package ch.ost.rj.mge.mind_supporter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;

import java.time.LocalDateTime;
    //Jonas = ToDoList Activity
public class ToDo {

    private String title;
    private LocalDateTime dueDateTime;
    private int durationMinutes;
    private int priority;
    private boolean finished;
    private Bitmap image;

    public ToDo(String title, LocalDateTime dueDateTime, int durationMinutes, int priority, boolean finished, Bitmap image){
        this.title=title;
        this.dueDateTime=dueDateTime;
        this.durationMinutes=durationMinutes;
        this.priority=priority;
        this.finished=finished;
        if(image == null){ //User provided no image --> use standard image in to_do_list_item_layout
            return;
        }
        this.image=image; //User provided an image
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
