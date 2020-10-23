package ch.ost.rj.mge.mind_supporter;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoStorage {
    private static ArrayList<ToDo> toDoArrayList;
    public static File persistFile;

    public static void persist() throws IOException {
        FileOutputStream fos = new FileOutputStream(persistFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toDoArrayList);
        oos.close();
        fos.close();
    }

    public static void replaceToDoArrayListWithPersistedToDoArrayList() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(persistFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        toDoArrayList = (ArrayList<ToDo>) ois.readObject();
        ois.close();
        fis.close();
    }

    public static ArrayList<ToDo> getFinished(){
        ArrayList<ToDo> out = new ArrayList<>();
        for(ToDo tmp : toDoArrayList){
            if(tmp.isFinished()){
                out.add(tmp);
            }
        }
        return out;
    }

    public static ArrayList<ToDo> getPending(){
        ArrayList<ToDo> out = new ArrayList<>();
        for(ToDo tmp : toDoArrayList){
            if(!tmp.isFinished()){
                out.add(tmp);
            }
        }
        return out;
    }

    static{
        toDoArrayList = new ArrayList<>();
        persistFile = new File(App.getContext().getFilesDir(), "todos");
        try {
            ToDoStorage.replaceToDoArrayListWithPersistedToDoArrayList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ToDo> getToDoArrayList() {
        return toDoArrayList;
    }

    public static void setToDoArrayList(ArrayList<ToDo> toDoArrayList) {
        ToDoStorage.toDoArrayList = toDoArrayList;
    }

    public static void addToToDoArrayList(ToDo currentToDo) throws IOException {
        for(ToDo x : toDoArrayList){
            if (x.getTitle().equals(currentToDo.getTitle())) {
                x.overrideWithOtherToDo(currentToDo);
                sortDueTime();
                persist();
                return;
            }
        }
        toDoArrayList.add(new ToDo(currentToDo.getTitle(), currentToDo.getDueDateTime(), currentToDo.getDurationMinutes(), currentToDo.getPriority(), currentToDo.isFinished(), currentToDo.getImage(), currentToDo.getNote()));
        sortDueTime();
        persist();
    }

    public static void removeToDoFromArrayList(ToDo todelete) throws IOException {
        toDoArrayList.remove(todelete);
        sortDueTime();
        persist();
    }

    private static void sortDueTime(){
        toDoArrayList.sort((a,b)->{return a.getDueDateTime().compareTo(b.getDueDateTime());});
    }
}
